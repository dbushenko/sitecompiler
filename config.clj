{:input-dir "site/input"
 :output-dir "site/output"
 :templates-dir "site/templates"
 :pages-in-list 3
 :tags-pages [{:tag "news"
               :list-template "index"
               :page-template "templ"}
              {:tag "clojure_tips"
               :list-template "index"
               :page-template "templ2"}]
 :single-pages [{:file-name "01"
                 :template "templ"}
                {:file-name "02"
                 :template "templ2"}]}
