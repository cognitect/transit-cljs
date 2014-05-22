(ns transit.test
  (:require [transit.core :as t]))

(enable-console-print!)

(def r (t/reader :json))
(def w (t/writer :json))

(println "testing basic transit write")
(assert (= (. w (write 1)) "{\"~#'\":1}"))
(assert (= (. w (write (js/Date. 1399471321791))) "{\"~#'\":\"~t2014-05-07T14:02:01.791Z\"}"))
(assert (= (. w (write {:foo "bar"})) "{\"~:foo\":\"bar\"}"))
(assert (= (. w (write [1 2 3])) "[1,2,3]"))
(assert (= (. w (write #{1 2 3})) "{\"~#set\":[1,2,3]}"))
(assert (= (. w (write '(1 2 3))) "{\"~#list\":[1,2,3]}"))
(assert (= (. w (write (reverse [1 2 3]))) "{\"~#list\":[3,2,1]}"))
(assert (= (. w (write (range 3))) "{\"~#list\":[0,1,2]}"))

(println "ok")
