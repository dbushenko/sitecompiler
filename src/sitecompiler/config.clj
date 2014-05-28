(ns sitecompiler.config
  (:require [sitecompiler.common :as c])
  (:use io.aviso.ansi))

(defn new-config [config-fname]
  (try
    (let [cfg (load-string (slurp config-fname))
          base-dir (c/make-correct-dir-path (or (:base-dir cfg) "./"))
          input-dir (or (:input-dir cfg) "input")
          output-dir (or (:output-dir cfg) "output")
          templates-dir (or (:templates-dir cfg) "templates")
          pages-in-list (or (:pages-in-list cfg) 5)]
      (merge
       {:base-dir base-dir
        :input-dir (str base-dir input-dir)
        :output-dir (str base-dir output-dir)
        :templates-dir (str base-dir templates-dir)
        :pages-in-list pages-in-list}
       cfg))
    (catch java.io.FileNotFoundException ex
      (println (bold-red "No config file specified!"))
      (System/exit -1))
    (catch Exception ex
      (println (bold-red "Error in config file!"))
      (System/exit -1))))
