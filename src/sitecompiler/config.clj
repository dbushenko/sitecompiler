(ns sitecompiler.config
  (:use io.aviso.ansi))

(defn new-config [config-fname]
  (try
    (let [cfg (load-string (slurp config-fname))]
      (merge
       {:input-dir "input"
        :output-dir "output"
        :templates-dir "templates"
        :pages-in-list 5}
       cfg))
    (catch java.io.FileNotFoundException ex
      (println (bold-red "Error in site config file!"))
      (System/exit -1))))
