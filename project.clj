(defproject transit-cljs "0.1.0-SNAPSHOT"
  :description "transit-js bindings for ClojureScript"
  :url "http://github.com/cognitect/transit-cljs"

  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-2227"]
                 [com.cognitect/transit-js "0.1.267"]]

  :plugins [[lein-cljsbuild "1.0.4-SNAPSHOT"]]

  :source-paths ["src"]

  :cljsbuild { 
    :builds [{:id "dev"
              :source-paths ["src"]
              :compiler {
                :output-to "transit.dev.js"
                :output-dir "target/out-dev"
                :optimizations :none
                :source-map true}}
             {:id "test"
              :source-paths ["src" "test"] 
              :compiler {
                :output-to "target/transit.test.js"
                :output-dir "target/out-test"
                :optimizations :none
                :source-map true}}
             {:id "adv"
              :source-paths ["src"]
              :compiler {
                :output-to "target/transit.adv.js"
                :output-dir "target/out-adv"
                :optimizations :advanced
                :pretty-print false}}]})
