# hiccup-find

Utilities to help you test hiccup-markup generating functions.

## Installation

Add this to your project.clj:

```clj
[hiccup-find  "0.1.0"]
```

## Usage

```clj
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
```

## License

Copyright © 2014 Magnar Sveen and Christian Johansen

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
