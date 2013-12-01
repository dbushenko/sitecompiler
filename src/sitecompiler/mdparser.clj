(ns sitecompiler.mdparser
  (:require [sitecompiler.header :as header])
  (:use markdown.core
        sitecompiler.common))

(defrecord MDParser []
  Supported
  (supported? [this ext]
    (= "md" ext))
  
  Parser  
  (parse [this fl]
    (let [lines (-> fl
                    clojure.java.io/reader
                    line-seq)
          parsed-header (header/parse-file-content lines (.getPath fl))]
      (assoc parsed-header
        :content (md-to-html-string (:content parsed-header))))))


(defn new-md-parser []
  (MDParser.))

