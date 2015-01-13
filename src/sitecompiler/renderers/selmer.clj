(ns sitecompiler.renderers.selmer
  (:require [selmer.parser :as selmer])
  (:use sitecompiler.common))

(defrecord SelmerRenderer []
  Supported
  (supported? [this ext]
    (= "selmer" ext))

  Renderer
  (render [this templ data]
     (selmer/render templ data)))


(defn new-selmer-renderer []
  (SelmerRenderer.))
