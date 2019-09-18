(ns imagelocation.core
  (:require [clojure.pprint :as pp]
            [clojure.string :as str]
            [clojure.tools.cli :refer [parse-opts]]
            [imagelocation.handler :as handler]
            [ring.adapter.jetty :as jetty]))


(def cli-options
  [["-f" "--filename FILENAME" "Process FILENAME for location information"]
   ["-h" "--help" "Show help usage"]])

(defn usage [summary]
  (->> ["Image Location"
        ""
        summary
        ""]
       (str/join \newline)))

(defn process-file [filename]
  (pp/pprint (format "Process %s" filename)))


(defn -main [& args]
  ;; (config/load-config)
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)]
    (cond
      (:filename options) (process-file (:filename options))
      (:help options) (println (usage summary))
      :else (jetty/run-jetty handler/app {:port 4002}))))
