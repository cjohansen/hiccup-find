(ns hiccup-find.core
  (:require [clojure.set :as set]))

(defn hiccup-nodes
  "Takes a hiccup tree and returns a list of all the nodes in it.

[:html
 [:body
  '([:p \"Hey\"]
    [:p \"There\"])]]

turns into

([:html [:body '([:p \"Hey\"] [:p \"There\"])]]
 [:body '([:p \"Hey\"] [:p \"There\"])]
 [:p \"Hey\"]
 [:p \"There\"])"
  [root]
  (->> root
       (tree-seq #(or (vector? %) (seq? %)) seq)
       (filter vector?)))

(defn split-hiccup-symbol
  "Split the hiccup 'tag name' symbol into the tag name, class names and id"
  [symbol]
  (re-seq #"[:.#][^:.#]+" (str symbol)))

(defn hiccup-symbol-matches?
  "Determine if a query matches a single hiccup node symbol"
  [q symbol]
  (set/subset? (set (split-hiccup-symbol q))
               (set (split-hiccup-symbol symbol))))

(defn hiccup-find
  "Return the node from the hiccup document matching the query, if any.
   The query is a vector of hiccup symbols; keywords naming tag names, classes
   and ids (either one or a combination) like :tag.class.class2#id"
  [query root]
  (if (and (seq root) (seq query))
    (recur (rest query)
           (->> root
                (hiccup-nodes)
                (filter #(hiccup-symbol-matches? (first query) (first %)))))
    root))
