(ns transit.test-runner
  (:require [cljs.test :refer [run-tests]]
            [transit.test.core]))

(set! *print-newline* false)

;; When testing Windows we default to Node.js
(if (exists? js/print)
  (set-print-fn! js/print)
  (enable-console-print!))

(run-tests 'transit.test.core)
