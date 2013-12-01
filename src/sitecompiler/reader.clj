(ns sitecompiler.reader)

(defn- find-data-files [dir-name]
  (filter (memfn isFile)
          (-> dir-name
              clojure.java.io/file
              file-seq)))

(defn get-fname-extension [fname]
  (let [ind (.lastIndexOf fname ".")]
    (if (> ind 0)
      (.substring fname (inc ind)))))

(defn get-fname-without-extension [fname]
  (let [ind (.lastIndexOf fname ".")]
    (if (> ind 0)
      (.substring fname 0 ind))))

;; Cреди всех доступных парсеров ищет тот, который может распарсить
;; указанный тип файла, и парсит его.
(defn parse-file [parsers fl]
  (let [ext (get-fname-extension (.getName fl))
        name (get-fname-without-extension (.getName fl))]
    (first
     (filter #(not (nil? %))
             (map (fn [p] (if (.supported? p ext)
                            (assoc (.parse p fl)
                              :file-name name
                              :file-ext ext)))
                  parsers)))))

(defn load-template [fl]
  (let [ext (get-fname-extension (.getName fl))
        name (get-fname-without-extension (.getName fl))
        content (slurp (java.io.FileReader. fl))]
    {:file-name name
     :file-ext ext
     :content content}))

(defn new-input-reader [config parsers]
  (let [input-files (find-data-files (:input-dir config))
        templ-files (find-data-files (:templates-dir config))
        parse (partial parse-file parsers)]
    {:input-files (doall (map parse input-files))
     :templates (doall (map load-template templ-files))}))

