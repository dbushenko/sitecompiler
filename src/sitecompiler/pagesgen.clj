(ns sitecompiler.pagesgen
  (:use io.aviso.ansi
        sitecompiler.common))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Individual pages from tags lists

(defn- generate-all-tags-pages [files-by-tags templates renderers input-files cfg-tag]
  (if (:page-template cfg-tag)  ;; Можем и не генерить отдельные страницы, если не указали шаблона
    (let [templ (first (filter #(= (:file-name %) (:page-template cfg-tag)) templates))
          files (filter #(contains? (:tags %) (:tag cfg-tag))
                        input-files)
          renderer (first (filter #(.supported? % (:file-ext templ))
                                  renderers))]
      (if (or (not templ) (not renderer))
        (do (println (bold-red (str "Can't render tags pages for tag '" (:tag cfg-tag) "'!")))
            (println "Template present:" (yes-no templ))
            (println "Renderer present:" (yes-no renderer))
            (System/exit -1)))
      {:tag (:tag cfg-tag)
       :files (doall (map #(assoc % :content (.render renderer (:content templ) (assoc % :data files-by-tags)))
                          files))})))

(defn generate-tags-pages [config files-by-tags templates input-files renderers]
  (doall (filter identity
                 (map (partial generate-all-tags-pages
                               files-by-tags
                               templates
                               renderers
                               input-files)
                      (:tags-pages config)))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Just single pages

(defn- generate-all-single-pages [files-by-tags templates renderers input-files cfg]
  (let [templ (first (filter #(= (:file-name %) (:template cfg)) templates))
        fl (first (filter #(= (:file-name %) (:file-name cfg))
                          input-files))
        renderer (first (filter #(.supported? % (:file-ext templ))
                                renderers))]
    (if (or (not templ) (not fl) (not renderer))
      (do (println (bold-red (str "Can't render page '" (:file-name cfg) "'!")))
          (println "Input file present:" (yes-no fl))
          (println "Template present:" (yes-no templ))
          (println "Renderer present:" (yes-no renderer))
          (System/exit -1)))
    (assoc fl :content (.render renderer (:content templ) (assoc fl :data files-by-tags)))))


(defn generate-single-pages [config files-by-tags templates input-files renderers]
  (doall (map (partial generate-all-single-pages
                       files-by-tags
                       templates
                       renderers
                       input-files)
              (:single-pages config))))
