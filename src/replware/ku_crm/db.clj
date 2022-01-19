(ns replare.ku-crm.db
  (:require [datalevin.core :as d]))

(def schema {:system/serial {:db/valueType :db.type/long
                         :db/cardinality :db.cardinality/one}
             :user/serial {:db/valueType :db.type/long
                        :db/unique    :db.unique/identity
                        :db/cardinality :db.cardinality/one}
             ;; :db/valueType is optional, if unspecified, the attribute will be
             ;; treated as EDN blobs, and may not be optimal for range queries
             :user/name {:db/valueType :db.type/string
                         :db/cardinality :db.cardinality/one}
             :user/birth {:db/valueType :db.type/instant
                          :db/cardinality :db.cardinality/one}
             :user/telephone {:db/valueType :db.type/string
                              :db/cardinality :db.cardinality/one}
             :user/classroom-id {:db/valueType :db.type/string
                                 :db/cardinality :db.cardinality/one}
             :user/classroom-type {:db/valueType :db.type/keyword
                                   :db/cardinality :db.cardinality/one}
             :user/old-id {:db/valueType :db.type/string
                           :db/cardinality :db.cardinality/one}})

;; Create DB on disk and connect to it, assume write permission to create given dir
(def conn (d/get-conn "/tmp/datalevin/my-db" schema))
;; or if you have a Datalevin server running on myhost with default port 8898
;; (def conn (d/get-conn "dtlv://myname:mypasswd@myhost/mydb" schema))

;; Transact some data
;; Notice that :nation is not defined in schema, so it will be treated as an EDN blob
(d/transact! conn
             [{:user/name "De Morgan", :user/serial 1, :user/birth #inst "2007-01-01T12:00:00+02:00" :user/old-id "13"
               :user/telephone "12345", :user/classroom-id "abcde" :user/classroom-type :classroom/ghost}
              {:user/name "May", :user/serial 2, :user/birth #inst "2007-01-01T12:00:00+02:00" :user/old-id "13"
               :user/telephone "6666", :user/classroom-id "xyz" :user/classroom-type :classroom/ac}
              {:user/name "John", :user/serial 3, :user/birth #inst "2008-01-01T12:00:00+02:00" :user/old-id "13"
               :user/telephone "12345", :user/classroom-id "abcde" :user/classroom-type :classroom/franchise}])

;; Query the data
(d/q '[:find ?serial
       :in $ ?name ?class
       :where
       [?e :user/serial ?serial]
       [?e :user/name ?name]
       [?e :user/classroom-id ?class]]
     (d/db conn)
     "May" "xyz")
;; => #{["France"]}

;; Retract the name attribute of an entity
;; (d/transact! conn [[:db/retract 1 :name "Frege"]])

;; Pull the entity, now the name is gone
(d/q '[:find (pull ?e [*])
       :in $ ?name
       :where
       [?e :user/name ?name]]
     (d/db conn)
     "John")
;; => ([{:db/id 1, :aka ["foo" "fred"], :nation "France"}])

;; Close DB connection
(d/close conn)
