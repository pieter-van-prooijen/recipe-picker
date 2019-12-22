(ns recipe-picker.core-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [recipe-picker.core :as core]))

(deftest fake-test
  (testing "fake description"
    (is (= 1 2))))
