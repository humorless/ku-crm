#!/usr/bin/env bb
(ns modify 
  (:require [babashka.pods :as pods]
            [clojure.pprint :as  pprint]))

(pods/load-pod 'huahaiy/datalevin "0.5.28")
(require '[pod.huahaiy.datalevin :as d])

(def db-path "/tmp/datalevin/txor")

(def conn
  (d/get-conn db-path))

(defn get-max-eid [conn]
 (last 
  (sort
    (map (fn [[entity]]
       (:db/id entity)) 
    (d/q
     '[:find (pull ?e [:db/id]) 
       :where [?e :student/serial ?s]]
     (d/db conn))))))

(prn
  "show current max eid: "
  (get-max-eid conn))

;; change here
(def eid (d/entid (d/db conn) [:student/serial "fc00000054"]))

(prn "The eid to be changed is:" eid)

(pprint/pprint
 (d/transact! conn
   [[:db/add eid :student/student_name "AABBCC"]]))


(d/close conn)
