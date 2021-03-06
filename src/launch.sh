#!/bin/bash
echo "--------------------------"
# dom Xpath
echo "Compilation du fichier Dom avec Xpath"
javac "DomXpath.java"
echo "Execution Dom avec xpath avec calcul du temps d'execution"
/usr/bin/time -f %E java DomXpath > domxpath.xml 
echo "--------------------------"
# # sax
echo "Compilation du fichier Sax"
javac Sax.java
echo "Execution du fichier avec calcul du temps d'execution"
/usr/bin/time -f %E java Sax > sax.xml
echo "--------------------------"
# # dom sans xpath
echo "Compilation du fichier avec Dom"
javac readDom.java
echo "Execution du fichier avec calcul du temps d'execution"
/usr/bin/time -f %E java ReadDom > dom.xml
echo "--------------------------"
# # xslt
touch xslt.xml
echo "Execution avec XSLT"
/usr/bin/time -f %E java -jar saxon9.jar -xsl:nantais_president.xsl -s:AMO30_tous_acteurs_tous_mandats_tous_organes_historique.xml -o:xslt.xml
echo "--------------------------"
# xquery
touch xquery.xml
echo "Execution avec xquery"
/usr/bin/time -f %E java -cp saxon9.jar net.sf.saxon.Query -q:president_nantaisx.xquery -o:xquery.xml
echo "--------------------------"
