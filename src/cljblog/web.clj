(ns cljblog.web
  (:require [ring.adapter.jetty :as jetty]
            [compojure.handler :as ch]
            [cljblog.handler :as blog]))

(defn -main [& args]
  (let [port (Integer. (or (System/getenv "APP_PORT") 3000))]
    (jetty/run-jetty (ch/site #'blog/app) {:port port :join? false})))

