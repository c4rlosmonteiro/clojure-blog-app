(ns cljblog.handler
  (:require [cljblog.admin :as adm]
            [cljblog.db :as db]
            [cljblog.pages :as pg]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
            [ring.middleware.session :as session]
            [ring.util.response :as resp]))

(defroutes app-routes
  (GET "/" [] (pg/index (db/list-articles)))
  (GET "/articles/:article-id" [article-id] (pg/article (db/find-article article-id)))

  (GET "/admin/login" [:as {session :session}]
    (if (:admin session)
      (resp/redirect "/")
      (pg/admin-login)))

  (GET  "/admin/logout" []
    (-> (resp/redirect "/")
        (assoc-in [:session :admin] false)))

  (POST "/admin/login" [login password]
    (if (adm/check-login login password)
      (-> (resp/redirect "/")
          (assoc-in [:session :admin] true))
      (pg/admin-login)))
  (route/not-found "Not Found"))

(defroutes admin-routes
  (GET "/articles/new" [] (pg/edit-article nil))
  (POST "/articles" [title body]
   (do (db/create-article title body)
       (resp/redirect "/")))

   (GET "/articles/:article-id/edit" [article-id] (pg/edit-article (db/find-article article-id)))
   (POST "/articles/:article-id" [article-id title body]
     (do
       (pg/edit-article (db/update-article article-id title body))
       (resp/redirect (str "/articles/" article-id)))))

(defn wrap-admin-only [handler]
  (fn [request]
    (if (-> request :session :admin)
      (handler request)
      (resp/redirect "/admin/login"))))

(def app
  (-> (routes (wrap-routes admin-routes wrap-admin-only) app-routes)
      (wrap-defaults site-defaults)
      session/wrap-session))
