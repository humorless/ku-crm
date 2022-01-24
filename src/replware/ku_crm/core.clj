(ns replware.ku-crm.core
  (:require [replware.ku-crm.db :as db]
            [replware.ku-crm.txor :as txor]
            [replware.ku-crm.dump :as dump])
  (:gen-class))

(defn -main
  "java -jar ku-crm-1.0.0-standalone.jar $DB_FILE_PATH"
  [& args]
  (let [dtlv-path (first args)]
    (txor/import-from-db! dtlv-path)))
