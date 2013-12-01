(ns sitecompiler.pagesgen)

(defn- generate-all-tags-pages [templates renderers input-files cfg-tag]
  (let [templ (first (filter #(= (:file-name %) (:page-template cfg-tag)) templates))
        files (filter #(contains? (:tags %) (:tag cfg-tag))
                      input-files)
        renderer (first (filter #(.supported? % (:file-ext templ))
                                renderers))]
    {:tag (:tag cfg-tag)
     :files (doall (map #(assoc % :content (.render renderer (:content templ) %))
                        files))}))

(defn generate-tags-pages [config templates input-files renderers]
  (doall (map (partial generate-all-tags-pages
                       templates
                       renderers
                       input-files)
              (:tags-pages config))))


(defn- generate-all-single-pages [templates renderers input-files cfg]
  (let [templ (first (filter #(= (:file-name %) (:template cfg)) templates))
        fl (first (filter #(= (:file-name %) (:file-name cfg))
                          input-files))
        renderer (first (filter #(.supported? % (:file-ext templ))
                                renderers))]
    (assoc fl :content (.render renderer (:content templ) fl))))


(defn generate-single-pages [config templates input-files renderers]
  (doall (map (partial generate-all-single-pages
                       templates
                       renderers
                       input-files)
              (:single-pages config))))
