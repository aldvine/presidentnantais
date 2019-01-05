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

	// traitement principal du programme
	public void read(Node n, ReadDom dom) {

		if (n.getNodeType() == Node.ELEMENT_NODE) {
			Element el = (Element) n;
			if (el.getTagName().equals("acteurs")) {
				NodeList nl = n.getChildNodes();
				// parcours de tout acteurs
				for (int i = 0; i < nl.getLength(); i++) {

					// initialisation des variables
					this.acteur = new Acteur();
					this.acteur.isNantais = false;
					this.acteur.isPresident = false;
					this.acteur.mandats = new ArrayList<>();
					// lecture de l'acteur
					this.readActeur(nl.item(i).getFirstChild());

					// si c'est un acteur né à nantes et qu'il a été président on l'ajoute dans le fichier final avec tous ses mandats
					if (this.acteur.isPresident && this.acteur.isNantais) {
						String personne = "\u0009<personne nom=\"" + this.acteur.prenom + " " + this.acteur.nom + "\">";
						System.out.println(personne);
						String md;
						
						for (Mandat unMandat : this.acteur.mandats) {

							md = "\u0009\u0009<md ";
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

							this.organe = new Organe();
							// recherche du libelle de l'organe
							this.readOrganes(dom.doc.getDocumentElement().getFirstChild(), unMandat);
							md += this.organe.libelle;
							md += "\n\u0009\u0009</md>";
							System.out.println(md);
						}

						System.out.println( "\u0009</personne>");
					}
				}
			} else {
				if (n.getNextSibling() != null) {
					this.read(n.getNextSibling(), dom);
				}
			}
		}
	}

	// fonction permettant de récupérer le noeud organes
	public void readOrganes(Node n, Mandat unMandat) {
		Element el = (Element) n;

		if (el.getTagName().equals("organes") && n.getFirstChild() != null) {
			this.readNextOrgane(n.getFirstChild(), unMandat);
		}
		if (n.getNextSibling() != null) {
			this.readOrganes(n.getNextSibling(), unMandat);
		}
	}
	
	// récupération des noeuds organes
	public void readNextOrgane(Node n, Mandat unMandat) {
		Element el = (Element) n;
		
		if (el.getTagName().equals("organe") && n.getFirstChild() != null) {
			this.readOrgane(n.getFirstChild(), unMandat);
		}
		if (n.getNextSibling() != null) {
			this.readNextOrgane(n.getNextSibling(), unMandat);
		}
	}
	// récuperation des infos d'un organe
	public void readOrgane(Node n, Mandat unMandat) {
		Element el = (Element) n;

		if (el.getTagName().equals("uid") && n.getFirstChild() != null) {
				this.organe.code = n.getFirstChild().getNodeValue();
		}

		if (el.getTagName().equals("libelle") && this.organe.code.equals(unMandat.organeRef)) {
			
			if (n.getFirstChild() != null) {

				this.organe.libelle = n.getFirstChild().getNodeValue();
				
			}
		}
		if (n.getNextSibling() != null) {
			this.readOrgane(n.getNextSibling(), unMandat);
		}
	}

	//récuperation des infos de l'acteur
	public void readActeur(Node n) {
		Element el = (Element) n;
		if (el.getTagName().equals("etatCivil")) {
			this.readEtatCivil(n.getFirstChild());
		}
		if (el.getTagName().equals("mandats")) {
			NodeList nl = n.getChildNodes();
			for (int i = 0; i < nl.getLength(); i++) {
				this.mandat = new Mandat();
				this.readMandat(nl.item(i).getFirstChild());
				if(this.mandat.president){
					this.acteur.mandats.add(this.mandat);
				}
			}

		}
		if (n.getNextSibling() != null) {
			this.readActeur(n.getNextSibling());
		}
	}
	
	// récupération des infos de l'état civil d'un acteur
	public void readEtatCivil(Node n) {
		Element el = (Element) n;

		if (el.getTagName().equals("ident") && n.getFirstChild() != null) {
			this.readIdent(n.getFirstChild());
		}
		if (el.getTagName().equals("infoNaissance") && n.getFirstChild() != null) {

			this.readInfoNaissance(n.getFirstChild());
		}
		if (n.getNextSibling() != null) {
			this.readEtatCivil(n.getNextSibling());
		}
	}

	// récuperation de l'identité d'un acteur
	public void readIdent(Node n) {
		Element el = (Element) n;
		if (el.getTagName().equals("prenom")) {
			this.acteur.nom = n.getFirstChild().getNodeValue();
		}
		if (el.getTagName().equals("nom")) {
			this.acteur.prenom = n.getFirstChild().getNodeValue();
		}
		if (n.getNextSibling() != null) {
			this.readIdent(n.getNextSibling());
		}
	}

	// récupération de la ville naissance et vérification si nantais
	public void readInfoNaissance(Node n) {
		Element el = (Element) n;

		if (el.getTagName().equals("villeNais") && n.getFirstChild() != null) {
			if (n.getFirstChild().getNodeValue().equals("Nantes")) {
				this.acteur.isNantais = true;
			}

		}
		if (n.getNextSibling() != null) {
			this.readInfoNaissance(n.getNextSibling());
		}
	}

	// récuperation des infos des mandats
	public void readMandat(Node n) {
		Element el = (Element) n;
		if (el.getTagName().equals("dateDebut") && n.getFirstChild() != null) {
			this.mandat.dateDebut = n.getFirstChild().getNodeValue();

		}
		if (el.getTagName().equals("datePublication") && n.getFirstChild() != null) {
			this.mandat.datePublication = n.getFirstChild().getNodeValue();

		}
		if (el.getTagName().equals("dateFin") && n.getFirstChild() != null) {
			this.mandat.dateFin = n.getFirstChild().getNodeValue();

		}
		if (el.getTagName().equals("legislature") && n.getFirstChild() != null) {
			this.mandat.legislature = n.getFirstChild().getNodeValue();

		}
		if (el.getTagName().equals("infosQualite") && n.getFirstChild() != null) {

			this.mandat.president = false;
			this.readInfosQualite(n.getFirstChild());
		}
		if (el.getTagName().equals("organes") && n.getFirstChild() != null) {
		

			if (n.getFirstChild().getFirstChild() != null) {
				this.mandat.organeRef = n.getFirstChild().getFirstChild().getNodeValue();
			}
		}
		if (n.getNextSibling() != null) {
			this.readMandat(n.getNextSibling());
		}

	}

	// récupération du code qualité du mandat et verification si president 
	public void readInfosQualite(Node n) {
		Element el = (Element) n;
		if (el.getTagName() == "codeQualite" && n.getFirstChild() != null) {
			if (n.getFirstChild().getNodeValue().equals("Pr\u00e9sident")) {
				this.mandat.president = true;
				this.acteur.isPresident = true;
			}
		}
		if (n.getNextSibling() != null) {
			this.readInfosQualite(n.getNextSibling());
		}
	}

	// chargement du fichier XML à traiter
	public void load(String fichier) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setIgnoringComments(true);
			dbf.setIgnoringElementContentWhitespace(true);
			dbf.setNamespaceAware(true);

			// dbf.setSchema("histo.dtd");
			dbf.setValidating(true);
			dbf.setXIncludeAware(true);
			DocumentBuilder db = dbf.newDocumentBuilder();

			doc = db.parse(fichier);
		} catch (Exception e) {
			System.out.println("Exception !");
			System.exit(0);
		}
	}

	public static void main(String argv[]) {
		ReadDom dom = new ReadDom();
		dom.load("AMO30_tous_acteurs_tous_mandats_tous_organes_historique.xml");
		System.out.println("<nantais>");
		dom.read(dom.doc.getDocumentElement().getFirstChild(), dom);
		System.out.println("</nantais>");
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
