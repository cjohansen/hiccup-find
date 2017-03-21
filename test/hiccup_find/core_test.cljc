(ns hiccup-find.core-test
  (:require #?(:cljs [cljs.test :refer-macros [deftest is testing run-tests]]
               :clj [clojure.test :refer :all])
            [hiccup-find.core :refer [hiccup-find
                                      hiccup-string
                                      hiccup-symbol-matches?
                                      hiccup-text
                                      hiccup-attrs-parts
                                      hiccup-symbol-parts
                                      normalized-symbol]]))

(deftest test-hiccup-symbol-matches?
  (is (hiccup-symbol-matches? :p :p.class))
  (is (not (hiccup-symbol-matches? :p.class :p)))
  (is (hiccup-symbol-matches? :.class :p.class.more))
  (is (hiccup-symbol-matches? :p.more.class :p.class.more)))

(deftest test-hiccup-attrs-parts []
  (is (= {:id "quux" :classes ["foo" "bar"]}
         (hiccup-attrs-parts [:div.x.y#z {:id "quux" :class "foo bar"}]))))

(deftest test-hiccup-symbol-parts []
  (is (= {:tag "div" :id "quux" :classes ["foo" "bar"]}
         (hiccup-symbol-parts [:div.foo.bar#quux]))))

(deftest test-normalized-symbol
  (is (= :div.foo.bar#quux (normalized-symbol [:div.foo.bar#quux])))
  (is (= :div.foo.bar#quux (normalized-symbol [:div.foo#quux {:class "bar"}])))
  (is (= :div.foo.bar#quux (normalized-symbol [:div.foo {:class "bar" :id "quux"}])))
  (is (= :div.foo.bar#quux (normalized-symbol [:div {:class "foo bar" :id "quux"}]))))

(deftest test-hiccup-find

  (is (= (list [:p.image "Yes 1"]
               [:p.image "Yes 2"])
         (hiccup-find [:p.image]
                      [:html
                       [:body
                        [:p.img "No"]
                        [:p.image "Yes 1"]
                        [:p.images "No"]
                        [:div.image
                         [:p.image "Yes 2"]]]])))

  (is (= (list [:p "Yes 1"]
               [:p "Yes 2"])
         (hiccup-find [:div :p]
                      [:html
                       [:body
                        [:p "No"]
                        [:div [:p "Yes 1"]]
                        [:div
                         [:table
                          [:tr
                           [:td
                            [:p "Yes 2"]]]]]]])))

  (is (= (list [:p "Yes 1"]
               [:p "Yes 2"]
               [:p "Yes 3"])
         (hiccup-find [:div :p]
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
                            [:p "Yes 3"]]]]]]]))))

(deftest test-hiccup-text
  (is (= "Text here"
         (hiccup-text [:p "Text here"])))

  (is (= "Text here"
         (hiccup-text [:p [:span "Text here"]])))

  (is (= "Text here"
         (hiccup-text [:p {:class "something"} "Text here"])))

  (is (= "Text here"
         (hiccup-text [:p {:class "something"} "Text " [:strong "here"]])))

  (is (= (str "Welcome, earthling\n"
              "Hope you enjoy your stay")
         (hiccup-text [:html
                       [:body
                        [:h1 "Welcome, earthling"]
                        [:p "Hope you " [:strong "enjoy"] " your stay"]]])))

  (is (= "Number 42"
         (hiccup-text [:html
                       [:body
                        [:p "Number " [:strong 42]]]]))))
(deftest test-hiccup-string
  (is (= "Welcome, earthling Hope you enjoy your stay"
         (hiccup-string [:html
                         [:body
                          [:h1 "Welcome, earthling"]
                          [:p "Hope you " [:strong "enjoy"] " your stay"]]])))

  (is (= "Welcome! Hope you enjoy your stay"
         (hiccup-string [:html
                         [:body
                          [:h1 "Welcome!"]
                          [:p "Hope you   " [:strong "enjoy"] " your stay"]]]))))
