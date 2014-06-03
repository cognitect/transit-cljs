(ns transit.test
  (:require [transit.core :as t]
            [cljs.reader :as r]))

(enable-console-print!)

(def fs (js/require "fs"))
(def rjson (. fs (readFileSync "../transit/seattle-data0.json" "utf8")))
(def json (. fs (readFileSync "../transit/seattle-data0.tjsm" "utf8")))
(def r (t/reader :json))
(def w (t/writer :json))
(def w-nc (t/writer :json {:cache false}))

(println "100 iters, JSON.parse seattle data")
(time
  (dotimes [_ 100]
    (.parse js/JSON rjson)))

(println "100 iters, transit read seattle data")
(time
  (dotimes [_ 100]
    (.read r json)))

(println "100 iters, transit write seattle data")
(let [seattle (.read r json)]
  (time
    (dotimes [_ 100] 
      (.write w seattle))))

(println "100 iters, transit write seattle data no-cache")
(let [seattle (.read r json)]
  (time
    (dotimes [_ 100] 
      (.write w-nc seattle))))

(println "100 iters, JSON.stringify seattle data")
(let [seattle (.parse js/JSON rjson)]
  (time
    (dotimes [_ 100] 
      (.stringify js/JSON seattle))))
