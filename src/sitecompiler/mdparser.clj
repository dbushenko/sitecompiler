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
    (let [h (header/parse-file fl)]
      (assoc h
        :content (md-to-html-string (:content h))))))


(defn new-md-parser []
  (MDParser.))

