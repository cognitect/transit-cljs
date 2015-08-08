(require '[cljs.build.api :as b])

(b/build (b/inputs "src" "test")
  {:output-to "target/transit.test.js"
   :output-dir "target/out-test"
   :optimizations :simple
   :verbose true
   :closure-warnings
   {:check-types :error
    :externs-validation :off
    :check-useless-code :off
    :missing-properties :off
    :undefined-names :off}})

(System/exit 0)