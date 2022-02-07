(ns repl-sessions.import
  (:require [datalevin.core :as d]
         ;; [replware.ku-crm.dsync :as dsync]
            [replware.ku-crm.db :as db]
            [replware.ku-crm.txor :as txor]
            [replware.ku-crm.sql :as sql]))

(def db-path "/tmp/datalevin/my-db-txor")

;; show the data just get from postgre
(def data (sql/get-all-students))
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

;; After import: go to repl-sessions.import namespace to continue testing
