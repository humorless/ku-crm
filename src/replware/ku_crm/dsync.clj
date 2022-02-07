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
(defn ->ops-student-table [db-path]
  (let [conn (db/init! db-path)
        students (pull-students conn)
        data (map ops-student students)]
    (dorun
     (map #(sql/create-student! %) data))))
