(defproject transit-cljs "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"

  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-2226"]
                 [com.cognitect/transit-js "0.1.262"]]

  :plugins [[lein-cljsbuild "1.0.4-SNAPSHOT"]]

  :source-paths ["src"]

  :cljsbuild { 
    :builds [{:id "dev"
              :source-paths ["src"]
              :compiler {
                :output-to "transit.dev.js"
                :output-dir "out-dev"
                :optimizations :none
                :source-map true}}
             {:id "release"
              :source-paths ["src"]
              :compiler {
                :output-to "transit.adv.js"
                :output-dir "out-adv"
                :optimizations :advanced
                :pretty-print false}}]})
