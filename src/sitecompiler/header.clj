(ns sitecompiler.header
  (:use io.aviso.ansi))

(defn parse-tags [tags-string]
  (let [arr (.split tags-string " ")]
    (apply hash-set (filter #(not (empty? %)) arr))))

(defn parse-header-line [line fname]
  (if (or (< (.length line) 4)
          (not (.startsWith line "#"))
          (= (.indexOf line ":") -1))
    (do (println (bold-red (str "Wrong header in file " fname)))
        (System/exit -1)))
  (let [s (.substring line 1)
        splitter (.indexOf s ":")
        content (.trim (.substring s (inc splitter)))
        header (.substring s 0 splitter)]
    (if (= header "tags")
      (vector (keyword header) (parse-tags content))
      (vector (keyword header) content))))

(defn parse-file-content [raw-lines fname]
  (loop [lines raw-lines
         header []]
    (let [line (first lines)]
      (if (or (empty? lines)
              (not (.startsWith line "#")))
        (let [h (apply hash-map header)]
          (assoc h :content (reduce #(str %1 %2 "\n") "" lines)))
        (recur (next lines)
               (concat header (parse-header-line line fname)))))))


(defn parse-file [fl]
  (let [file-path (.getPath fl)]
    (println (bold-green (str "Parsing " file-path)))
    (let [lines (-> fl
                    clojure.java.io/reader
                    line-seq)]
      (if (empty? lines)
        (do (println (bold-red (str "File " file-path " has invalid format!")))
            (System/exit -1)))
      (parse-file-content lines file-path))))
