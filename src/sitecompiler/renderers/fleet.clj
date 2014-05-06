(ns sitecompiler.renderers.fleet
  (:require fleet)
  (:use sitecompiler.common))

(defrecord FleetRenderer []
  Supported
  (supported? [this ext]
    (= "fleet" ext))

  Renderer
  (render [this templ data]
     (fleet/fleet data templ)))


(defn new-fleet-renderer []
  (FleetRenderer.))

