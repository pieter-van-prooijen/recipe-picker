(ns recipe-picker.runner
    (:require [doo.runner :refer-macros [doo-tests]]
              [recipe-picker.core-test]))

(doo-tests 'recipe-picker.core-test)
