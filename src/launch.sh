#!/bin/bash
# dom Xpath
echo "Compilation du fichier Dom avec Xpath"
javac DomXpath.java
echo "Execution du fichier avec calcul du temps d'execution"
time -f %E java DomXpath > domxpath.xml 
# sax
echo "Compilation du fichier Sax"
javac Sax.java
echo "Execution du fichier avec calcul du temps d'execution"
time -f %E java Sax > sax.xml
# dom sans xpath
echo "Compilation du fichier avec Dom"
javac ReadDom.java
echo "Execution du fichier avec calcul du temps d'execution"
time -f %E java ReadDom > readdom.xml
# xslt
touch xslt.xml
time -f %E java -jar saxon9he.jar -xsl:nantais_president.xsl -s:xslt.xml
