(ns cljblog.admin)

(def admin-login (or (System/getenv "ADMIN_LOGIN") "admin"))
(def admin-password (or (System/getenv "ADMIN_PASSWORD") "admin"))

(defn check-login [login password]
  (and (= login admin-login)
       (= password admin-password)))