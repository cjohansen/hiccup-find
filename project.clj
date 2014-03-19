(defproject hiccup-find "0.2.1"
  :description "Hiccup tree inspection for tests"
  :url "http://github.com/cjohansen/hiccup-find"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]]
  :profiles {:test {:dependencies [[midje "1.6.0"]]
                    :plugins [[lein-midje "3.1.3"]]}})
