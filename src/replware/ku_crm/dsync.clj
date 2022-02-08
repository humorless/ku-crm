(ns replware.ku-crm.dsync
  (:require [datalevin.core :as d]
            [replware.ku-crm.db :as db]
            [replware.ku-crm.sql :as sql]))

(defn pull-students [conn]
  (d/q '[:find (pull ?e [*])
         :in $
         :where
         [?e :student/serial]]
       (d/db conn)))

(defn ops-student [[v]]
  (-> v
      (dissoc :db/id)
      (update :student/classroom-type str)))

;; API
(defn sync->ops-student-table! [db-path]
  (let [conn (db/init! db-path)
        students (pull-students conn)
        data-segments (partition 1000 1000 []
                       (map ops-student students))]
    ;; (prn "data-segments is:" data-segments)
    (dorun
     (map #(sql/create-student! %) data-segments))))
