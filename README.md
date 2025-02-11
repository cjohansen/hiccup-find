# hiccup-find

Very rudimentary querying of hiccup documents.

## Installation

With tools.deps:

```clj
hiccup-find/hiccup-find {:mvn/version "2022.10.03"}
```

With Leiningen:

```clj
[hiccup-find "2022.10.03"]
```

## Queries

Find nodes matching a query:

```clj
(ns my.stuff-test
  (:require [hiccup-find.core :as hf]))

(hf/hiccup-find [:p.image]
                [:html
                 [:body
                  [:p.img "No"]
                  [:p.image "Yes 1"]
                  [:p.images "No"]
                  [:div.image
                   [:p.image "Yes 2"]]]])

;;=> (list [:p.image "Yes 1"]
;;         [:p.image "Yes 2"])
```

Queries support tag names, classes, ids and parent/child relationships.

### Tag name

```clj
[:h1]
```

### Class name

```clj
;; Match elements with this class
[:.some-class]

;; Match elements with tag name h2 and class heading
[:h2.heading]

;; Match elements with tagname p and the two classes
;; "important", and "pitch", in any order.
[:p.important.pitch]
```

### ID

```clj
;; Match elements with this id
[:#main]

;; Match elements with tag name h2 and id main-heading
[:h2#main-heading]

;; Match element with tagname, class and id
[:h2#main-heading.important]
```

### Parent

```clj
;; Find all h2s in an element with id "main"
[:#main h2]
```

## Text

To avoid having tests become overly coupled to details in the markup (such as
OOCSS classes, which basically couple markup with visual appearance), sometimes
asserting on the text content is good enough.

```clj
(hf/hiccup-text [:html
                 [:body
                  [:h1 "Welcome, earthling"]
                  [:p "Hope you " [:strong "enjoy"] " your stay"]]])
;; => "Welcome, earthling\nHope you enjoy your stay\n")
```

`hiccup-text` appends a newline after each block element.

For a textual representation that reveals even fewer details about the
underlying structure, try `hiccup-string`. It works just like `hiccup-text`,
except it returns a string with newlines replaced by space characters, and all
consecutive spaces condensed to one.

## Testing

Test hiccup-find in Clojure with

```sh
bin/kaocha
```

To run the ClojureScript tests in node:

```sh
bin/kaocha unit-cljs
```

## Changelog

### 2022.10.03

Support matching against classes in a vector.

### 2021.10.12 1.0.1

Moved Clojure and ClojureScript dependencies to the dev profile.

## License

Copyright © 2014-2024 Christian Johansen, Magnar Sveen, and Ian Truslove

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
