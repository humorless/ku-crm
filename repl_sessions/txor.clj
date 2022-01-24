(ns repl-sessions.txor
  (:require [datalevin.core :as d]
            [replware.ku-crm.dump :as dump]
            [replware.ku-crm.db :as db]
            [replware.ku-crm.txor :as txor]
            [replware.ku-crm.postgre :as postgre]))

(def db-path "/tmp/datalevin/my-db-txor")

;; show the data just get from postgre
(def data (postgre/get-all-students))
;; show the rows
(count data)
;; show the hashmap style data
(txor/students->data data)
;; before import: show eid
(d/with-conn [conn db-path]
  (db/get-max-eid conn))

;; import
(txor/import-from-db! db-path)
;; after import: show eid
(d/with-conn [conn db-path]
  (db/get-max-eid conn))

;; after import: show content
(def conn (db/init! db-path))
(dump/pull-users conn)
