(ns replware.ku-crm.postgre
  (:require [conman.core :as conman]))

(def db-entry {:jdbc-url "postgresql://localhost/kuops_dev?user=laurence"})
(def ^:dynamic *db* (conman/connect! db-entry))
(conman/bind-connection *db* "sql/queries.sql")

(comment
  (get-all-students))
