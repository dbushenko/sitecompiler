(defproject sitecompiler "0.2.7"
  :description "Static site compiler"
  :url "https://github.com/dbushenko/sitecompiler"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [io.aviso/pretty "0.1.26"]
                 [markdown-clj "0.9.89" :exclusions [org.clojure/clojure]]
                 [de.ubercode.clostache/clostache "1.4.0"]
                 [hiccup "1.0.5"]
                 [cuma "0.1.1"]
                 [fleet "0.10.2"]
                 [selmer "1.0.6"]]
  :main sitecompiler.core)
