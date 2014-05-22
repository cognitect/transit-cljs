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
(assert (= (. w (write (take 3 (repeat true)))) "{\"~#list\":[true,true,true]}"))
(assert (= (. w (write #js [1 2 3])) "[1,2,3]"))
(assert (= (. w (write #js {"foo" "bar"})) "{\"foo\":\"bar\"}"))

(println "testing basic transit read")
(assert (= (. r (read "{\"~#'\":1}")) 1))
(assert (= (. r (read "{\"~:foo\":\"bar\"}")) {:foo "bar"}))
(assert (= (. r (read "[1,2,3]")) [1 2 3]))
(assert (= (. r (read "{\"~#set\":[1,2,3]}")) #{1 2 3}))
(assert (= (. r (read "{\"~#list\":[1,2,3]}")) '(1 2 3)))
(assert (= (.valueOf (. r (read "{\"~#'\":\"~t2014-05-07T14:02:01.791Z\"}")))
           (.valueOf (js/Date. 1399471321791))))

(defn roundtrip [s]
  (. w (write (. r (read s)))))

(println "testing round tripping")
(assert (= (roundtrip "[\"~:foo\",\"~:bar\",{\"^\\\"\":[1,2]}]")
           "[\"~:foo\",\"~:bar\",{\"^\\\"\":[1,2]}]"))
(assert (= (roundtrip "{\"~#point\":[1,2]}")
           "{\"~#point\":[1,2]}"))
(assert (= (roundtrip "{\"foo\":\"~xfoo\"}")
           "{\"foo\":\"~xfoo\"}"))
(assert (= (roundtrip "{\"~/t\":null}")
          "{\"~/t\":null}"))
(assert (= (roundtrip "{\"~/f\":null}")
           "{\"~/f\":null}"))
(assert (= (roundtrip "{\"~#'\":\"~f-1.1E-1\"}")
           "{\"~#'\":\"~f-1.1E-1\"}"))

(println "ok")
