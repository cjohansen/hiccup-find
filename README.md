# hiccup-find

Very rudimentary querying of hiccup documents.

## Installation

Add this to your project.clj:

```clj
[hiccup-find  "1.0.0"]
```

## Queries

Find nodes matching a query:

```clj
(ns my.stuff-test
  (:require [hiccup-find.core :as hf]))

(fact (hf/hiccup-find [:p.image]
                   [:html
                    [:body
                     [:p.img "No"]
                     [:p.image "Yes 1"]
                     [:p.images "No"]
                     [:div.image
                      [:p.image "Yes 2"]]]])
      => (list [:p.image "Yes 1"]
               [:p.image "Yes 2"]))
```

Queries support tag names, classes, ids and parent/child relationships.

### Tag name

```clj
[:h1]
```

### Class name

```clj
[:.some-class] ; Match elements with this class
[:h2.heading]  ; Match elements with tag name h2 and class heading
[:p.important.pitch] ; Match elements with tagname p and the
                     ; two classes "important", and "pitch", in any order.
```

### ID

```clj
[:#main]            ; Match elements with this id
[:h2#main-heading]  ; Match elements with tag name h2 and id main-heading
[:h2#main-heading.important] ; Match element with tagname, class and id
```

### Parent

```clj
[:#main h2] ; Find all h2s in an element with id "main"
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

Test hiccup-find in Clojure and ClojureScript with

```sh
lein test-all
```

[PhantomJS](http://phantomjs.org/) is a prerequisite, since that's where the
ClojureScript tests are run.

## License

Copyright © 2014-2018 Christian Johansen, Magnar Sveen, and Ian Truslove

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
