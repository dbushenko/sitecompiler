(ns sitecompiler.config)

(defn new-config [config-fname]
  (let [cfg (load-string (slurp config-fname))]
    (merge
     {:input-dir "input"
      :output-dir "output"
      :templates-dir "templates"
      :pages-in-list 5}
     cfg)))
