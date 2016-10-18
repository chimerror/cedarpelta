(defproject cedarpelta "0.1.0-SNAPSHOT"
  :description "A simple ebooks-style Twitter bot"
  :url "https://github.com/foobardog/cedarpelta"
  :license {:name "MIT License"
            :url "https://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [clj-http "2.2.0"]
                 [org.apache.httpcomponents/httpclient "4.3.5"]
                 [twitter-api "0.7.8"]
                 [org.clojure/tools.logging "0.3.1"]
                 [org.slf4j/slf4j-log4j12 "1.7.12"]
                 [log4j/log4j "1.2.17"]
                 [janiczek/markov "0.3.1"]]
  :main ^:skip-aot cedarpelta.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
