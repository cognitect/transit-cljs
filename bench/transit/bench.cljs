(ns transit.test
  (:require [transit.core :as t]
            [cljs.reader :as r]))

(enable-console-print!)

(def fs (js/require "fs"))
(def json (. fs (readFileSync "../transit/seattle-data0.tjs" "utf8")))
(def r (t/reader :json))
(def w (t/writer :json))

(println "100 iters, transit read seattle data")
(println (.read r json))
(time
  (dotimes [_ 100]
    (.read r json)))

(println "100 iters, JSON.parse seattle data")
(time
  (dotimes [_ 100]
    (.parse js/JSON json)))

(println "100 iters, transit write seattle data")
(let [seattle (.read r json)]
  (time
    (dotimes [_ 100] 
      (.write w seattle))))

(println "100 iters, JSON.stringify seattle data")
(let [seattle (.parse js/JSON json)]
  (time
    (dotimes [_ 100] 
      (.stringify js/JSON seattle))))

(println "10 iters, cljs.reader/read-string seattle data")
(def edn (pr-str (.read r json)))
(time
  (dotimes [_ 10]
    (r/read-string edn)))
