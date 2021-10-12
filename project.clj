(defproject hiccup-find "1.0.1"
  :description "Hiccup tree inspection for tests"
  :url "http://github.com/cjohansen/hiccup-find"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :profiles {:dev {:dependencies [[org.clojure/clojure "1.10.3"]
                                  [org.clojure/clojurescript "1.10.879"]]}}
  :plugins [[lein-cljsbuild "1.1.8"]
            [lein-doo "0.1.11"]]
  :aliases {"test-cljs" ["doo" "firefox-headless" "test" "once"]
            "test-clj" ["test"]
            "test-all" ["do" ["test-clj"] ["test-cljs"]]}
  :cljsbuild {:builds {:test {:source-paths ["src" "test"]
                              :compiler {:output-to "target/cljs/test-hiccup-find.js"
                                         :output-dir "target/out"
                                         :main hiccup-find.runner
                                         :optimizations :none
                                         :pretty-print true}}}})
