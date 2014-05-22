;; Copyright (c) Cognitect, Inc.
;; All rights reserved.

(ns transit.core
  (:require [com.cognitect.transit :as t]))

(enable-console-print!)

(defn opts-merge [a b]
  (doseq [k (js-keys b)]
    (when-let [v (aget b k)]
      (if (not= k "handlers")
        (aset a k v)
        (aset a k (.concat (aget a k) (aget b k))))))
  a)

(defn reader
  ([type] (reader type nil))
  ([type opts]
     (t/reader (name type)
       (opts-merge
         #js {:decoders
              #js {"$" (fn [v] (symbol v))
                   ":" (fn [v] (keyword v))
                   "set" (fn [v] (into #{} v))
                   "list" (fn [v] (into () (.reverse v)))}
              :defaultMapBuilder
              #js {:init (fn [] (transient {}))
                   :add (fn [m k v] (assoc! m k v))
                   :finalize (fn [m] (persistent! m))}
              :prefersStrings false}
         (clj->js opts)))))

(defn keyword-handler []
  #js {:tag (fn [v] ":")
       :rep (fn [v] (.substring (str v) 1))
       :stringRep (fn [v] (.substring (str v) 1))})

(defn symbol-handler []
  #js {:tag (fn [v] "$")
       :rep (fn [v] (.substring (str v) 1))
       :stringRep (fn [v] (.substring (str v) 1))})

(defn list-handler []
  #js {:tag (fn [v] "list")
       :rep (fn [v]
              (let [ret #js []]
                (doseq [x v] (.push ret x))
                (t/tagged "array" ret)))})

(defn map-handler []
  #js {:tag (fn [v] "map")
       :rep (fn [v] v)
       :stringRep (fn [v] nil)})

(defn set-handler []
  #js {:tag (fn [v] "set")
       :rep (fn [v]
              (let [ret #js []]
                (doseq [x v] (.push ret x))
                (t/tagged "array" ret)))
       :stringRep (fn [v] nil)})

(defn vector-handler []
  #js {:tag (fn [v] "array")
       :rep (fn [v]
              (let [ret #js []]
                (doseq [x v] (.push ret x))
                ret))
       :stringRep (fn [v] nil)})

(deftype MapIterator [^{:mutable true :tag not-native} seq]
  Object
  (hasNext [_] (not (nil? seq)))
  (next [_]
    (let [me (-first seq)]
      (set! seq (-next seq))
      (.-tail me))))

(defn writer
  ([type] (writer type nil))
  ([type opts]
     (t/writer (name type)
       (opts-merge
         #js {:mapIterator
              (fn [m] (println "map iterator!") (MapIterator. (seq m)))
              :handlers
              #js [cljs.core/Keyword               (keyword-handler)
                   cljs.core/Symbol                (symbol-handler)
                   cljs.core/Range                 (list-handler)
                   cljs.core/List                  (list-handler)
                   cljs.core/Cons                  (list-handler)
                   cljs.core/EmptyList             (list-handler)
                   cljs.core/LazySeq               (list-handler)
                   cljs.core/RSeq                  (list-handler)
                   cljs.core/IndexedSeq            (list-handler)
                   cljs.core/ChunkedCons           (list-handler)
                   cljs.core/ChunkedSeq            (list-handler)
                   cljs.core/PersisteQueueSeq      (list-handler)
                   cljs.core/PersisteQueue         (list-handler)
                   cljs.core/PersistentArrayMapSeq (list-handler)
                   cljs.core/PersistentTreeMapSeq  (list-handler)
                   cljs.core/NodeSeq               (list-handler)
                   cljs.core/ArrayNodeSeq          (list-handler)
                   cljs.core/KeySeq                (list-handler)
                   cljs.core/ValSeq                (list-handler)
                   cljs.core/PersistentArrayMap    (map-handler)
                   cljs.core/PersistentHashMap     (map-handler)
                   cljs.core/PersistentTreeMap     (map-handler)
                   cljs.core/PersistentHashSet     (set-handler)
                   cljs.core/PersistentTreeSet     (set-handler)
                   cljs.core/PersistentVector      (vector-handler)
                   cljs.core/Subvec                (vector-handler)]}
         (clj->js opts)))))

