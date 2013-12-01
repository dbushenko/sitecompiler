(ns sitecompiler.mustache
  (:require [sitecompiler.header :as header]
            [clostache.parser :as clst])
  (:use sitecompiler.common))

(defrecord MSTRenderer []
  Supported
  (supported? [this ext]
    (= "mst" ext))

  Renderer
  (render [this templ data]
     (clst/render templ data)))


(defn new-mst-renderer []
  (MSTRenderer.))

