(ns replware.ku-crm.dsync
  (:require [datalevin.core :as d]
            [replware.ku-crm.db :as db]
            [replware.ku-crm.postgre :as postgre]))

(defn pull-users [conn]
  (d/q '[:find (pull ?e [*])
         :in $
         :where
         [?e :user/name]]
       (d/db conn)))

(defn ops-student [[v]]
  (let [{:user/keys [serial name birth telephone
                     mobile classroom-id classroom-type
                     old-id]} v]
    {:id serial :name name
     :birth birth :telephone telephone
     :mobile mobile :classroom-id classroom-id
     :classroom-type (str classroom-type)
     :old-id old-id}))

(defn ->ops-students-table [db-path]
  (let [conn (db/init! db-path)
        users (pull-users conn)
        data (map ops-student users)]
    (dorun
     (map #(postgre/create-student! %) data))))
