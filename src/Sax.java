import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


// Paquetages SAX
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

// Description du gestionnaire d'événements spécifique
public class Sax extends DefaultHandler {
    // Mise en place de l'analyseur et insertion du gestionnaire spécifique
    Boolean inOrgane = false;
    Boolean inUid = false;
    Boolean inLibelle = false;
    Boolean inActeur = false;
    Boolean inIdentite = false;
    Boolean inNom = false;
    Boolean inPrenom = false;
    Boolean inInfoNaissance = false;
    Boolean inVilleNais = false;
    Boolean inMandats = false;
    Boolean inMandat = false;
    Boolean inDateDebut = false;
    Boolean inDatePublication = false;
    Boolean inDateFin = false;
    Boolean inLegislature = false;
    Boolean inInfosQualite = false;
    Boolean inCodeQualite = false;
    Boolean inOrganeRef = false;
    Organe organe;
    Acteur acteur;
    Mandat mandat;
    ArrayList<Organe> lesOrganes = new ArrayList<>();
    // Organe lesOrganes = new ArrayList<>();

    public static void main(String[] args) {

        DefaultHandler handler = new Sax();
        try {
            long startTime = System.currentTimeMillis();
            XMLReader saxParser = XMLReaderFactory.createXMLReader();
            saxParser.setContentHandler(handler);
            saxParser.setErrorHandler(handler);
            
            saxParser.parse("AMO30_tous_acteurs_tous_mandats_tous_organes_historique.xml");
            long endTime = System.currentTimeMillis();
            System.out.println("That took " + (endTime - startTime) + " milliseconds");

        } catch (Throwable t) {
            t.printStackTrace();
        }
        System.exit(0);
    }
    public String utf8_encode(String chaine){
      
 // TODO
        return chaine;
    }

    public void afficherOrganes() {
        for (Organe org : this.lesOrganes) {
            System.out.println("organe : " + org.code + " " + org.libelle);
        }
        // for (int i = 0; i < this.lesOrganes.size(); i++) {
        // System.out.println("organe : " + this.lesOrganes.get(i).code + " "+
        // this.lesOrganes.get(i).libelle);

        // }
    }

    // Méthodes surchargées
    public void startDocument() throws SAXException {
        System.out.println("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><!DOCTYPE nantais SYSTEM \"ex.dtd\" ><nantais>");
    }

    public void endDocument() throws SAXException {
        // System.out.println("endDocument");
        System.out.println("</nantais>");
        // this.afficherOrganes();
    }

    public void characters(char[] caracteres, int debut, int longueur) throws SAXException {
        String donnees = new String(caracteres, debut, longueur);
        // Traitement Organe
        if (this.inUid && this.inOrgane) {
            this.organe.code = donnees;
        }
        if (this.inLibelle && this.inOrgane) {
            this.organe.libelle = donnees;
        }

        // Traitement Acteur
        if (this.inNom) {
            this.acteur.nom = donnees;
        }
        if (this.inPrenom) {
            this.acteur.prenom = donnees;
        }
        if (this.inVilleNais && donnees.equals("Nantes")) {
            this.acteur.isNantais = true;
        }

        // Traitement Mandat
        if (this.inDateDebut) {
            this.mandat.dateDebut = donnees;
        }

        if (this.inDatePublication) {
            this.mandat.datePublication = donnees;
        }
        if (this.inOrganeRef) {
            this.mandat.organeRef = donnees;
        }
        if (this.inDateFin) {
            this.mandat.dateFin = donnees;
        }
        if (this.inCodeQualite && donnees.equals("Pr\u00e9sident")) {
            this.mandat.president = true;
            this.acteur.isPresident = true;
        }
        if (this.inLegislature) {
            this.mandat.legislature = donnees;
        }
    }

    public void startElement(String namespaceURI, String sName, String qName, Attributes attrs) throws SAXException {
        // Traitement organe
        if (sName.equals("organe")) {
            this.inOrgane = true;
            this.organe = new Organe();
        }
        if (sName.equals("uid") && this.inOrgane) {
            this.inUid = true;
        }
        if (sName.equals("libelle") && this.inOrgane) {
            this.inLibelle = true;
        }

        // Traitement acteur
        if (sName.equals("acteur")) {
            this.inActeur = true;
            this.acteur = new Acteur();
            this.acteur.mandats = new ArrayList<>();
        }
        if (sName.equals("ident") && this.inActeur) {
            this.inIdentite = true;
        }
        if (sName.equals("prenom") && this.inIdentite) {
            this.inPrenom = true;
        }

        if (sName.equals("nom") && this.inIdentite) {
            this.inNom = true;
        }

        if (sName.equals("infoNaissance") && this.inActeur) {
            this.inInfoNaissance = true;
        }
        if (sName.equals("villeNais") && this.inInfoNaissance) {
            this.inVilleNais = true;
        }

        // Traitement Mandat
        if (sName.equals("mandat") && this.inActeur) {
            this.inMandat = true;
            this.mandat = new Mandat();
        }
        if (sName.equals("dateDebut") && this.inMandat) {
            this.inDateDebut = true;
        }
        if (sName.equals("organeRef") && this.inMandat) {
            this.inOrganeRef = true;
        }
        if (sName.equals("datePublication") && this.inMandat) {
            this.inDatePublication = true;
        }
        if (sName.equals("dateFin") && this.inMandat) {
            this.inDateFin = true;
        }
        if (sName.equals("codeQualite") && this.inMandat) {
            this.inCodeQualite = true;
        }
        if (sName.equals("legislature") && this.inMandat) {
            this.inLegislature = true;
        }

    }

    public void endElement(String namespaceURI, String sName, String qName) throws SAXException {
        // Traitement organe
        if (sName.equals("organe")) {
            this.inOrgane = false;
            this.lesOrganes.add(this.organe);
        }
        if (sName.equals("uid")) {
            this.inUid = false;
        }
        if (sName.equals("libelle")) {
            this.inLibelle = false;
        }

        // Traitement acteur
        if (sName.equals("acteur")) {
            this.inActeur = false;

            if (this.acteur.isPresident && this.acteur.isNantais) {
                
                String personne = "<personne nom=\"" + this.acteur.prenom + " " + this.acteur.nom + "\">";
                

                System.out.println(this.utf8_encode(personne));
                String md;
                for (Mandat unMandat : this.acteur.mandats) {
                    md = "<md ";
                    md += "code=\"" + unMandat.organeRef + "\" ";
                    if (unMandat.dateDebut != null && !unMandat.dateDebut.isEmpty()) {
                        md += "d\u00e9but=\"" + unMandat.dateDebut + "\" ";
                    }
                    if (unMandat.dateFin != null && !unMandat.dateFin.isEmpty()) {
                        md += "fin=\"" + unMandat.dateFin + "\" ";
                    }
                    md += "legislature=\"" + unMandat.legislature + "\" ";
                    if (unMandat.datePublication != null && !unMandat.datePublication.isEmpty()) {
                        md += "pub=\"" + unMandat.datePublication + "\" ";
                    }
                    md += ">";
                    // recherche du libelle de l'organe
                    for (Organe org : lesOrganes) {
                        if (org.code.equals(unMandat.organeRef)) {
                            md += org.libelle;
                            break;
                        }
                    }
                    md += "</md>";
                    System.out.println(this.utf8_encode(md));
                }
                personne += "</personne>";
                System.out.println(personne);
            }
        }
        if (sName.equals("ident")) {
            this.inIdentite = false;
        }
        if (sName.equals("prenom")) {
            this.inPrenom = false;
        }
        if (sName.equals("nom")) {
            this.inNom = false;
        }

        if (sName.equals("infoNaissance")) {
            this.inInfoNaissance = false;
        }
        if (sName.equals("villeNais")) {
            this.inVilleNais = false;
        }

        // Traitement Mandat
        if (sName.equals("mandat")) {
            this.inMandat = false;
            // si le mandat est un mandat de président
            if (this.mandat.president) {
                this.acteur.mandats.add(this.mandat);
            }
        }
        if (sName.equals("dateDebut")) {
            this.inDateDebut = false;
        }
        if (sName.equals("organeRef")) {
            this.inOrganeRef = false;
        }
        if (sName.equals("datePublication")) {
            this.inDatePublication = false;
        }
        if (sName.equals("dateFin")) {
            this.inDateFin = false;
        }
        if (sName.equals("codeQualite")) {
            this.inCodeQualite = false;
        }
        if (sName.equals("legislature")) {
            this.inLegislature = false;
        }

    }

}

class Organe {
    public String code;
    public String libelle;
}
class Mandat {
    public String dateDebut;
    public String datePublication;
    public String dateFin;
    public String legislature;
    public String organeRef;
    public Boolean president = false;
}
class Acteur {
    public String nom;
    public String prenom;
    public ArrayList<Mandat> mandats;
    public Boolean isNantais = false;
    public Boolean isPresident = false;
}