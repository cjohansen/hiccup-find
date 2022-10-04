hiccup-find.jar: src/hiccup_find/*
	rm -f hiccup-find.jar && clojure -M:jar

deploy: hiccup-find.jar
	mvn deploy:deploy-file -Dfile=hiccup-find.jar -DrepositoryId=clojars -Durl=https://clojars.org/repo -DpomFile=pom.xml

.PHONY: deploy
