(ns replware.ku-crm.postgre
  "The namespace for connecting postgre

  PostgreSQL does not seem to perform a conversion from `java.util.Date` to a SQL
  data type automatically. I need to require the next.jdbc.date-time namespace to
  enable that conversion."
  (:require [conman.core :as conman]
            [next.jdbc.date-time]
            [clojure.java.io :as io]
            [aero.core :as aero]))

(def config (aero/read-config
              (io/resource "config.edn")))

(def db-entry {:jdbc-url
               (:database-url config)})

(def ^:dynamic *db* (conman/connect! db-entry))

(conman/bind-connection *db* "sql/queries.sql")

(comment
  (get-all-students)
  (create-student! {:id  ""
                    :name ""
                    :birth ""})
  )
