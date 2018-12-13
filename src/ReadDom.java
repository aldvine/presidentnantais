import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

class ReadDom {
	public Document doc;
	Organe organe;
    Acteur acteur;
    Mandat mandat;
	
	public void read(Node n,ReadDom dom) {
		System.out.println("<nantais>");
		if (n.getNodeType()==Node.ELEMENT_NODE) {
			Element el = (Element)n;
			if (el.getTagName().equals("acteurs")){
				NodeList nl = n.getChildNodes();
				for(int i=0;i<nl.getLength();i++) {
					
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
				this.read(n.getNextSibling(),dom);
			}
		}
		System.out.println("</nantais>");
	}
	
	public void readOrganes(Node n, Mandat unMandat) {
		Element el = (Element)n;
		if (el.getTagName().equals("organes")){
			this.readOrgane(n.getChildNodes().item(0), unMandat);
		}
		if (n.getNextSibling()!=null){
			this.readOrganes(n.getNextSibling(),unMandat);
		}
	}
	
	public void readOrgane(Node n, Mandat unMandat) {
		Element el = (Element)n;
		if (el.getTagName().equals("uid") && n.getNodeValue().equals(unMandat.organeRef)) {
			this.organe.code = n.getNodeValue();
		}
		if (el.getTagName().equals("libelle") && this.organe.code.equals(unMandat.organeRef)) {
			this.organe.libelle = n.getNodeValue();
		}
		if (n.getNextSibling()!=null){
			this.readOrgane(n.getNextSibling(),unMandat);
		}
	}
	
	public void readActeur(Node n) {
		Element el = (Element)n;
		if (el.getTagName().equals("etatCivil")){
			this.readEtatCivil(n.getChildNodes().item(0));
		}
		if (el.getTagName().equals("mandats")) {
			NodeList nl = n.getChildNodes();
			for(int i=0;i<nl.getLength();i++) {
				this.mandat = new Mandat();
				this.readMandat(nl.item(i));
				this.acteur.mandats.add(this.mandat);
			}
			this.readMandat(n.getChildNodes().item(0));
		}
		if (n.getNextSibling()!=null){
			this.readActeur(n.getNextSibling());
		}
	}
	
	public void readEtatCivil(Node n) {
		Element el = (Element)n;
		if (el.getTagName().equals("ident")){
			this.readIdent(n.getChildNodes().item(0));
		}
		if (el.getTagName().equals("infoNaissance")) {
			this.readInfoNaissance(n.getChildNodes().item(0));
		}
		if (n.getNextSibling()!=null){
			this.readEtatCivil(n.getNextSibling());
		}
	}
	
	public void readIdent(Node n) {
		Element el = (Element)n;
		if (el.getTagName().equals("prenom")){
			this.acteur.nom = n.getNodeValue();
		}
		if (el.getTagName().equals("nom")) {
			this.acteur.prenom = n.getNodeValue();
		}
		if (n.getNextSibling()!=null){
			this.readIdent(n.getNextSibling());
		}
	}
	
	public void readInfoNaissance(Node n) {
		Element el = (Element)n;
		if (el.getTagName().equals("villeNais") && n.getNodeValue().equals("Nantes")){
			this.acteur.isNantais = true;
		}
		if (n.getNextSibling()!=null){
			this.readInfoNaissance(n.getNextSibling());
		}
	}
	
	public void readMandat(Node n) {
		Element el = (Element)n;
		if (el.getTagName().equals("dateDebut")){
			this.mandat.dateDebut = n.getNodeValue();
		}
		if (el.getTagName().equals("datePublication")){
			this.mandat.datePublication = n.getNodeValue();
		}
		if (el.getTagName().equals("dateFin")){
			this.mandat.dateFin = n.getNodeValue();
		}
		if (el.getTagName().equals("legislature")){
			this.mandat.legislature = n.getNodeValue();
		}
		if (el.getTagName().equals("infosQualite")){
			this.mandat.president = false;
			this.readInfosQualite(n.getChildNodes().item(0));
		}
		if (el.getTagName().equals("organes")){
			this.mandat.organeRef = n.getFirstChild().getNodeValue();
		}
		if (n.getNextSibling()!=null){
			this.readMandat(n.getNextSibling());
		}
	}
	
	public void readInfosQualite(Node n) {
		Element el = (Element)n;
		if (el.getTagName()=="codeQualite" && n.getNodeValue().equals("Président")){
			this.mandat.president = true;
			this.acteur.isPresident = true;
		}
		if (n.getNextSibling()!=null){
			this.readInfosQualite(n.getNextSibling());
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
	  ReadDom dom = new ReadDom();
	  dom.load("AMO30_tous_acteurs_tous_mandats_tous_organes_historique.xml");
	  dom.read(dom.doc.getDocumentElement().getChildNodes().item(0),dom);
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
