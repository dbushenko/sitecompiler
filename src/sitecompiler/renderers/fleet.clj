(ns sitecompiler.renderers.fleet
  (:require fleet)
  (:use sitecompiler.common))

(defrecord FleetRenderer []
  Supported
  (supported? [this ext]
    (= "fleet" ext))

  Renderer
  (render [this templ data]
    (let [r (fleet/fleet [data] templ)] ;; renderer arguments are stored in 'data'
      (r data))))


(defn new-fleet-renderer []
  (FleetRenderer.))

