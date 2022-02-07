(ns replware.ku-crm.db
  (:require [datalevin.core :as d]))

(defn expand-entity
  "Provide a very concise notation for Datomic schema, for rapid application
  development."
  [[ident type ?doc & tags]]
  (let [[?doc tags] (if (string? ?doc)
                      [?doc (set tags)]
                      [nil (cons ?doc (set tags))])]
    (cond-> {:db/ident ident
             :db/valueType (keyword "db.type" (name type))
             :db/cardinality :db.cardinality/one}
      ?doc
      (assoc :db/doc ?doc)
      (:many tags)
      (assoc :db/cardinality :db.cardinality/many)
      (:unique tags)
      (assoc :db/unique :db.unique/value)
      (:identity tags)
      (assoc :db/unique :db.unique/identity))))

(def entity [[:user/serial :string "The new id of the student entity" :identity]
             [:user/classroom-type :keyword]
             ;; begin the schema inherit from old system
             [:user/center_symbol :string]
             [:user/registration_date :instant]
             [:user/ematter_student_symbol :string]
             [:user/student_name :string]
             [:user/gender :string]
             [:user/birth_date :instant]
             ;; learning section
             [:user/school_grade :string]
             [:user/graduation_year :long]
             ;; parent section
             [:user/parent_name :string]
             [:user/parent_gender :string]
             [:user/parent_birth_date :instant]
             ;; contact section
             [:user/email_address :string]
             [:user/mobile_phone_number :string]
             [:user/phone_number :string]
             [:user/postal_code :string]
             [:user/address :string]
             ;; other section
             [:user/hdyhau :string]
             [:user/hdyhau_other :string]
             [:user/note :string]])

(def schema
  (reduce (fn [coll {:db/keys [ident] :as m}]
            (assoc coll ident (dissoc m :db/ident))) {}
          (map expand-entity entity)))

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
