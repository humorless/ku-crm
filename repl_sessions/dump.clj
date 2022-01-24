(ns repl-sessions.dump
  (:require [replware.ku-crm.dump :as dump]
            [datalevin.core :as d]
            [replware.ku-crm.db :as db]
            [clojure.data.csv :as csv]))

(def path "/Users/laurence/kumon/ku-crm/fake_student.tsv")
(def db-path "/tmp/datalevin/my-db-txor")

(def conn (db/init! db-path))

(db/get-max-eid conn)
(dump/pull-users conn)
