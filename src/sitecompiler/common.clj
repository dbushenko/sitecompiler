(ns sitecompiler.common)

(defprotocol Supported
  (supported? [this ext]))

(defprotocol Parser
  (parse [this fl]))

(defprotocol Renderer
  (render [this templ data]))

(defn yes-no [val]
  (if val "yes" "no"))

(defn make-correct-dir-path [path]
  (let [sep java.io.File/separator]
    (if (.endsWith path sep)
      path
      (str path sep))))
