(ns imagelocation.core
  (:require [clojure.pprint :as pp]
            [clojure.string :as str]
            [clojure.tools.cli :refer [parse-opts]]
            [imagelocation.handler :as handler]
            [imagelocation.image :as image]
            [ring.adapter.jetty :as jetty])
  (:gen-class))


(def cli-options
  [["-w" "--web" "Start web app"]
   ["-h" "--help" "Show help usage"]])

(defn usage [summary]
  (->> ["usage: imagelocation.jar [options] [filename[s]]"
        "options:"
        summary
        ""
        "Examples:"
        "java -jar imagelocation.jar -w           ;; start web app"
        "java -jar imagelocation.jar photo.jpg    ;; process single file"
        "java -jar imagelocation.jar *.jpg        ;; process all jpg files"
        ""
        "Output from command line processing:"
        "filename,lat,lng,link"
        ""
        ]
       (str/join \newline)))

(defn process-file [filename]
  (let [r (image/process filename)]
    (format "%s,%s,%s,%s" filename (:lat r) (:lng r) (:link r))))

(defn process-files [filenames]
  (let [lst (map #(process-file %) filenames)]
    (doseq [l lst]
      (println l))))

(defn -main [& args]
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)]
    (if (> (count arguments) 0)
      (process-files arguments)
      (cond
        (:help options) (println (usage summary))
        :else (jetty/run-jetty handler/app {:port 4002})))))
