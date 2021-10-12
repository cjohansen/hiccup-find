FIREFOX_BIN ?= /Applications/Firefox Developer Edition.app/Contents/MacOS/firefox

node_modules:
	npm install

test-cljs: node_modules
	PATH="./node_modules/.bin:${PATH}" FIREFOX_BIN="$(FIREFOX_BIN)" lein test-cljs

.PHONY: test-cljs
