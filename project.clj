(defproject payeer-clj "0.1.0"
  :description "Payeer api wraper for clojure"
  :url "https://github.com/CHNB128/Payeer-clj"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/data.json "0.2.6"]
                 [digest "1.4.9"]
                 [http-kit "2.3.0"]]
  :repl-options {:init-ns payeer.core})
