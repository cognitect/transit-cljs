;; Copyright 2014 Cognitect. All Rights Reserved.
;;
;; Licensed under the Apache License, Version 2.0 (the "License");
;; you may not use this file except in compliance with the License.
;; You may obtain a copy of the License at
;;
;;      http://www.apache.org/licenses/LICENSE-2.0
;;
;; Unless required by applicable law or agreed to in writing, software
;; distributed under the License is distributed on an "AS-IS" BASIS,
;; WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
;; See the License for the specific language governing permissions and
;; limitations under the License.

(ns transit.test
  (:require [cognitect.transit :as t]
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

(println "100 iters, JSON.stringify seattle data")
(let [seattle (.parse js/JSON rjson)]
  (time
    (dotimes [_ 100] 
      (.stringify js/JSON seattle))))

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

(println "100 iters, pr-str seattle data")
(let [seattle (pr-str (.read r json))]
  (time
    (dotimes [_ 100] 
      (r/read-string seattle))))

(println "100 iters, pr-str seattle data")
(let [seattle (.read r json)]
  (time
    (dotimes [_ 100] 
      (pr-str seattle))))


