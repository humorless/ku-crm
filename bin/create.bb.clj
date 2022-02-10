#!/usr/bin/env bb
(ns create 
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

(defn class-id->prefix [c-id]                                                                                                                       
    (cond                                                                                                                                             
      (= 5 (count c-id)) [:classroom/ac "ac"]                                                                                                         
      (> (count c-id) 2) [:classroom/franchise (subs c-id 0 2)]                                                                                       
      :else [:classroom/ghost "gh"])) 

(defn compact [record]                                                                                                                              
   (into {} (remove (comp nil? second) record)))                                                                                                     
                                                                                                                                                      
(defn direct-write                                                                                                                                  
   "Handle one student datum"                                                                                                                        
   [conn {:student/keys [center_symbol] :as datum}]                                                                                                  
   (let [[tag prefix]  (class-id->prefix center_symbol)                                                                                              
         ;; tx (map->nsmap datum "user")                                                                                                             
         new-eid (get-max-eid conn)                                                                                                               
         serial (str prefix (pprint/cl-format nil "~8,'0d" (inc new-eid)))                                                                           
         tx* (assoc datum :student/serial serial                                                                                                     
                    :student/classroom-type tag)                                                                                                     
         tx** (compact tx*)]                                                                                                                         
     (d/transact! conn [tx**]))) 

(prn
  "show current max eid: "
  (get-max-eid conn))

(pprint/pprint
 (direct-write conn
   {:student/student_name "AABBCC" :student/center_symbol "fc-10054-01"}))


(d/close conn)
