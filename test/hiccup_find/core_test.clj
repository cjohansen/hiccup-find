(ns hiccup-find.core-test
  (:require [midje.sweet :refer :all]
            [hiccup-find.core :refer :all]))

(fact (hiccup-symbol-matches? :p :p.class) => true
      (hiccup-symbol-matches? :p.class :p) => false
      (hiccup-symbol-matches? :.class :p.class.more) => true
      (hiccup-symbol-matches? :p.more.class :p.class.more) => true)

(fact (hiccup-find :p.image
                   [:html
                    [:body
                     [:p.img "No"]
                     [:p.image "Yes 1"]
                     [:p.images "No"]
                     [:div.image
                      [:p.image "Yes 2"]]]])
      => (list [:p.image "Yes 1"]
               [:p.image "Yes 2"]))
