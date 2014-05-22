(ns transit.roundtrip.core
  (:require [transit.core :as t]))

(def transport (or (aget (.-argv js/process) 2) :json))
(def r (t/reader transport))
(def w (t/writer transport))

(.on (.-stdin js/process) "data"
  (fn [data err]
    (.write (.-stdout js/process) (.write w (.read r data)))))
