xquery version "1.0" encoding "utf-8";
<nantais>
{
    (: La requete retourne un résultat pour chaque acteur nantais s'il a au moins un mandat de président :)
for $acteurNantais in  doc("AMO30_tous_acteurs_tous_mandats_tous_organes_historique.xml")/export/acteurs/acteur[./etatCivil/infoNaissance/villeNais/text() eq "Nantes" ]
let $presidentNantais := $acteurNantais/count(./mandats/mandat/infosQualite/codeQualite[text() eq "Président"]) 
where $presidentNantais ge 1
order by $acteurNantais/etatCivil/ident/nom
return 
    <personne nom='{$acteurNantais/etatCivil/ident/prenom/text()} {$acteurNantais/etatCivil/ident/nom/text()}'>
        {  (: Pour chaque mandat de l'acteur, on on crée un élement mandat dans l'élement personne avec les attributs correspondants :)
        for $mandat in $acteurNantais/mandats/mandat[./infosQualite/codeQualite[text() eq "Président"]]
        order by $mandat/dateDebut/translate(text(), '-', '')
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
