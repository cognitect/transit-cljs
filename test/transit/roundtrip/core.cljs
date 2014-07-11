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

(ns transit.roundtrip.core
  (:require [com.cognitect.transit-cljs :as t]))

(def transport (or (aget (.-argv js/process) 2) :json))
(def r (t/reader transport))
(def w (t/writer transport))

(.on (.-stdin js/process) "data"
  (fn [data err]
    (.write (.-stdout js/process) (.write w (.read r data)))))
