(ns sitecompiler.renderers.mustache
  (:require [clostache.parser :as clst])
  (:use sitecompiler.common))

(defrecord MSTRenderer []
  Supported
  (supported? [this ext]
    (= "mustache" ext))

  Renderer
  (render [this templ data]
    (println "####" data)
    (clst/render templ data)))


(defn new-mst-renderer []
  (MSTRenderer.))

