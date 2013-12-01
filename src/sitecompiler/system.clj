(ns sitecompiler.system
  (:require [sitecompiler.config :as conf]
            [sitecompiler.reader :as rd]
            [sitecompiler.mdparser :as md]
            [sitecompiler.mustache :as mst]
            [sitecompiler.tagsgen :as tg]
            [sitecompiler.pagesgen :as pg])
  (:use light-dependency.core))

(defn system [& [cfg-file]]
  (with-dependencies
    :config (conf/new-config (or cfg-file "config.clj"))

    ;; Парсеры, которые не зависят от input-reader-а,
    ;; могут быть использованы для обработки входных данных
    :mdparser (md/new-md-parser)

    ;; Reader читает все входные файлы и преобразует их
    ;; в "сырой" html
    :reader (rd/new-input-reader
             inject-config
             [inject-mdparser])

    ;; Файлы, которые будем генерить
    :input-files (:input-files inject-reader)

    ;; Шаблоны для файлов
    :templates (:templates inject-reader)

    ;; Обработчики шаблонов (Mustache).
    :mstrenderer (mst/new-mst-renderer)

    ;; Собрать нужные тэги
    :tags-chunks (tg/chunks inject-config
                            inject-input-files)

    ;; Сгенерировать списки страниц по тегам
    :tags-lists (tg/generate inject-config
                             inject-templates
                             inject-tags-chunks
                             [inject-mstrenderer])

    ;; Сгенерить одиночные страницы по тегам
    :tags-pages (pg/generate-tags-pages inject-config
                                        inject-templates
                                        inject-input-files
                                        [inject-mstrenderer])

    ;; Сгенерить одиночные страницы без тегов
    :single-pages (pg/generate-single-pages inject-config
                                            inject-templates
                                            inject-input-files
                                            [inject-mstrenderer])))

