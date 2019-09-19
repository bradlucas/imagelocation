(ns imagelocation.handler
  (:require [clojure.pprint :as pp]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [imagelocation.image :as image]
            [ring.middleware.anti-forgery :refer [*anti-forgery-token*]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.anti-forgery :refer [wrap-anti-forgery]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [ring.middleware.multipart-params :refer [wrap-multipart-params]]))



(defn index [req]
  (let [anti-forgery-field (anti-forgery-field)]
    (pp/pprint anti-forgery-field)
    (str "<html>"
         "<head></head>"
         "<body>"
         "<h4>Image Location</h4>" 
         "<form action='/' method='post' enctype='multipart/form-data'>" 
         anti-forgery-field
         "<input type='file' name='file' size='20'>"
         "<button type='submit' class='btn'>Submit</button>"
         "</form>"
         "</body>"
         "</html>")))



(defn upload [req]
  ;; @see https://github.com/dfuenzalida/compojure-file-upload/blob/master/src/fileupload/core.clj
  (pp/pprint req)
  (pp/pprint (:params req))

    (let [params (:params req)
          file (:file params)]
      (pp/pprint params)
      (pp/pprint file)
      (let [tempfile (.getPath (:tempfile file))]
        (if tempfile
          (do
            (pp/pprint tempfile)
            (with-out-str 
              (pp/pprint (image/process tempfile))))
          ("Error: No file uploaded")))))



(defroutes app-routes
  (GET "/" [] index)
  (POST "/" [] upload)

  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))


;; https://github.com/ring-clojure/ring/wiki/File-Uploads

;; (def app
;;   (-> app-routes
;;       wrap-anti-forgery
;;       wrap-params
;;       wrap-multipart-params))

