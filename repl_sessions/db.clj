(ns repl-sessions.db
  (:require [replware.ku-crm.db :as db]
            [datalevin.core :as d]))

(def conn 
  (db/init! "/tmp/datalevin/my-db-1"))

(db/get-max-eid conn)

(d/transact! conn
             [{:user/name "De Morgan", :user/serial "ac00000001", :user/birth #inst "2007-01-01T12:00:00+02:00" :user/old-id "13"
               :user/telephone "12345", :user/mobile "0099" :user/classroom-id "abcde" :user/classroom-type :classroom/ghost}
              {:user/name "May", :user/serial "qa00000002", :user/birth #inst "2007-01-01T12:00:00+02:00" :user/old-id "13"
               :user/telephone "6666", :user/classroom-id "xyz" :user/classroom-type :classroom/ac}
              {:user/name "John", :user/serial "gh00000003", :user/birth #inst "2008-01-01T12:00:00+02:00" :user/old-id "13"
               :user/telephone "12345", :user/classroom-id "abcde" :user/classroom-type :classroom/franchise}])

(db/query-attr conn :user/name "De Morgan")

(db/query-eids conn {:name "De Morgan"})
(db/query-eids conn {:name "De Morgan" :telephone "12345"})
(db/query-eids conn {:name "De Morgan" :telephone "12345" :mobile "0099"})
(db/query-eids conn {:name "De Morgan" :telephone "12345" :mobile "009"})
