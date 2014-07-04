;; Copyright (c) Cognitect, Inc.
;; All rights reserved.

(ns transit.core
  (:require [com.cognitect.transit :as t]))

(enable-console-print!)

(defn opts-merge [a b]
  (doseq [k (js-keys b)]
    (let [v (aget b k)]
      (aset a k v)))
  a)

(deftype MapBuilder []
  Object
  (init [_] (transient {}))
  (add [_ m k v] (assoc! m k v))
  (finalize [_ m] (persistent! m)))

(deftype VectorBuilder []
  Object
  (init [_] (transient []))
  (add [_ v x] (conj! v x))
  (finalize [_ v] (persistent! v)))

(defn reader
  "Return a transit reader. type may be either :json or :json-verbose.
   opts may be a map optionally containing a :handlers entry. The value
   of :handlers should be map from tag to a decoder function which returns
   then in-memory representation of the semantic transit value."
  ([type] (reader type nil))
  ([type opts]
     (t/reader (name type)
       (opts-merge
         #js {:handlers
              #js {"$" (fn [v] (symbol v))
                   ":" (fn [v] (keyword v))
                   "set" (fn [v] (into #{} v))
                   "list" (fn [v] (into () (.reverse v)))}
              :mapBuilder (MapBuilder.)
              :arrayBuilder (VectorBuilder.)
              :prefersStrings false}
         (clj->js opts)))))

(deftype KeywordHandler []
  Object
  (tag [_ v] ":")
  (rep [_ v] (.-fqn v))
  (stringRep [_ v] (.-fqn v)))

(deftype SymbolHandler []
  Object
  (tag [_ v] "$")
  (rep [_ v] (.-str v))
  (stringRep [_ v] (.-str v)))

(deftype ListHandler []
  Object
  (tag [_ v] "list")
  (rep [_ v]
    (let [ret #js []]
      (doseq [x v] (.push ret x))
      (t/tagged "array" ret)))
  (stringRep [_ v] nil))

(deftype MapHandler []
  Object
  (tag [_ v] "map")
  (rep [_ v] v)
  (stringRep [_ v] nil))

(deftype SetHandler []
  Object
  (tag [_ v] "set")
  (rep [_ v]
    (let [ret #js []]
      (doseq [x v] (.push ret x))
      (t/tagged "array" ret)))
  (stringRep [v] nil))

(deftype VectorHandler []
  Object
  (tag [_ v] "array")
  (rep [_ v]
    (let [ret #js []]
      (doseq [x v] (.push ret x))
      ret))
  (stringRep [_ v] nil))

(defn writer
  "Return a transit writer. type maybe either :json or :json-verbose.
   opts is a map containing a :handlers entry. :handlers is a JavaScript
   array of interleaved type constructors and handler instances for those 
   type constructors."
  ([type] (writer type nil))
  ([type opts]
     (let [keyword-handler (KeywordHandler.)
           symbol-handler  (SymbolHandler.)
           list-handler    (ListHandler.)
           map-handler     (MapHandler.)
           set-handler     (SetHandler.)
           vector-handler  (VectorHandler.)
           handlers
           (merge
             {cljs.core/Keyword               keyword-handler
              cljs.core/Symbol                symbol-handler
              cljs.core/Range                 list-handler
              cljs.core/List                  list-handler
              cljs.core/Cons                  list-handler
              cljs.core/EmptyList             list-handler
              cljs.core/LazySeq               list-handler
              cljs.core/RSeq                  list-handler
              cljs.core/IndexedSeq            list-handler
              cljs.core/ChunkedCons           list-handler
              cljs.core/ChunkedSeq            list-handler
              cljs.core/PersistentQueueSeq    list-handler
              cljs.core/PersistentQueue       list-handler
              cljs.core/PersistentArrayMapSeq list-handler
              cljs.core/PersistentTreeMapSeq  list-handler
              cljs.core/NodeSeq               list-handler
              cljs.core/ArrayNodeSeq          list-handler
              cljs.core/KeySeq                list-handler
              cljs.core/ValSeq                list-handler
              cljs.core/PersistentArrayMap    map-handler
              cljs.core/PersistentHashMap     map-handler
              cljs.core/PersistentTreeMap     map-handler
              cljs.core/PersistentHashSet     set-handler
              cljs.core/PersistentTreeSet     set-handler
              cljs.core/PersistentVector      vector-handler
              cljs.core/Subvec                vector-handler}
             (:handlers opts))]
      (t/writer (name type)
        (opts-merge
          #js {:objectBuilder
               (fn [m kfn vfn]
                 (reduce-kv
                   (fn [obj k v]
                     (doto obj (.push (kfn k) (vfn v))))
                   #js ["^ "] m))
               :handlers
               (specify handlers
                 Object
                 (forEach
                   ([coll f]
                      (doseq [[k v] coll]
                        (f v k)))))}
          (clj->js (dissoc opts :handlers)))))))

