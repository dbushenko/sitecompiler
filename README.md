# sitecompiler

Static site compiler. See leiningen plugin here: https://github.com/dbushenko/lein-sitecompiler

For now accepts the following input formats:

Templates:
* Html with Mustache (https://github.com/fhd/clostache)
* Hiccup (https://github.com/weavejester/hiccup)
* Cuma (https://github.com/liquidz/cuma)
* Fleet (https://github.com/Flamefork/fleet)

Posts:
* Markdown (https://github.com/yogthos/markdown-clj)
* Html
* Hiccup (https://github.com/weavejester/hiccup)

## Usage

### Config file

In "config.clj"  you may specify the follosing options:

* :base-dir -- the directory where input, output and templates dirs are contained.
* :input-dir -- the directory where the input files (articles) are stored, relative to :base-dir. Default value is "input".
* :output-dir -- the directory where all generated files are stored, relative to :base-dir. Default value is "output".
* :templates-dir -- the directory where all page templates are stored, relative to :base-dir. Default values is "templates".
* :pages-in-list -- how many pages are shown in a list. Default value is 5.

You also may specify how to generate the pages. For single pages (which are not included in lists) you may specify the following options:

 :single-pages [{:file-name "promo"
                 :template "main"}
                {:file-name "authors"
                :template "authors"}]}

Here "single-pages" specifies, that the vector contains information how to generate single pages.		

* :file-name -- the file name (without the file extension).
* :template -- the template name (without the file extension) which should be applied to this file.

Following config option describes how to generate lists of articles:

 :tags-pages [{:tag "index"
               :list-template "index"
               :page-template "main"}]

* tag -- which tag to search for. If an article contains this tag, then it is included in the list.	       
* :list-template -- the template name (without extension) which describes how to generate the list of files.
* :page-template -- the template name (without extension) for generating individual pages from this list.

Run example config file as following:

	$ lein run examples/step1/config.clj

### Input files
	
The format of the input file should consist of following:

1. Header lines (one or more) -- the lines starting from the # sign.
2. Empty line.
3. Text of the input file.

The format of a header line is the following:

    #header-name: header value

Header name may be any string not containing whitespaces and colon sign. The value of this header is the rest of the string after the colon. For example:

    #date: 2014-04-26

This line creates a header "date" with value "2014-04-26".

There is one special header type -- #tags. This header contains a list of tags for this file separated by whitespaces.

### Templates

#### List templates

The following values are available within the list template:

* prev -- for previous list file name;
* next -- for next list file name;
* current -- current list file name;
* files -- all input files within this list file prepared for pagination (see the content of each input file further in 'Pages templates' section); also contains option 'page-file-name' in each file item for the generated page name;
* tag -- current tag for which this list was generated;
* latest -- latest file;
* data -- a map containing all tags and sorted list of input files corresponding those tags (tags are keywords here). These lists are not paginated.

#### Pages templates

The following values are available within the single page (or tag page) template:

* content -- the content of the input file without the headers;
* file-name -- the file name without extension;
* file-ext -- file extension;
* tags -- list of tags if available;
* data -- a map containing all tags and sorted lists of input files corresponding those tags (tags are keywords here). These lists are not paginated.
* all the headers you have specified in the input file.


In Fleet templates all data is contained in one compound variable -- 'data'.

## License

Copyright © 2013-2015 by Dmitry Bushenko

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
