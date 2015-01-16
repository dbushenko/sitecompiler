(ns sitecompiler.header
  (:use io.aviso.ansi))

;; Используется исключительно для разбора значения заголовка tags
(defn parse-tags [tags-string]
  (let [arr (.split tags-string " ")]
    (apply hash-set (filter #(not (empty? %)) arr))))

;; Функция умеет разбирать заголовок.
;;
;; Все заголовки выглядят так:
;; #header-name: header value
;; Символ # означает начало заголовка
;; Символ : отделяет название заголовка от его значения, пробелы после : и в конце строки удаляются
;; Здесь название заголовка -- header-name,
;; Значение заголовка -- "header value"
(defn parse-header-line [line fname]
  (if (or (< (.length line) 4)         ;; Строка заголовка не может быть меньше 4 символов
          (not (.startsWith line "#")) ;; Заголовок должен начинаться с символа #
          (= (.indexOf line ":") -1))  ;; Строка заголовка должна содержать как название, так и значение заголовка
    (do (println (bold-red (str "Wrong header in file " fname)))
        (System/exit -1)))
  ;; Вырезаем из строки название и значение заголовка
  (let [s (.substring line 1)
        splitter (.indexOf s ":")
        content (.trim (.substring s (inc splitter)))
        header (.substring s 0 splitter)]
    ;; Заголовок tags имеет специальное значение и предназначен для хранения
    ;; множества тэгов, к которым относится статья. Поэтому значение заголовка
    ;; tags -- множество строк, разделенных пробелами.
    (if (= header "tags")
      (vector (keyword header) (parse-tags content))
      (vector (keyword header) content))))

(defn parse-file-content [raw-lines fname]
  ;; Выполняем разбор построчно
  (loop [lines raw-lines
         header []]
    (let [line (first lines)]
      (if (or (empty? lines)
              (not (.startsWith line "#")))
        
        ;; Первая строка без префикса # означает, что дальше идет содержимое
        ;; статьи, которое запишем в :content
        (let [h (apply hash-map header)] ;; Здесь список векторов с заголовками-значениями преобразовывается в мапку
          ;; Содержимое файла -- строка, склеенная из оставшихся строк
          (assoc h :content (reduce #(str %1 %2 "\n") "" lines)))
        
        ;; Входная строка начинается с символа # и является заголовком
        (recur (next lines)
               ;; Разбираем заголовок, добавляем его в коллекцию header
               (concat header (parse-header-line line fname)))))))


(defn parse-file [fl]
  (let [file-path (.getPath fl)]
    (println (bold-green (str "Parsing " file-path)))
    ;; Получить содержимое файла как мапку
    (let [lines (-> fl
                    clojure.java.io/reader
                    line-seq)]
      (if (empty? lines) ;; Не обрабатываем пустые файлы
        (do (println (bold-red (str "File " file-path " has invalid format!")))
            (System/exit -1)))
      ;; Выполнить разбор файла
      (parse-file-content lines file-path))))
