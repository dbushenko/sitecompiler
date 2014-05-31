(ns sitecompiler.core
  (:require [sitecompiler.system :as sys])
  (:use io.aviso.ansi)
  (:gen-class))

(defn- save-file [output name content]
  (let [s java.io.File/separator
        outDir (if (.endsWith output s) output (str output s))
        fname (str outDir name)]
    (spit fname content)))

(defn save-tags-lists [system]
  (let [tags-list (:tags-lists system)
        output (-> system :config :output-dir)]
        (dorun (map #(save-file output (:current %) (:content %))
                    tags-list))))


(defn save-tags-files [system]
  (let [tags-pages (:tags-pages system)
        output (-> system :config :output-dir)]
    (dorun (map (fn [tag-page]
                  (let [tag (:tag tag-page)
                        files (:files tag-page)]
                    (dorun (map #(save-file output
                                            (str tag "-" (:file-name %) ".html")
                                            (:content %))
                            files))))
                tags-pages))))

(defn save-single-pages [system]
  (let [single-pages (:single-pages system)
        output (-> system :config :output-dir)]
    (dorun (map #(save-file output (str (:file-name %) ".html") (:content %))
                single-pages))))

(defn -main [& args]
  (let [system (sys/system (first args))]
    (println (bold-blue "Saving tags lists..."))
    (save-tags-lists system)
    (println (bold-blue "Saving tags individual files..."))
    (save-tags-files system)
    (println (bold-blue "Saving separate pages..."))
    (save-single-pages system)))


