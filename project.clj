(defproject sitecompiler "0.2.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [io.aviso/pretty "0.1.10"]
                 [markdown-clj "0.9.43"]
                 [de.ubercode.clostache/clostache "1.4.0"]
                 [hiccup "1.0.5"]
                 [cuma "0.0.8"]
                 [fleet "0.10.1"]]
  :main sitecompiler.core)
