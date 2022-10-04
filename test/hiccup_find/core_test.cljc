(ns hiccup-find.core-test
  (:require #?(:cljs [cljs.test :refer-macros [deftest is]]
               :clj [clojure.test :refer [deftest is]])
            [hiccup-find.core :as hf]))

(deftest test-hiccup-form-matches?
  (is (hf/hiccup-form-matches? :p [:p.lol]))
  (is (hf/hiccup-form-matches? :p [:p {:class "lol"}]))
  (is (hf/hiccup-form-matches? :p.lol [:p.lol]))
  (is (hf/hiccup-form-matches? :p.lol [:p {:class "lol"}]))
  (is (hf/hiccup-form-matches? :p.lol [:p {:class "lol haha"}]))
  (is (hf/hiccup-form-matches? :p.lol.haha [:p {:class "lol haha"}]))
  (is (hf/hiccup-form-matches? :p.lol.haha [:p.lol {:class "haha"}]))
  (is (hf/hiccup-form-matches? :p.lol.haha [:p.haha {:class "lol"}]))
  (is (hf/hiccup-form-matches? :p.lol.haha [:p.haha.lol {}]))
  (is (not (hf/hiccup-form-matches? :p.lol.haha [:p {:class "lol"}])))
  (is (hf/hiccup-form-matches? :p.lol#ok [:p {:class "lol" :id "ok"}]))
  (is (hf/hiccup-form-matches? :p.lol [:p.haha {:class ["lol"]}]))
  (is (hf/hiccup-form-matches? :p.haha.lol [:p.haha {:class ["lol"]}]))
  (is (hf/hiccup-form-matches? :p.lol.haha.hihi [:p.haha {:class ["hihi" "lol"]}]))
  (is (hf/hiccup-form-matches? :p.lol [:p.haha {:class ["hihi" "lol"]}])))

(deftest test-hiccup-symbol-matches?
  (is (hf/hiccup-symbol-matches? :p :p.class))
  (is (not (hf/hiccup-symbol-matches? :p.class :p)))
  (is (hf/hiccup-symbol-matches? :.class :p.class.more))
  (is (hf/hiccup-symbol-matches? :p.more.class :p.class.more)))

(deftest test-hiccup-find
  (is (= (hf/hiccup-find
          [:p.image]
          [:html
           [:body
            [:p.img "No"]
            [:p.image "Yes 1"]
            [:p.images "No"]
            [:div.image
             [:p.image "Yes 2"]]]])
         (list [:p.image "Yes 1"]
               [:p.image "Yes 2"])))

  (is (= (hf/hiccup-find
          [:p.image]
          [:html
           [:body
            [:p.img "No"]
            [:p {:class "image"} "Yes 1"]
            [:p.images "No"]
            [:div.image
             [:p {:class "image"} "Yes 2"]]]])
         (list [:p {:class "image"} "Yes 1"]
               [:p {:class "image"} "Yes 2"])))

  (is (= (hf/hiccup-find
          [:div :p]
          [:html
           [:body
            [:p "No"]
            [:div [:p "Yes 1"]]
            [:div
             [:table
              [:tr
               [:td
                [:p "Yes 2"]]]]]]])
         (list [:p "Yes 1"]
               [:p "Yes 2"])))

  (is (= (hf/hiccup-find
          [:div :p]
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
         (list [:p "Yes 1"]
               [:p "Yes 2"]
               [:p "Yes 3"])))

  (is (= (hf/hiccup-find
          [:div :p]
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
         (list [:p "Yes 1"]
               [:p "Yes 2"]
               [:p "Yes 3"]))))

(deftest test-hiccup-text
  (is (= (hf/hiccup-text [:p "Text here"])
         "Text here"))

  (is (= (hf/hiccup-text [:p [:span "Text here"]])
         "Text here"))

  (is (= (hf/hiccup-text [:p {:class "something"} "Text here"])
         "Text here"))

  (is (= (hf/hiccup-text [:p {:class "something"} "Text " [:strong "here"]])
         "Text here"))

  (is (= (hf/hiccup-text
          [:html
           [:body
            [:h1 "Welcome, earthling"]
            [:p "Hope you " [:strong "enjoy"] " your stay"]]])
         (str "Welcome, earthling\n"
              "Hope you enjoy your stay")))

  (is (= (hf/hiccup-text
          [:html
           [:body
            [:p "Number " [:strong 42]]]])
         "Number 42")))

(deftest test-hiccup-string
  (is (= (hf/hiccup-string
          [:html
           [:body
            [:h1 "Welcome, earthling"]
            [:p "Hope you " [:strong "enjoy"] " your stay"]]])
         "Welcome, earthling Hope you enjoy your stay"))

  (is (= (hf/hiccup-string
          [:html
           [:body
            [:h1 "Welcome!"]
            [:p "Hope you   " [:strong "enjoy"] " your stay"]]])
         "Welcome! Hope you enjoy your stay")))
