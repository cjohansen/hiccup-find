(ns hiccup-find.core-test
  (:require [midje.sweet :refer :all]
            [hiccup-find.core :refer :all]))

(fact (hiccup-symbol-matches? :p :p.class) => true
      (hiccup-symbol-matches? :p.class :p) => false
      (hiccup-symbol-matches? :.class :p.class.more) => true
      (hiccup-symbol-matches? :p.more.class :p.class.more) => true)

(fact (hiccup-find [:p.image]
                   [:html
                    [:body
                     [:p.img "No"]
                     [:p.image "Yes 1"]
                     [:p.images "No"]
                     [:div.image
                      [:p.image "Yes 2"]]]])
      => (list [:p.image "Yes 1"]
               [:p.image "Yes 2"]))

(fact (hiccup-find [:div :p]
                   [:html
                    [:body
                     [:p "No"]
                     [:div [:p "Yes 1"]]
                     [:div
                      [:table
                       [:tr
                        [:td
                         [:p "Yes 2"]]]]]]])
      => (list [:p "Yes 1"]
               [:p "Yes 2"]))

(fact (hiccup-find [:div :p]
                   [:html
                    [:body
                     [:div
                      [:table
                       [:tr
                        [:td
                         [:p "Yes 1"]]]]]
                     [:div
                      [:table
                       [:tr
                        [:td
                         [:p "Yes 2"]]]
                       [:tr
                        [:td
                         [:p "Yes 3"]]]]]]])
      => (list [:p "Yes 1"]
               [:p "Yes 2"]
               [:p "Yes 3"]))

(fact (hiccup-text [:p "Text here"])
      => "Text here")

(fact (hiccup-text [:p [:span "Text here"]])
      => "Text here")

(fact (hiccup-text [:p {:class "something"} "Text here"])
      => "Text here")

(fact (hiccup-text [:p {:class "something"} "Text " [:strong "here"]])
      => "Text here")

(fact (hiccup-text [:html
                    [:body
                     [:h1 "Welcome, earthling"]
                     [:p "Hope you " [:strong "enjoy"] " your stay"]]]) => (str "Welcome, earthling\n"
                                                                                "Hope you enjoy your stay"))

(fact (hiccup-text [:html
                    [:body
                     [:p "Number " [:strong 42]]]]) => "Number 42")
