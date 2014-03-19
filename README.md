# hiccup-find

Utilities to help you test hiccup-markup generating functions.

## Installation

Add this to your project.clj:

```clj
[hiccup-find  "0.1.0"]
```

## Queries

Find nodes matching a query:

```clj
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
```

Queries are simple, and support tag names, classes, ids and parent/child
relationships.

### Tag name

```clj
[:h1]
```

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
assering on the text content is good enough.

```clj
(hiccup-text [:html
               [:body
                [:h1 "Welcome, earthling"]
                [:p "Hope you " [:strong "enjoy"] " your stay"]]])
;; => "Welcome, earthling\nHope you enjoy your stay\n")
```

`hiccup-text` appends a newline after each block element.

## License

Copyright © 2014 Magnar Sveen and Christian Johansen

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
