(ns sitecompiler.core
  (:require [sitecompiler.system :as sys]))

(defn- save-file [output name content]
  (let [s java.io.File/separator
        outDir (if (.endsWith output s) output (str output s))
        fname (str outDir name ".html")]
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
                                            (str tag "-" (:file-name %))
                                            (:content %))
                            files))))
                tags-pages))))

(defn save-single-pages [system]
  (let [single-pages (:single-pages system)
        output (-> system :config :output-dir)]
    (dorun (map #(save-file output (:file-name %) (:content %))
                single-pages))))

(defn -main [& args]
  (let [system (sys/system (first args))]
    (save-tags-lists system)
    (save-tags-files system)
    (save-single-pages system)))


