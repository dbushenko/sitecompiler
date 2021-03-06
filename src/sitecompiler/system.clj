(ns sitecompiler.system
  (:require [sitecompiler.config :as conf]
            [sitecompiler.reader :as rd]
            [sitecompiler.parsers.mdparser :as md]
            [sitecompiler.parsers.htmlparser :as html]
            [sitecompiler.parsers.hcparser :as hcp]
            [sitecompiler.renderers.mustache :as mst]
            [sitecompiler.renderers.cuma :as cuma]
            [sitecompiler.renderers.selmer :as selmer]
            [sitecompiler.renderers.fleet :as fleet]
            [sitecompiler.renderers.hiccup :as hc]
            [sitecompiler.tagsgen :as tg]
            [sitecompiler.pagesgen :as pg]))

(defn system [& [cfg-file]]
  (let [config (conf/new-config (or cfg-file "config.clj"))

        ;; Парсеры, которые не зависят от input-reader-а,
        ;; могут быть использованы для обработки входных данных
        mdparser (md/new-md-parser)
        htmlparser (html/new-html-parser)
        hcparser (hcp/new-hc-parser)

        ;; Reader читает все входные файлы и преобразует их
        ;; в "сырой" html
        reader (rd/new-input-reader
                config
                [htmlparser
                 hcparser
                 mdparser])

        ;; Файлы, которые будем генерить
        input-files (:input-files reader)

        ;; Шаблоны для файлов
        templates (:templates reader)

        ;; Обработчики шаблонов (Mustache).
        mstrenderer (mst/new-mst-renderer)
        hcrenderer (hc/new-hc-renderer)
        cumarenderer (cuma/new-cuma-renderer)
        selmerrenderer (selmer/new-selmer-renderer)
        fleetrenderer (fleet/new-fleet-renderer)

        ;; Разобрать все входные файлы по тегам.
        ;; Эти списки не адаптированы для pagination,
        ;; это просто набор входных даных, доступный при
        ;; генерации любой страницы
        files-by-tags (tg/split-by-tags input-files)
        

        ;; Рендереры шаблонов
        rendrers-list [mstrenderer
                       hcrenderer
                       cumarenderer
                       fleetrenderer
                       selmerrenderer]

        ;; Собрать входные файлы в наборы фрагментов,
        ;; один фрагмент для -- для одной страницы списка
        tags-chunks (tg/chunks config
                               input-files)

        ;; Сгенерировать списки страниц по тегам
        tags-lists (tg/generate config
                                files-by-tags
                                templates
                                tags-chunks
                                rendrers-list)

        ;; Сгенерить одиночные страницы по тегам
        tags-pages (pg/generate-tags-pages config
                                           files-by-tags
                                           templates
                                           input-files
                                           rendrers-list)

        ;; Сгенерить одиночные страницы без тегов
        single-pages (pg/generate-single-pages config
                                               files-by-tags
                                               templates
                                               input-files
                                               rendrers-list)
        ]
    {:config config
     :tags-lists tags-lists
     :tags-pages tags-pages
     :input-files input-files
     :single-pages single-pages
     } ))

