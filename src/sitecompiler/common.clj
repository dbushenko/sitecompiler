(ns sitecompiler.common)

(defprotocol Supported
  (supported? [this ext]))

(defprotocol Parser
  (parse [this fl]))

(defprotocol Renderer
  (render [this templ data]))

