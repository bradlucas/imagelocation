(defproject imagelocation "1.0"
  :description "Provides a web interface and command line interface to process image files and extract their GPS information."
  :url "http://imagelocation.beaconhill.com/"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]

                 [compojure "1.6.1"]
                 [ring/ring-defaults "0.3.2"]
                 
                 [ring/ring-jetty-adapter "1.7.1"]
                 [org.clojure/tools.cli "0.4.2"]

                 [com.drewnoakes/metadata-extractor "2.12.0"]

                 [selmer "1.12.12"]
                 ]

  :repl-options {:init-ns imagelocation.core}

  :plugins [[lein-ring "0.12.5"]]
  :ring {:handler imagelocation.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.2"]]}}

  :main imagelocation.core
  )






