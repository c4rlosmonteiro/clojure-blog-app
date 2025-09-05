(ns cljblog.db
  (:require [monger.core :as mg]
            [monger.operators :refer [$set]]
            [monger.collection :as mc])
  (:import (org.bson.types ObjectId)))

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

(defn update-article
  "This function will update an article into the database"
  [article-id title body]
  (mc/update-by-id db collection-name (ObjectId. article-id)
              {$set
               {:title title
                :body body}}))

(defn list-articles
      "This function will list all articles"
      []
      (mc/find-maps db collection-name))

(defn find-article
  "This function will find an article by its id"
  [article-id]
  (mc/find-map-by-id db collection-name (ObjectId. article-id)))

