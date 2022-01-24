(ns replware.ku-crm.db
  (:require [datalevin.core :as d]))

(def schema {:user/serial {:db/valueType :db.type/string
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
             :user/mobile {:db/valueType :db.type/string
                           :db/cardinality :db.cardinality/one}
             :user/classroom-id {:db/valueType :db.type/string
                                 :db/cardinality :db.cardinality/one}
             :user/classroom-type {:db/valueType :db.type/keyword
                                   :db/cardinality :db.cardinality/one}
             :user/old-id {:db/valueType :db.type/string
                           :db/cardinality :db.cardinality/one}})

;; API
(defn init!
  "return the database connection"
  [path]
  (d/get-conn path schema))

(defn get-max-eid
  [conn]
  (:max-eid (d/db conn)))

(defn query-attr
  "Example k v are :user/name \"May\" "
  [conn k v]
  (if (some? v)
    [:yes (set
           (d/q '[:find [?e ...]
                  :in $ ?attr ?v
                  :where
                  [?e ?attr ?v]]
                (d/db conn)
                k v))]
    [:no nil]))

(defn query-eids
  "query-eids return the eids set by input args as
   name or birth or telephone ... "
  [conn {:keys [name birth telephone mobile classroom-id old-id]}]
  (let [name-v (query-attr conn :user/name name)
        birth-v (query-attr conn :user/birth birth)
        tele-v (query-attr conn :user/telephone telephone)
        mobile-v (query-attr conn :user/mobile mobile)
        class-v (query-attr conn :user/classroom-id classroom-id)
        old-v (query-attr conn :user/old-id old-id)
        data [name-v birth-v tele-v mobile-v class-v old-v]
        ;; _ (prn data)
        data* (map (fn m [[_ s]] s)
                   (filter (fn f [[tag _]]
                             (= tag :yes)) data))]
    (apply clojure.set/intersection data*)))
