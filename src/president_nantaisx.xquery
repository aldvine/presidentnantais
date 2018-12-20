xquery version "1.0" encoding "utf-8";
<nantais>
{for $acteurNantais in  doc("AMO30_tous_acteurs_tous_mandats_tous_organes_historique.xml")/export/acteurs/acteur[./etatCivil/infoNaissance/villeNais/text() eq "Nantes" ]
let $presidentNantais := $acteurNantais/count(./mandats/mandat/infosQualite/codeQualite[text() eq "Président"]) 
where $presidentNantais ge 1
return 
    <personne nom='{$acteurNantais/etatCivil/ident/nom/text()}' prenom='{$acteurNantais/etatCivil/ident/prenom/text()}'>
        {for $mandat in $acteurNantais/mandats/mandat[./infosQualite/codeQualite[text() eq "Président"]]
         return 
            <md code='{$mandat/organes/organeRef/text()}'     
                debut='{$mandat/dateDebut/text()}'
                fin='{$mandat/dateFin/text()}' legislature='{$mandat/legislature/text()}' pub='{$mandat/datePublication/text()}'>
                { doc("AMO30_tous_acteurs_tous_mandats_tous_organes_historique.xml")/export/organes/organe[./uid/text() eq $mandat/organes/organeRef/text()]/libelle/text()}
             </md>
        }
    </personne>}
</nantais>
