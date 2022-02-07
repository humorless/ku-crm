(ns repl-sessions.sync
  (:require [replware.ku-crm.dsync :as dsync]
            [replware.ku-crm.db :as db]))

(def db-path "/tmp/datalevin/my-db-txor")

(def conn (db/init! db-path))

(def users (dsync/pull-students conn))

(map dsync/ops-student users)

(dsync/->ops-student-table db-path)

