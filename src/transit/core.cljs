(ns transit.core
  (:require [com.cognitect.transit :as t]))

(defn obj-merge [a b]
  (doseq [k (js-keys b)]
    (when-let [v (aget b k)]
      (aset a k v)))
  a)

(defn reader
  ([type] (reader type nil))
  ([type options]
     (t/reader (name type)
       (obj-merge
         #js {:decoders
              #js {"$" (fn [v] (symbol v))
                   ":" (fn [v] (keyword v))
                   "set" (fn [v] (into #{} v))
                   "list" (fn [v] (into () (.reverse v)))}
              :defaultMapBuilder
              #js {:init (fn [] (transient {}))
                   :add (fn [m k v] (assoc! m k v))
                   :finalize (fn [m] (persistent! m))}}
         (clj->js options)))))

(println (reader :json ))
