(ns sitecompiler.parsers.htmlparser
  (:require [sitecompiler.header :as header])
  (:use sitecompiler.common))

(defrecord HtmlParser []
  Supported
  (supported? [this ext]
    (= "html" ext))
  
  Parser  
  (parse [this fl]
    (let [h (header/parse-file fl)]
      (assoc h
        :content (:content h)))))


(defn new-html-parser []
  (HtmlParser.))

