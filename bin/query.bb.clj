#!/usr/bin/env bb
(ns query 
  (:require [babashka.pods :as pods]
            [clojure.pprint :refer [pprint]]))

(pods/load-pod 'huahaiy/datalevin "0.5.14")
(require '[pod.huahaiy.datalevin :as d])

(def db-path "/tmp/datalevin/txor")
(def student-name "AABBCC")

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

(pprint
  (d/q
  '[:find (pull ?e [*]) 
    :in $ ?n
    :where [?e :student/student_name ?n]]
  (d/db conn) student-name))

(d/close conn)
