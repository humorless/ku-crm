(ns replware.ku-crm.sql
  "The namespace for connecting postgre

  PostgreSQL does not seem to perform a conversion from `java.util.Date` to a SQL
  data type automatically. I need to require the next.jdbc.date-time namespace to
  enable that conversion."
  (:require [honey.sql :as sql]
            [honey.sql.helpers :as hh]
            [next.jdbc :as jdbc]
            [next.jdbc.date-time]
            [clojure.java.io :as io]
            [aero.core :as aero]))

(def config (aero/read-config
             (io/resource "config.edn")))

(def db-entry {:jdbcUrl
               (:database-url config)})

(def conn (jdbc/get-datasource db-entry))

(def all-students-sqlmap
  {:select [:*]
   :from [:student]})

(defn get-all-students []
  (jdbc/execute! conn (sql/format all-students-sqlmap)))

(defn create-student!
  "Input argument is a list of hashmap `m`, so it denoted as `ms`.

   It does not matter if the hashmap `m` is using namespaced key or not.
   However, m has to have a `serial` key.
   If certain column does not exist in `m`, that column will be filled with NULL"
  [ms]
  (let [cmd (-> (hh/insert-into :ops_student)
                (hh/values (vec ms))
                (sql/format {:pretty true}))]
    (prn "the batch size is: " (count ms))
    (jdbc/execute! conn cmd)))

(comment
  (get-all-students)
  (create-student! {:serial "aaaab"})
  (create-student! {:student/serial "xy1xz"}))
