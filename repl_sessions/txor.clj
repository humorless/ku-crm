(ns repl-sessions.txor
  (:require [replware.ku-crm.txor :as txor]))

(def path "/Users/laurence/kumon/ku-crm/fake_student.tsv")
(def db-path "/tmp/datalevin/my-db-txor")

(def data (txor/from-csv path))

;; show the hashmap style data
(txor/students->data data)

;; import
(txor/import-from-csv! path db-path)
