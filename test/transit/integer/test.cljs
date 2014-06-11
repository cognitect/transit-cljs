(ns transit.integer.test
  (:require [com.cognitect.transit :as t])
  (:import [goog.math Long]))

(enable-console-print!)

(def r (t/reader "json"
         #js {:decoders
              #js {"i" (fn [v] (. Long (fromString v)))}}))

(let [arr (.read r "[\"~i1\"]")]
  (assert (= (.toString (.shiftLeft (aget arr 0) 16)) "65536")))

(println "ok")
