(ns repl-sessions.txor
  (:require [replware.ku-crm.txor :as txor]
            [replware.ku-crm.postgre :as postgre]))

(def postgre-url "postgresql://localhost/kuops_dev?user=laurence")
(def db-path "/tmp/datalevin/my-db-txor")

(def data (postgre/from-db postgre-url))

;; show the hashmap style data
(txor/students->data data)

;; import
(txor/import-from-db! postgre-url db-path)
