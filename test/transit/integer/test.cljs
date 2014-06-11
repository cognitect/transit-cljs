(ns transit.integer.test
  (:require [com.cognitect.transit :as t])
  (:import [goog.math Long]))

(enable-console-print!)

(def r (t/reader "json"
         #js {:decoders
              #js {"i" (fn [v] (. Long (fromString v)))}}))

(let [arr (.read r "[\"~i1\"]")]
  (assert (= (.toString (.shiftLeft (aget arr 0) 16)) "65536")))

(deftype IntegerHandler []
  Object
  (tag [_ v] "i")
  (rep [_ v] (.toString v))
  (stringRep [_ v] (.toString v)))

(def w (t/writer "json"
         #js {"handlers"
              #js [Long (IntegerHandler.)]}))

(let [arr (.read r "[\"~i1\"]")]
  (assert (= (.write w (.shiftLeft (aget arr 0) 16)) "{\"~#'\":\"~i65536\"}")))

(println "ok")
