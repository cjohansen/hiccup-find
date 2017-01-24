(defproject hiccup-find "0.5.0"
  :description "Hiccup tree inspection for tests"

  :url "http://github.com/cjohansen/hiccup-find"

  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.293"]]

  :plugins [[lein-cljsbuild "1.1.5"]
            [lein-doo "0.1.7"]]

  :aliases {"test-cljs" ["doo" "phantom" "test" "once"]
            "test-clj" ["test"]
            "test-all" ["do" ["test-clj"] ["test-cljs"]]}

  :cljsbuild {:builds {:test {:source-paths ["src" "test"]
                              :compiler {:output-to "target/cljs/test-hiccup-find.js"
                                         :output-dir "target/out"
                                         :main hiccup-find.runner
                                         :optimizations :none
                                         :pretty-print true}}}})
