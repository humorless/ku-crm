(ns replware.ku-crm.core
  (:require [replware.ku-crm.db :as db]
            [replware.ku-crm.txor :as txor]
            [replware.ku-crm.dsync :as dsync])
  (:gen-class))

(defn -main
  "java -jar ku-crm-1.0.0-standalone.jar $CMD $DB_FILE_PATH"
  [& args]
  (let [cmd (first args)
        dtlv-path (second args)]
    (cond
      (= cmd "import") (txor/import-from-db! dtlv-path)
      (= cmd "sync") (dsync/->ops-students-table))))

;;TODO
;; - provide some options:
;;   1. import-from-db
;;   2. sync-to-db
;;   3. CRUD
