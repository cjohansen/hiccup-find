(ns hiccup-find.runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [hiccup-find.core-test]))

(doo-tests 'hiccup-find.core-test)
