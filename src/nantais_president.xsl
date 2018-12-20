<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:transform xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
    
   <xsl:output method="xml" indent="yes" doctype-system="ex.dtd"/>
    
    <xsl:template match="/export">
        <nantais>
            <xsl:apply-templates
                select="./acteurs/acteur[./etatCivil/infoNaissance/villeNais eq 'Nantes' and
                count(./mandats/mandat/infosQualite/codeQualite[text() eq 'Pr�sident']) ge 1
                ]"
            />
        </nantais>
    </xsl:template>
    
   <xsl:template match="acteur">
       <xsl:element name="personne">
           <xsl:attribute name="nom" select="concat(./etatCivil/ident/prenom,' ',./etatCivil/ident/nom)"></xsl:attribute>
           <xsl:apply-templates select="./mandats/*[./infosQualite/codeQualite/text() eq 'Pr�sident']"></xsl:apply-templates>
       </xsl:element>
    </xsl:template>
    
    <xsl:template match="mandat">
        <xsl:element name="md">
           
            <xsl:attribute name="code" select="./organes/organeRef/text()"></xsl:attribute>
            <xsl:if test="exists(./dateDebut/text())">
                <xsl:attribute name="d�but" select="./dateDebut/text()"></xsl:attribute>
            </xsl:if>
            <xsl:if test="exists(./dateFin/text())">
                <xsl:attribute name="fin" select="./dateFin/text()"></xsl:attribute>
            </xsl:if>
            <xsl:attribute name="legislature" select="./legislature/text()"></xsl:attribute>
            <xsl:if test="exists(./datePublication/text())">
                <xsl:attribute name="pub" select="./datePublication/text()"></xsl:attribute>
            </xsl:if>
            <xsl:apply-templates select="/export/organes/organe[./uid eq current()/organes/organeRef/text()]"></xsl:apply-templates>
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="organe">       
        <xsl:value-of select="./libelle/text()"/>
    </xsl:template>
    
</xsl:transform>