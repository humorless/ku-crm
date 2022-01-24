(ns replware.ku-crm.dump
  (:require [datalevin.core :as d]
            [replware.ku-crm.db :as db]
            [clojure.data.csv :as csv]))

(defn pull-users [conn]
  (d/q '[:find (pull ?e [*])
         :in $
         :where
         [?e :user/name]]
       (d/db conn)))


