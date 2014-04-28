(ns sitecompiler.renderers.cuma
  (:require [cuma.core :as cuma])
  (:use sitecompiler.common))

(defrecord CumaRenderer []
  Supported
  (supported? [this ext]
    (= "cuma" ext))

  Renderer
  (render [this templ data]
     (cuma/render templ data)))


(defn new-cuma-renderer []
  (CumaRenderer.))

