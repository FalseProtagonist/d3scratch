(ns chestnut.test-runner
  (:require
   [cljs.test :refer-macros [run-tests]]
   [chestnut.core-test]))

(enable-console-print!)

(defn runner []
  (if (cljs.test/successful?
       (run-tests
        'chestnut.core-test))
    0
    1))
