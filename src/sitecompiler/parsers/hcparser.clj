(ns sitecompiler.parsers.hcparser
  (:require [sitecompiler.header :as header]
            [hiccup.core :as hc])
  (:use sitecompiler.common))

(defrecord HiccupParser []
  Supported
  (supported? [this ext]
    (= "hiccup" ext))
  
  Parser  
  (parse [this fl]
    (let [h (header/parse-file fl)]
      (assoc h
        :content (hc/html (load-string (:content h)))))))


(defn new-hc-parser []
  (HiccupParser.))

