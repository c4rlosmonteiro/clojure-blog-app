(ns cljblog.db
  (:require [monger.core :as mg]
            [monger.collection :as mc]))

(def collection-name "articles")

(def db-connection-string (or (System/getenv "MONGO_CONNECTION_STRING")
                              "mongodb://127.0.0.1/cljblog-test"))

(def db (-> db-connection-string
            mg/connect-via-uri
            :db))

(defn create-article
      "This function will insert an article into the database"
      [title body]
      (mc/insert db collection-name
                 {:title title
                  :body body
                  :created (new java.util.Date)}))

(defn list-articles
      "This function will list all articles"
      []
      (mc/find-maps db collection-name))