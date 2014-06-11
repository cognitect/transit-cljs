(defproject transit-cljs "0.1.0-SNAPSHOT"
  :description "transit-js bindings for ClojureScript"
  :url "http://github.com/cognitect/transit-cljs"

  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-2227"]
                 [com.cognitect/transit-js "0.1.316"]]

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
              :source-paths ["src" "test/transit/test"] 
              :compiler {
                :output-to "target/transit.test.js"
                :output-dir "target/out-test"
                :optimizations :advanced}}
             {:id "integer-test"
              :source-paths ["src" "test/transit/integer"] 
              :compiler {
                :output-to "target/transit.integer.test.js"
                :output-dir "target/out-integer-test"
                :optimizations :none
                :source-map true}}
             {:id "roundtrip"
              :source-paths ["src" "test/transit/roundtrip"] 
              :compiler {
                :output-to "target/roundtrip.js"
                :output-dir "target/out-roundtrip"
                :optimizations :advanced
                :externs ["resources/node_externs.js"]}}
             {:id "bench"
              :source-paths ["src" "bench"] 
              :compiler {
                :output-to "target/transit.bench.js"
                :output-dir "target/out-bench"
                :optimizations :advanced
                :externs ["resources/node_externs.js"]}}
             {:id "adv"
              :source-paths ["src"]
              :compiler {
                :output-to "target/transit.adv.js"
                :output-dir "target/out-adv"
                :optimizations :advanced
                :pretty-print false}}]})
