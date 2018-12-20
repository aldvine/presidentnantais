xquery version "1.0" encoding "utf-8";
<nantais>
{for $acteurNantais in  doc("AMO30_tous_acteurs_tous_mandats_tous_organes_historique.xml")/export/acteurs/acteur[./etatCivil/infoNaissance/villeNais/text() eq "Nantes" ]
let $presidentNantais := $acteurNantais/count(./mandats/mandat/infosQualite/codeQualite[text() eq "Président"]) 
where $presidentNantais ge 1
return 
    <personne nom='{$acteurNantais/etatCivil/ident/nom/text()}' prenom='{$acteurNantais/etatCivil/ident/prenom/text()}'>
        {for $mandat in $acteurNantais/mandats/mandat[./infosQualite/codeQualite[text() eq "Président"]]
         return element md {if ($mandat/organes/organeRef/text() ne "") then attribute code {$mandat/organes/organeRef/text()} else "",
                            if ($mandat/dateDebut/text() ne "") then attribute debut {$mandat/dateDebut/text()} else "",
                            if ($mandat/dateFin/text() ne "") then attribute fin {$mandat/dateFin/text()} else "", 
                            if ($mandat/legislature/text() ne "") then attribute legislature {$mandat/legislature/text()} else "",
                            if ($mandat/datePublication/text() ne "") then attribute pub {$mandat/datePublication/text()} else "",
                            doc('AMO30_tous_acteurs_tous_mandats_tous_organes_historique.xml')/export/organes/organe[./uid/text() eq $mandat/organes/organeRef/text()]/libelle/text()
             }
         }
    </personne>}
</nantais>
