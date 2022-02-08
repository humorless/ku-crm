(ns replware.ku-crm.core
  (:require [replware.ku-crm.db :as db]
            [replware.ku-crm.txor :as txor]
            [replware.ku-crm.dsync :as dsync])
  (:gen-class))

(defn -main
  "java -jar target/ku-crm.jar $CMD $DB_FILE_PATH

  Example:
   java -jar target/ku-crm.jar :import ./resources/datalevin/db
   java -jar target/ku-crm.jar :sync ./resources/datalevin/db  "
  [& args]
  (let [cmd (first args)
        dtlv-path (second args)]
    (cond
      (= cmd ":import") (txor/import-from-db! dtlv-path)
      (= cmd ":sync") (dsync/sync->ops-student-table! dtlv-path))))

;;TODO
;; - provide some options:
;;   1. import-from-db
;;   2. sync-to-db
;;   3. CRUD
