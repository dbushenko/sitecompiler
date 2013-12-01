(ns sitecompiler.header)

(def ^:dynamic *date-format* (java.text.SimpleDateFormat. "yyyy-MM-dd"))
(def ^:dynamic *date-time-format* (java.text.SimpleDateFormat. "yyyy-MM-dd hh:mm"))

(defn- try-parse-date [str-date]
  (try
    (.parse *date-format* str-date)
    (catch Exception e
      (try
        (.parse *date-time-format* str-date)
        (catch Exception e2
          nil)))))

(defn parse-tags [tags-string]
  (let [arr (.split tags-string " ")]
    (apply hash-set (filter #(not (empty? %)) arr))))

(defn parse-header-line [line fname]
  (if (or (< (.length line) 4)
          (not (.startsWith line "#"))
          (= (.indexOf line ":") -1))
    (throw (Exception. "Wrong header in file " fname)))
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
          (assoc h :content (reduce str lines)))
        (recur (next lines)
               (concat header (parse-header-line line fname)))))))


(defn parse-file [fl]
  (let [lines (-> fl
                  clojure.java.io/reader
                  line-seq)]
    (parse-file-content lines (.getPath fl))))
