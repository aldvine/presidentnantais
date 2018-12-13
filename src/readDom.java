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
		System.out.println("<nantais>");
		if (n.nodeType()==Node.ELEMENT_NODE) {
			Element el = (Element)n;
			if (el.tagName().equals("acteurs")){
				NodeList nl = n.childNodes();
				for(int i=0;i<nl.length();i++) {
					
					this.acteur = new Acteur();
					this.acteur.isNantais = false;
					this.acteur.isPresident = false;
					this.acteur.mandats = new ArrayList<>();
					
					this.readActeur(nl.item(i));
					
					if (this.acteur.isPresident && this.acteur.isNantais) {

						String personne = "<personne nom=\"" + this.acteur.prenom + " " + this.acteur.nom + "\">";
						System.out.println(personne);
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
							this.readOrganes(dom.doc.getDocumentElement().getChildNodes().item(0), unMandat);
							md += this.organe.libelle;
							md += "</md>";
							System.out.println(md);
						}
						personne += "</personne>";
						System.out.println(personne);
					}
				}
			}else {
				this.read(n.getNextSibling());
			}
		}
		System.out.println("</nantais>");
	}
	
	public void readOrganes(Node n, Mandat unMandat) {
		Element el = (Element)n;
		if (el.tagName().equals("organes")){
			this.readOrgane(n.childNodes().item(0), unMandat);
		}
		if (n.nextSibling()!=null){
			this.readOrganes(n.nextSibling());
		}
	}
	
	public void readOrgane(Node n, Mandat unMandat) {
		Element el = (Element)n;
		if (el.tagName().equals("uid") && n.nodeValue().equals(unMandat.organeRef) {
			this.organe.code = n.nodeValue();
		}
		if (el.tagName().equals("libelle") && this.organe.code.equals(unMandat.organeRef) {
			this.organe.libelle = n.nodeValue();
		}
		if (n.nextSibling()!=null){
			this.readOrgane(n.nextSibling());
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
			this.readActeur(n.nextSibling());
		}
	}
	
	public void readEtatCivil(Node n) {
		Element el = (Element)n;
		if (el.tagName().equals("ident")){
			this.readIdent(n.childNodes().item(0));
		}
		if (el.tagName().equals("infoNaissance")) {
			this.readInfoNaissance(n.childNodes().item(0));
		}
		if (n.nextSibling()!=null){
			this.readEtatCivil(n.nextSibling());
		}
	}
	
	public void readIdent(Node n) {
		Element el = (Element)n;
		if (el.tagName().equals("prenom")){
			this.acteur.nom = n.nodeValue();
		}
		if (el.tagName().equals("nom")) {
			this.acteur.prenom = n.nodeValue();
		}
		if (n.nextSibling()!=null){
			this.readIdent(n.nextSibling());
		}
	}
	
	public void readInfoNaissance(Node n) {
		Element el = (Element)n;
		if (el.tagName().equals("villeNais") && n.nodeValue().equals("Nantes")){
			this.acteur.isNantais = true;
		}
		if (n.nextSibling()!=null){
			this.readInfoNaissance(n.nextSibling());
		}
	}
	
	public void readMandat(Node n) {
		Element el = (Element)n;
		if (el.tagName().equals("dateDebut")){
			this.mandat.dateDebut = n.nodeValue();
		}
		if (el.tagName().equals("datePublication")){
			this.mandat.datePub = n.nodeValue();
		}
		if (el.tagName().equals("dateFin")){
			this.mandat.dateFin = n.nodeValue();
		}
		if (el.tagName().equals("legislature")){
			this.mandat.legislature = n.nodeValue();
		}
		if (el.tagName().equals("infosQualite")){
			this.mandat.president = false;
			this.readInfosQualite(n.childNodes().item(0));
		}
		if (el.tagName().equals("organes")){
			this.mandat.organeRef = n.firstChild().nodeValue();
		}
		if (n.nextSibling()!=null){
			this.readMandat(n.nextSibling());
		}
	}
	
	public void readInfosQualite(Node n) {
		Element el = (Element)n;
		if (el.tagName()=="codeQualite" && n.nodeValue.equals("Président")){
			this.mandat.president = true;
			this.acteur.isPresident = true;
		}
		if (n.nextSibling()!=null){
			this.readInfosQualite(n.nextSibling());
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
