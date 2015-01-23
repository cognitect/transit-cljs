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

(ns transit.test.core
  (:require [cognitect.transit :as t]
            [cljs.test :as test :refer-macros [deftest is run-tests testing]]))

(enable-console-print!)

(def r (t/reader :json))
(def w (t/writer :json))

(deftest test-basic-write
  (testing "Testing basic Transit write"
    (is (= (t/write w 1) "[\"~#'\",1]"))
    (is (= (t/write w (js/Date. 1399471321791)) "[\"~#'\",\"~m1399471321791\"]"))
    (is (= (t/write w {:foo "bar"}) "[\"^ \",\"~:foo\",\"bar\"]"))
    (is (= (t/write w [1 2 3]) "[1,2,3]"))
    ;;(is (= (t/write w #{1 2 3}) "{\"~#set\":[1,2,3]}"))
    (is (= (t/write w '(1 2 3)) "[\"~#list\",[1,2,3]]"))
    (is (= (t/write w (reverse [1 2 3])) "[\"~#list\",[3,2,1]]"))
    (is (= (t/write w (range 3)) "[\"~#list\",[0,1,2]]"))
    (is (= (t/write w (take 3 (repeat true))) "[\"~#list\",[true,true,true]]"))
    (is (= (t/write w #js [1 2 3]) "[1,2,3]"))
    (is (= (t/write w #js {"foo" "bar"}) "[\"^ \",\"foo\",\"bar\"]"))))

(deftest test-basic-read
  (testing "Testing basic Transit read"
    (is (= (t/read r "{\"~#'\":1}") 1))
    (is (= (t/read r "{\"~:foo\":\"bar\"}") {:foo "bar"}))
    (is (= (t/read r "[1,2,3]") [1 2 3]))
    (is (= (t/read r "[\"~#set\",[1,2,3]]") #{1 2 3}))
    (is (= (t/read r "[\"~#list\",[1,2,3]]") '(1 2 3)))
    (is (= (.valueOf (t/read r "{\"~#'\":\"~t2014-05-07T14:02:01.791Z\"}"))
               (.valueOf (js/Date. 1399471321791))))))

(defn roundtrip [s]
  (t/write w (t/read r s)))

(deftest test-roundtrip
  (testing "Testing round tripping"
    (is (= (roundtrip "[\"~:foo\",\"~:bar\",[\"^ \",\"^1\",[1,2]]]")
              "[\"~:foo\",\"~:bar\",[\"^ \",\"^1\",[1,2]]]"))
    (is (= (roundtrip "[\"~#point\",[1,2]]")
              "[\"~#point\",[1,2]]"))
    (is (= (roundtrip "[\"^ \",\"foo\",\"~xfoo\"]")
              "[\"^ \",\"foo\",\"~xfoo\"]"))
    (is (= (roundtrip "[\"^ \",\"~/t\",null]")
              "[\"^ \",\"~/t\",null]"))
    (is (= (roundtrip "[\"^ \",\"~/f\",null]")
              "[\"^ \",\"~/f\",null]"))
    (is (= (roundtrip "{\"~#'\":\"~f-1.1E-1\"}")
              "[\"~#'\",\"~f-1.1E-1\"]"))
    (is (= (roundtrip "{\"~#'\":\"~f-1.10E-1\"}")
              "[\"~#'\",\"~f-1.10E-1\"]"))
    (is (= (roundtrip "[\"~#set\",[[\"~#ratio\",[\"~i4953778853208128465\",\"~i636801457410081246\"]],[\"^1\",[\"~i-8516423834113052903\",\"~i5889347882583416451\"]]]]")
              "[\"~#set\",[[\"~#ratio\",[\"~i4953778853208128465\",\"~i636801457410081246\"]],[\"^1\",[\"~i-8516423834113052903\",\"~i5889347882583416451\"]]]]"))
    (is (= (roundtrip "[[\"^ \",\"aaaa\",1,\"bbbb\",2],[\"^ \",\"^0\",3,\"^1\",4],[\"^ \",\"^0\",5,\"^1\",6]]")
              "[[\"^ \",\"aaaa\",1,\"bbbb\",2],[\"^ \",\"^0\",3,\"^1\",4],[\"^ \",\"^0\",5,\"^1\",6]]"))
    (is (= (roundtrip "{\"~#'\":\"~n8987676543234565432178765987645654323456554331234566789\"}")
              "[\"~#'\",\"~n8987676543234565432178765987645654323456554331234566789\"]"))
    (is (= (roundtrip "[\"~#list\",[0,1,2,true,false,\"five\",\"~:six\",\"~$seven\",\"~~eight\",null]]")
              "[\"~#list\",[0,1,2,true,false,\"five\",\"~:six\",\"~$seven\",\"~~eight\",null]]"))
    ;; (is (= (roundtrip "[\"^ \",\"~:key0000\",0,\"~:key0001\",1,\"~:key0002\",2,\"~:key0003\",3,\"~:key0004\",4,\"~:key0005\",5,\"~:key0006\",6,\"~:key0007\",7,\"~:key0008\",8,\"~:key0009\",9]")
    ;;                       "[\"^ \",\"~:key0000\",0,\"~:key0001\",1,\"~:key0002\",2,\"~:key0003\",3,\"~:key0004\",4,\"~:key0005\",5,\"~:key0006\",6,\"~:key0007\",7,\"~:key0008\",8,\"~:key0009\",9]"))
    ))

;; cmap
(def cmap
  (->> {[] 42}
    (t/write (t/writer :json))
    (t/read (t/reader :json))))

(deftest test-cmap
  (is (satisfies? cljs.core/IMap cmap))
  (is (= cmap {[] 42})))

(deftest test-constructor-and-predicates-api
  (testing "Testing constructor & predicates API"
    (let [p0 (t/read r "{\"~#point\":[1.5,2.5]}")
          p1 (t/read r "{\"~#point\":[1.5,2.5]}")
          m0 {p0 :foo}]
      (is (t/tagged-value? p0))
      (is (= p0 p1))
      (is (= (get m0 p0) :foo)))
    (let [uuid0 (t/read r "{\"~#'\":\"~u2f9e540c-0591-eff5-4e77-267b2cb3951f\"}")
          uuid1 (t/read r "{\"~#'\":\"~u2f9e540c-0591-eff5-4e77-267b2cb3951f\"}")
          m1    {uuid0 :bar}]
      (is (t/uuid? uuid0))
      (is (= uuid0 uuid1))
      (is (= (get m1 uuid0) :bar)))
    (let [l0 (t/read r "{\"~#'\":\"~i9007199254740993\"}")
          m2 {l0 :baz}]
      (is (t/integer? l0))
      (is (= (get m2 l0) :baz)))))

;; TCLJS-3
(deftest test-tcljs3
  (is (= (t/read (t/reader :json {:handlers {"custom" (fn [x] x)}}) "[\"~:foo\", 1]")
         [:foo 1])))

(defrecord Point [x y])

(deftype PointHandler []
  Object
  (tag [_ v] "point")
  (rep [_ v] #js [(.-x v) (.-y v)])
  (stringRep [_ v] nil))

(def cr
  (t/reader :json
    {:handlers
     {"custom" (fn [x] x)
      "point" (fn [[x y]] (Point. x y))}}))

(deftest test-read-custom-tags
  (is (= (t/read cr "[\"~#point\",[1.5,2.5]]")
         (Point. 1.5 2.5)))
  (is (= (t/read cr "[\"~:foo\", 1]")
         [:foo 1])))

(def cw
  (t/writer :json
    {:handlers
     {Point (PointHandler.)}}))

(deftest test-write-custom-tags
  (is (= (t/write cw (Point. 1.5 2.5))
         "[\"~#point\",[1.5,2.5]]")) )

;; CLJS UUID

(deftest test-uuid
  (is (= (t/read r "{\"~#'\":\"~u550e8400-e29b-41d4-a716-446655440000\"}")
         #uuid "550e8400-e29b-41d4-a716-446655440000"))
  (is (= #uuid "550e8400-e29b-41d4-a716-446655440000"
         (t/read r "{\"~#'\":\"~u550e8400-e29b-41d4-a716-446655440000\"}")))
  (is (= (t/write w #uuid "550e8400-e29b-41d4-a716-446655440000")
         "[\"~#'\",\"~u550e8400-e29b-41d4-a716-446655440000\"]")))

;; Transit UUID printing

(deftest test-uuid-print
  (is (= (pr-str (t/read r "{\"~#'\":\"~u550e8400-e29b-41d4-a716-446655440000\"}"))
         (pr-str #uuid "550e8400-e29b-41d4-a716-446655440000"))))

(defn -main [& args]
  (run-tests))

(set! *main-cli-fn* -main)
