(defproject hiccup-find "0.4.0"
  :description "Hiccup tree inspection for tests"
  :url "http://github.com/cjohansen/hiccup-find"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]]
  :profiles {:dev {:dependencies [[midje "1.8.3"]]
                   :plugins [[lein-midje "3.2.1"]]}})
