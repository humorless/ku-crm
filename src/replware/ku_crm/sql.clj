(ns replware.ku-crm.sql
  "The namespace for connecting postgre

  PostgreSQL does not seem to perform a conversion from `java.util.Date` to a SQL
  data type automatically. I need to require the next.jdbc.date-time namespace to
  enable that conversion."
  (:require [honey.sql :as sql]
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

(comment
  (get-all-students)
  (create-student! {:id  ""
                    :name ""
                    :birth ""}))
