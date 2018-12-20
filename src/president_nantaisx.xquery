xquery version "1.0" encoding "utf-8";


declare variable $acteursNantais := <acteurnantais>{ doc("AMO30_tous_acteurs_tous_mandats_tous_organes_historique.xml")
    /export/acteurs/acteur[./etatCivil/infoNaissance/villeNais/text() eq "Nantes" ]}
    </acteurnantais>;

declare variable $acteursNantaisPresident :=<acteur>
{
for $acteur in $acteursNantais/acteur[count(./mandats/mandat/infosQualite/codeQualite/text() eq "Président")]
return <president>$acteur/acteur</president>
}</acteur>;
    <test>{$acteursNantaisPresident/president}</test>