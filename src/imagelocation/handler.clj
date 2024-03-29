(ns imagelocation.handler
  (:require [clojure.pprint :as pp]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [imagelocation.image :as image]
            [ring.middleware.anti-forgery :refer [*anti-forgery-token*]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [ring.util.response :as response]
            [selmer.parser :as parser]))

(parser/add-tag! :csrf-field (fn [_ _] (anti-forgery-field)))

(defn index [req]
  (parser/render-file "templates/index.html"  {:csrf-token *anti-forgery-token*}))

(defn upload [req]
  (let [params (:params req)
        file (:file params)]
    (if (= (:size file) 0)
      ;; redirect to original page
      (response/redirect "/")
      (let [tempfile (.getPath (:tempfile file))]
        (if tempfile
          (let [result (image/process tempfile)]
            (parser/render-file "templates/result.html" {:result result}))
          ("Error: No file uploaded"))))))

(defn about [req]
  (let [result {:gps-latitude 40.71117777777778
                :gps-longitude -74.01142222222222
                :link "https://www.google.com/maps/search/?api=1&query=40.71117777777778,-74.01142222222222"}]
    (parser/render-file "templates/about.html" {:result result})))

(defroutes app-routes
  (GET "/" [] index)
  (POST "/" [] upload)

  (GET "/about" [] about)

  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))

