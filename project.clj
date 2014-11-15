(defproject com.cognitect/transit-cljs "0.8.188"
  :description "transit-js bindings for ClojureScript"
  :url "http://github.com/cognitect/transit-cljs"

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2371" :scope "provided"]
                 [com.cognitect/transit-js "0.8.746"]]

  :plugins [[lein-cljsbuild "1.0.4-SNAPSHOT"]
            [codox "0.8.9"]]

  :scm {:connection "scm:git:git@github.com:cognitect/transit-cljs.git"
        :developerConnection "scm:git:git@github.com:cognitect/transit-cljs.git"
        :url "git@github.com:cognitect/transit-cljs.git"}
  :pom-addition [:developers [:developer
                              [:name "David Nolen"]
                              [:email "david.nolen@cognitect.com"]
                              [:organization "Cognitect"]
                              [:organizationUrl "http://cognitect.com"]]]
  :license {:name "The Apache Software License, Version 2.0"
            :url "http://www.apache.org/licenses/LICENSE-2.0.txt"
            :distribution :repo}

  :source-paths ["src"]

  :cljsbuild { 
    :builds [{:id "dev"
              :source-paths ["src"]
              :compiler {
                :output-to "transit.dev.js"
                :output-dir "target/out-dev"
                :optimizations :none
                :source-map true}}
             {:id "test-dev"
              :source-paths ["src" "test/transit/test"] 
              :compiler {
                :output-to "target/transit.test.js"
                :output-dir "target/out-dev-test"
                :optimizations :none
                :source-map true}}
             {:id "test"
              :source-paths ["src" "test/transit/test"] 
              :compiler {
                :output-to "target/transit.test.js"
                :output-dir "target/out-test"
                :optimizations :advanced
                :pretty-print false}}
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
                :pretty-print false
                :externs ["resources/node_externs.js"]}}
             {:id "adv"
              :source-paths ["src"]
              :compiler {
                :output-to "target/transit.adv.js"
                :output-dir "target/out-adv"
                :optimizations :advanced
                :pretty-print false}}]}

  :codox {:language :clojurescript
          :output-dir "doc"
          :src-dir-uri "http://github.com/cognitect/transit-cljs/blob/master/"
          :src-linenum-anchor-prefix "L"}
)
