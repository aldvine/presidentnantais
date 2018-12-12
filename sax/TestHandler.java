import src.*;
import java.io.File;
import java.util.ArrayList;

import Organe.java;
// Paquetages SAX
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

// Description du gestionnaire d'événements spécifique
public class TestHandler extends DefaultHandler {
    // Mise en place de l'analyseur et insertion du gestionnaire spécifique
    Boolean inOrgane = false;
    Boolean inUid = false;
    Boolean inLibelle = false;
    Boolean inActeur = false;
    Boolean inIdent = false;
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
    Organe organe;
    Acteur acteur;
    Mandat mandat;
    ArrayList<Organe> lesOrganes = new ArrayList<>();
    // Organe lesOrganes = new ArrayList<>();

    public static void main(String[] args) {
        DefaultHandler handler = new TestHandler();
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

    public void afficherOrganes(){
        for (Organe org : this.lesOrganes) {
            System.out.println("organe : " + org.code  + " "+ org.libelle);
        }
        // for (int i = 0; i < this.lesOrganes.size(); i++) {
        //     System.out.println("organe : " + this.lesOrganes.get(i).code  + " "+ this.lesOrganes.get(i).libelle);

        // }
    }

    // Méthodes surchargées
    public void startDocument() throws SAXException {
        System.out.println("<nantais>");
    }

    public void endDocument() throws SAXException {
        // System.out.println("endDocument");
        System.out.println("</nantais>");
        this.afficherOrganes();
    }
    
    public void characters(char[] caracteres, int debut, int longueur)  throws SAXException{
        String donnees = new String(caracteres, debut, longueur);
        //Traitement Organe
        if(this.inUid){
            this.organe.code =donnees;
        }
        if(this.inLibelle){
            this.organe.libelle =donnees;
        }

        //Traitement Acteur
        if(this.inNom){
            this.acteur.nom =donnees;
        }
        if(this.inPrenom){
            this.acteur.prenom =donnees;
        }
        if(this.inVilleNais){
            this.acteur.villeNais =donnees;
        }

        //Traitement Mandat
        if(this.inDateDebut){
            this.mandat.dateDebut =donnees;
        }
        if(this.inDatePublication){
            this.mandat.datePublication =donnees;
        }
        if(this.inDateFin){
            this.mandat.dateFin =donnees;
        }
        if(this.inCodeQualite && donnees.equals("Président")){
            this.mandat.president =true;
            this.acteur.isPresident = true;
        }
        if(this.inLegislature){
            this.mandat.legislature =donnees;
        }
    }

    public void startElement(String namespaceURI, String sName, String qName, Attributes attrs) throws SAXException {
        //Traitement organe
        if (sName.equals("organe")) {
            this.inOrgane = true;
            this.organe = new Organe();
        }
        if (sName.equals("uid") && inOrgane) {
            this.inUid=true;
        }
        if (sName.equals("libelle") && inOrgane) {
            this.inLibelle=true;
        }

        //Traitement acteur
        if (sName.equals("acteur")) {
            this.inActeur = true;
            this.acteur = new Acteur();
            this.acteur.mandats = new ArrayList<>();
        }
        if (sName.equals("identite") && inActeur) {
            this.inIdentite=true;
        }
        if (sName.equals("prenom") && inIdentite) {
            this.inPrenom=true;
        }
        if (sName.equals("nom") && inIdentite) {
            this.inNom=true;
        }

        if (sName.equals("infoNaissance") && inActeur) {
            this.inInfoNaissance=true;
        }
        if (sName.equals("villeNais") && inInfoNaissance) {
            this.inVilleNais=true;
        }

        //Traitement Mandat
        if (sName.equals("mandat") && inActeur) {
            this.inMandat=true;
            this.mandat = new Mandat();
        }
        if (sName.equals("dateDebut") && inMandat) {
            this.inDateDebut=true;
        }
        if (sName.equals("datePublication") && inMandat) {
            this.inDatePublication=true;
        }
        if (sName.equals("dateFin") && inMandat) {
            this.inDateFin=true;
        }
        if (sName.equals("codeQualite") && inMandat) {
            this.inCodeQualite=true;
        }
        if (sName.equals("legislature") && inMandat) {
            this.inLegislature=true;
        }

    }

    public void endElement(String namespaceURI, String sName, String qName) throws SAXException {
        //Traitement organe
        if (sName.equals("organe")) {
            this.inOrgane = false;
            this.lesOrganes.add(this.organe);
        }
        if (sName.equals("uid") ) {
            this.inUid=false;
        }
        if (sName.equals("libelle") ) {
            this.inLibelle=false;
        }

        //Traitement acteur
        if (sName.equals("acteur")) {
            this.inActeur = false;

            if(this.acteur.isPresident){
                
            }
            //A compléter traitement acteur
        }
        if (sName.equals("identite")) {
            this.inIdentite=false;
        }
        if (sName.equals("prenom")) {
            this.inPrenom=false;
        }
        if (sName.equals("nom")) {
            this.inNom=false;
        }

        if (sName.equals("infoNaissance")) {
            this.inInfoNaissance=false;
        }
        if (sName.equals("villeNais")) {
            this.inVilleNais=false;
        }

        //Traitement Mandat
        if (sName.equals("mandat")) {
            this.inMandat=false;
            this.acteur.mandats.add(this.mandat);
        }
        if (sName.equals("dateDebut")) {
            this.inDateDebut=false;
        }
        if (sName.equals("datePublication")) {
            this.inDatePublication=false;
        }
        if (sName.equals("dateFin")) {
            this.inDateFin=false;
        }
        if (sName.equals("codeQualite")) {
            this.inCodeQualite=false;
        }
        if (sName.equals("legislature")) {
            this.inLegislature=false;
        }

    }

}
