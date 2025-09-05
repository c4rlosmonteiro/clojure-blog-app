(ns cljblog.pages
  (:require
    [hiccup.page :refer [html5]]
    [hiccup.form :as form]
    [ring.util.anti-forgery :refer [anti-forgery-field]]))

(defn base-page [& body]
  (html5
    [:head [:title "CljBlog"]]
    [:body
     [:h1 [:a {:href "/"} "CljBlog"]]
     [:a {:href "/articles/new"} "New article"]
     [:hr]
     body]))

(defn index [articles]
  (base-page
    (for [a articles]
      [:h2 [:a {:href (str "/articles/" (:_id a))}(:title a)]])))

(defn article [a]
  (base-page
    [:a {:href (str "/articles/" (:_id a) "/edit")} "Edit"]
    [:hr]
    [:small (:created a)]
    [:h1 (:title a)]
    [:p (:body a)]))

(defn edit-article [a]
  (base-page
    (form/form-to
      [:post (if a
               (str "/articles/" (:_id a))
               "/articles")]

      (form/label "title" "Title")
      (form/text-field "title" (:title a))

      (form/label "body" "Body")
      (form/text-area "body" (:body a))

      ;It is used by ring anti forgery middleware
      ;to prevent csrf attacks
      (anti-forgery-field)

      (form/submit-button "Save!")
      )))

(defn admin-login []
  (base-page
    (form/form-to
      [:post "/admin/login"]
      (form/label "login" "Login")
      (form/text-field "login")

      (form/label "password" "Password")
      (form/password-field "password")

      ;It is used by ring anti forgery middleware
      ;to prevent csrf attacks
      (anti-forgery-field)

      (form/submit-button "Login!")
      )))