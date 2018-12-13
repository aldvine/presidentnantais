import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import Organe.java;
import Acteur.Java;
import Mandat mandat;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

class readDom {
	public Document doc;
	Organe organe;
    Acteur acteur;
    Mandat mandat;
	
	public void read(Node n) {
		if (n.nodeType()==Node.ELEMENT_NODE) {
			Element el = (Element)n;
			if (el.tagName().equals("acteurs")){
				NodeList nl = n.childNodes();
				for(int i=0;i<nl.length();i++) {
					this.acteur = new Acteur();
					this.acteur.mandats = new ArrayList<>();
					this.readActeur(nl.item(i));
				}
			}else {
				this.read(n.getNextSibling());
			}
		}
	}
	
	public void readActeur(Node n) {
		Element el = (Element)n;
		if (el.tagName().equals("etatCivil")){
			this.readEtatCivil(n.childNodes().item(0));
		}
		if (el.tagName().equals("mandats")) {
			NodeList nl = n.childNodes();
			for(int i=0;i<nl.length();i++) {
				this.mandat = new Mandat();
				this.readMandat(nl.item(i));
				this.acteur.mandats.add(this.mandat);
			}
			this.readMandats(n.childNodes().item(0));
		}
		if (n.nextSibling()!=null){
			this.readActeur(n.getNextSibling());
		}
	}
	
	public void readEtatCivil(Node n) {
		Element el = (Element)n;
		if (el.getTagName().equa"ident"){
			this.readIdent(n.childNodes().item(0));
		}
		if (el.getName()=="infoNaissance") {
			this.readInfoNaissance(n.childNodes().item(0));
		}
		if (n.getNextSibling()!=null){
			this.readActeur(n.nextSibling());
		}
	}
	
	public void readIdent(Node n) {
		Element el = (Element)n;
		if (el.getTagName()=="prenom"){
			this.acteur.nom = n.nodeValue();
		}
		if (el.getName()=="nom") {
			this.acteur.prenom = n.nodeValue();
		}
		if (n.getNextSibling()!=null){
			this.readActeur(n.getNextSibling());
		}
	}
	
	public void readInfoNaissance(Node n) {
		Element el = (Element)n;
		if (el.getTagName()=="villeNais"){
			this.acteur.dateNaissance = n.nodeValue();
		}
		if (n.getNextSibling()!=null){
			this.readActeur(n.getNextSibling());
		}
	}
	
	public void readMandat(Node n) {
		Element el = (Element)n;
		if (el.getTagName()=="dateDebut"){
			this.mandat.dateDebut = n.nodeValue();
		}
		if (el.getTagName()=="datePublication"){
			this.mandat.datePub = n.nodeValue();
		}
		if (el.getTagName()=="dateFin"){
			this.mandat.dateFin = n.nodeValue();
		}
		if (el.getTagName()=="legislature"){
			this.mandat.legislature = n.nodeValue();
		}
		if (el.getTagName()=="infosQualite"){
			this.readInfosQualite(n.getChildNodes().item(0));
		}
		if (n.getNextSibling()!=null){
			this.readActeur(n.getNextSibling());
		}
	}
	
	public void readInfosQualite(Node n) {
		Element el = (Element)n;
		if (el.getTagName()=="codeQualite" && n.nodeValue.equals("Président")){
			this.mandat.president = true;
			this.acteur.isPresident = true;
		}
		if (n.getNextSibling()!=null){
			this.readActeur(n.getNextSibling());
		}
	}
	
	public void load(String fichier) {
	  try {
	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    DocumentBuilder db = dbf.newDocumentBuilder();
	    doc = db.parse(fichier);
	  } catch(Exception e) {System.out.println("Exception !");System.exit(0);}
	}
	
	public static void main(String argv[]) {
	  readDom dom = new readDom();
	  dom.load("AMO30_tous_acteurs_tous_mandats_tous_organes_historique.xml");
	  dom.read(dom.doc.getDocumentElement().getChildNodes().item(0)));
	}
}

class Mandat {
    public String dateDebut;
    public String datePub;
    public String dateFin;
    public String legislature;
    public Boolean president = false;
}

class Acteur {
    public String nom;
    public String prenom;
    public String dateNaissance;
    public ArrayList<Mandat> mandats;
    public Boolean isPresident = false;
}

class Organe {
    public String code;
    public String libelle;

}
