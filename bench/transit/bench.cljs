(ns transit.test
  (:require [transit.core :as t]
            [cljs.reader :as r]))

(enable-console-print!)

(def fs (js/require "fs"))
(def json (. fs (readFileSync "../transit/seattle-data0.tjs" "utf8")))
(def r (t/reader :json))
(def w (t/writer :json))

(println "100 iters, transit read seattle file")
(println (. r (read json)))
(time
  (dotimes [_ 100]
    (.read r json)))

(println "100 iters, JSON.parse seattle file")
(time
  (dotimes [_ 100]
    (.parse js/JSON json)))

(println "100 iters, transit write seattle file")
(let [seattle (. r (read json))]
  (time
    (dotimes [_ 100] 
      (.write w seattle))))

(println "10 iters, cljs.reader/read-string seattle file")
(def edn (pr-str (.read r json)))
(time
  (dotimes [_ 10]
    (r/read-string edn)))
