(ns sitecompiler.hiccup
  (:require [hiccup.core :as hc])
  (:use sitecompiler.common))

(defrecord HiccupRenderer []
  Supported
  (supported? [this ext]
    (= "hiccup" ext))

  Renderer
  (render [this templ data]
    (hc/html (load-string
              (str "(let [data " data "]" templ ")")))))

(defn new-hc-renderer []
  (HiccupRenderer.))

