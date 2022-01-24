(ns replware.ku-crm.postgre
  (:require [conman.core :as conman]))

(defn from-db [postgre-url]
  (let [db-entry {:jdbc-url postgre-url}
        db-conn (conman/connect! db-entry)]
    (conman/bind-connection db-conn "sql/queries.sql")
    (get-all-students)))

(comment
  (def db-entry {:jdbc-url "postgresql://localhost/kuops_dev?user=laurence"})
  (def ^:dynamic *db* (conman/connect! db-entry))
  (conman/bind-connection *db* "sql/queries.sql")
  (get-all-students))
