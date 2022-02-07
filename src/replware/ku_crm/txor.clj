(ns replware.ku-crm.txor
  (:require [clojure.data.csv :as csv]
            [clojure.pprint :as pprint]
            [clojure.java.io :as io]
            [replware.ku-crm.db :as db]
            [replware.ku-crm.sql :as sql]
            [datalevin.core :as d])
  (:import java.util.Date)
  (:import java.text.SimpleDateFormat))

(defn str->inst [s]
  (when s
    (let [fmt (SimpleDateFormat. "yyyy-MM-dd")]
      (.parse fmt s))))


(defn students->data
  "data -> list of hashmap
  
   TODO: double check the source csv format
         NULL data should be nil value, not empty string"
  [rows]
  (map-indexed
   (fn [idx datum]
     (when (zero? (mod idx 100))
       (prn "processsing to the " (inc idx) "order of student data."))
     datum)
   rows))

(defn map->nsmap
  "The function can change the namespace of a hashmap

   Usage: (map->nsmap m  \"new-namespace\")"
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

(defn direct-write
  "Handle one student datum"
  [conn {:student/keys [center_symbol] :as datum}]
  (let [[tag prefix]  (class-id->prefix center_symbol)
        ;; tx (map->nsmap datum "user")
        new-eid (db/get-max-eid conn)
        serial (str prefix (pprint/cl-format nil "~8,'0d" (inc new-eid)))
        tx* (assoc datum :student/serial serial
                   :student/classroom-type tag)
        tx** (compact tx*)]
    (d/transact! conn [tx**])))

;; API
(defn import-from-db!
  [db-path]
  (let [conn (db/init! db-path)
        write! (partial direct-write conn)
        students (sql/get-all-students)
        _ (prn "Get all the students: " (count students))
        data (students->data students)
        data* (distinct data)]
    (dorun
     (map write! data*))))
