(ns sitecompiler.tagsgen
  (:use io.aviso.ansi
        sitecompiler.common))

(defn make-list-file-name [tag num]
  (case num
    1 tag
    nil nil
    (str tag num)))

(defn make-next-prev-chunk [chunks tag]
  (doall (map
          (fn [ch i]
            (let [ind (inc i)]
              {:prev (make-list-file-name tag (if (= ind (count chunks)) nil (inc ind)))
               :next (make-list-file-name tag (if (= ind 1) nil (dec ind)))
               :current (make-list-file-name tag ind)
               :files ch
               :tag tag
               :latest (first ch) }))
          chunks (range (count chunks)))))

(defn prepare-tag [pg-count files tag]
  (let [t (:tag tag)
        fl (->> files
                (filter #(contains? (:tags %) t))
                (sort-by :file-name)
                reverse
                (map #(assoc % :page-file-name (str t "-" (:file-name %) ".html")))
                (partition-all pg-count))]
    {:tag t
     :chunks (make-next-prev-chunk fl t)}))

;; Разбить набор входных файлов по тэгам, а внутри каждого
;; тэга -- на наборы фрагментов, из которых будут сформированы
;; страницы списка
(defn chunks [config input-files]
  (let [prep-tag (partial prepare-tag
                          (:pages-in-list config)
                          input-files)
        tags (doall (map prep-tag (:tags-pages config)))]
    tags))

(defn generate-tags-list [files-by-tags templates renderers fl-chunks cfg-tag]
  ;; Для cfg-tag:
  ;; 1) Найти подходящий template
  ;; 2) Найти fl-chunks с этим же тэгом
  ;; 3) Сгенерить страницы списков
  (if (:list-template cfg-tag) ;; Можем и не генерить список, если не указали шаблона
    (let [tag-name (:tag cfg-tag)
          templ (first (filter #(= (:file-name %) (:list-template cfg-tag))
                               templates))
          chunks (:chunks (first (filter #(= (:tag %) tag-name)
                                         fl-chunks)))
          renderer (first (filter #(.supported? % (:file-ext templ))
                                  renderers))]
      (if (or (not templ) (not renderer))
        (do (println (bold-red (str "Can't render tags list '" tag-name "'!")))
            (println "Template present:" (yes-no templ))
            (println "Renderer present:" (yes-no renderer))
            (System/exit -1)))
      (doall (map #(-> %
                       (assoc :content (.render renderer (:content templ)
                                                (assoc % :data files-by-tags)))
                       (dissoc :files))
                  chunks)))))


;; tg-chunks: {:tag :chunks {:next-index :prev-index :current-index :files}}
(defn generate [config files-by-tags templates tg-chunks renderers]
  (reduce concat (filter identity
                         (map (partial generate-tags-list
                                       files-by-tags
                                       templates
                                       renderers
                                       tg-chunks)
                              (:tags-pages config)))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Input data
;;
(defn filter-by-tag [files tag]
  (let [fl (reverse
            (sort-by
             :file-name
             (filter #(contains? (:tags %) tag) files)))]
    {:tag tag
     :data fl}))

(defn get-all-tags [input-files]
  (loop [res #{}
         fl input-files]
    (if (empty? fl)
      (into #{} res)
      (recur (concat res (:tags (first fl)))
             (next fl)))))

(defn split-by-tags [input-files]
  (let [all-tags (get-all-tags input-files)
        data (map #(filter-by-tag input-files %) all-tags)]
    (reduce #(assoc %1 (keyword (:tag %2)) (:data %2)) {} data)))

