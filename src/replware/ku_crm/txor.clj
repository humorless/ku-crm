(ns replware.ku-crm.txor
  (:require [clojure.data.csv :as csv]
            [clojure.pprint :as pprint]
            [clojure.java.io :as io]
            [replware.ku-crm.db :as db]
            [replware.ku-crm.postgre :as postgre]
            [datalevin.core :as d])
  (:import java.util.Date)
  (:import java.text.SimpleDateFormat))

(defn str->inst [s]
  (when s
    (let [fmt (SimpleDateFormat. "yyyy-MM-dd")]
      (.parse fmt s))))

(defn non-empty-field? [f]
  (some? f))

(defn students->data
  "data -> list of hashmap
  
   TODO: double check the source csv format
         NULL data should be nil value, not empty string"
  [rows]
  (map-indexed
   (fn [idx {:keys [center_symbol ematter_student_symbol
                student_name birth_date mobile_phone_number
                phone_number]}]
     (when (zero? (mod idx 100))
       (prn "processsing to the " (inc idx) "order of student data."))
     (let [base {:name student_name}
           datum (cond-> base
                   (non-empty-field? birth_date) (assoc :birth birth_date)
                   (non-empty-field? phone_number) (assoc :telephone phone_number)
                   (non-empty-field? mobile_phone_number) (assoc :mobile mobile_phone_number)
                   (non-empty-field? center_symbol) (assoc :classroom-id center_symbol)
                   (non-empty-field? ematter_student_symbol) (assoc :old-id ematter_student_symbol))]
       datum))
   rows))

(defn map->nsmap
  [m n]
  (reduce-kv (fn [acc k v]
               (let [new-kw (if (and (keyword? k)
                                     (not (qualified-keyword? k)))
                              (keyword (str n) (name k))
                              k)]
                 (assoc acc new-kw v)))
             {} m))

(defn class-id->prefix [c-id]
  (cond
    (= 5 (count c-id)) [:classroom/ac "ac"]
    (> (count c-id) 2) [:classroom/franchise (subs c-id 0 2)]
    :else [:classroom/ghost "gh"]))

(defn compact [record]
  (into {} (remove (comp nil? second) record)))

(defn duplicated-warn [datum]
  (prn "found duplicated student record:")
  (prn datum))

(defn check-and-write
  "Handle one student datum"
  [conn {:keys [classroom-id] :as datum}]
  (let [[tag prefix]  (class-id->prefix classroom-id)
        tx (map->nsmap datum "user")
        new-eid (db/get-max-eid conn)
        serial (str prefix (pprint/cl-format nil "~8,'0d" (inc new-eid)))
        tx* (assoc tx :user/serial serial
                   :user/classroom-type tag)
        tx** (compact tx*)
        results (db/query-eids conn datum)]
    (if (empty? results)
      (d/transact! conn [tx**])
      (duplicated-warn datum))))

;; API
(defn import-from-db!
  [db-path]
  (let [conn (db/init! db-path)
        write! (partial check-and-write conn)
        students (postgre/get-all-students)
        _ (prn "Get all the students: " (count students))
        data (students->data students)]
    (dorun
     (map write! data))))
