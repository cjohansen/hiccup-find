(ns hiccup-find.core
  (:require [clojure.set :as set]
            [clojure.string :as str]
            [clojure.set :refer [union]]
            [clojure.walk :refer [postwalk]]
            [clojure.string :as s]))

(defn hiccup-tree [tree]
  (tree-seq #(or (vector? %) (seq? %)) seq tree))

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
       hiccup-tree
       (filter vector?)))

(defn split-hiccup-symbol
  "Split the hiccup 'tag name' symbol into the tag name, class names and id"
  [symbol]
  (re-seq #"[:.#][^:.#]+" (str symbol)))

(defn hiccup-attrs-parts
  "Extracts the id and classes from the attributes of the given node."
  [node]
  (let [attrs (when (map? (second node)) (second node))]
    {:id (:id attrs)
     :attrs attrs
     :classes (re-seq #"[^ ]+" (:class attrs ""))}))

(defn hiccup-symbol-parts
  "Extracts the tag, id and classes from the symbol of the given node."
  [node]
  (let [coll (split-hiccup-symbol (first node))
        tag (first coll)
        id (first (filter #(str/starts-with? % "#") coll))
        classes (map #(subs % 1) (filter #(str/starts-with? % ".") coll))]
    {:tag (subs tag 1)
     :id (when id (subs id 1))
     :attrs {:id (when id (subs id 1)) :class (str/join " " classes)}
     :classes classes}))

(defn merge-attrs
  "Merge two attribute maps with special joining of :class"
  [a1 a2]
  (assoc (merge a1 a2)
         :class (s/trim (str (:class a1) " " (:class a2)))))

(defn hiccup-attrs
  "Returns attributes both from the symbol as well as attributes of the node"
  [node]
  (when node
    (-> (merge-attrs
         (:attrs (hiccup-symbol-parts node))
         (:attrs (hiccup-attrs-parts node))))))

(defn normalized-symbol
  "Takes a node and returns the tag symbol with classes and id
  calculated both from the tag classes and id as well as ones
  found from the attributes map.

  (normalized-symbol [:div.foo {:class \"bar\" :id \"quux\"}])

  returns

  :div.foo.bar#quux"
  [node]
  (let [from-attrs (hiccup-attrs-parts node)
        from-symbol (hiccup-symbol-parts node)
        tag (:tag from-symbol) ; not present in attrs
        id (or (:id from-attrs) (:id from-symbol)) ; prefer attributes
        classes (union (set (:classes from-attrs))
                       (set (:classes from-symbol)))]
    (keyword (str tag
                  (when classes (str "." (str/join "." classes)))
                  (when id (str "#" id))))))

(defn hiccup-symbol-matches?
  "Determine if a query matches a single hiccup node symbol"
  [q symbol]
  (set/subset? (set (split-hiccup-symbol q))
               (set (split-hiccup-symbol symbol))))

(defn hiccup-attribute-matches? [q node]
  (let [attrs (hiccup-attrs node)]
    (every? true?
            (for [[attribute matcher] q]
              (if (fn? matcher)
                (matcher (get attrs attribute))
                (= matcher (get attrs attribute)))))))

(defn hiccup-find
  "Return the node from the hiccup document matching the query, if any.
   The query is a vector of hiccup symbols; keywords naming tag names, classes
   and ids (either one or a combination) like :tag.class.class2#id"
  [query root]
  (if (and (seq root) (seq query))
    (recur (rest query)
           (->> root
                (hiccup-nodes)
                (filter #(hiccup-symbol-matches? (first query) (normalized-symbol %)))))
    root))

(def inline-elements
  #{:b :big :i :small :tt :abbr :acronym :cite :code :dfn :em :kbd
    :strong :samp :var :a :bdo :br :img :map :object :q :script
    :span :sub :sup :button :input :label :select :textarea})

(defn inline? [node]
  (and (vector? node) (contains? inline-elements (first node))))

(defn hiccup-text
  "Return only text from the hiccup structure; remove
   all tags and attributes"
  [tree]
  (->> (hiccup-tree tree)
       (reduce (fn [text node]
                 (cond
                  (inline? node) text
                  (vector? node) (str/replace text #"(.+)\n?$" #(str (second %1) "\n"))
                  (string? node) (str text node)
                  (number? node) (str text node)
                  :else text)) "")))

(defn hiccup-string
  "Return the hiccup-text as a one-line string; removes newlines and collapses
   multiple spaces."
  [tree]
  (-> tree
      hiccup-text
      (str/replace #"\n" " ")
      (str/replace #"\s+" " ")))
