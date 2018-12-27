import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.parsers.DocumentBuilder;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

class DomXpath {
	public Document doc;
	NodeList pn;
	Organe organe;
	Acteur acteur;
	Mandat mandat;

	String expression;
	XPathFactory xpf;
	XPath path;

	public void read(Element root) {
		this.xpf = XPathFactory.newInstance();
		this.path = this.xpf.newXPath();

		// version de xpath 1 donc = au lieu de eq
		this.expression = "/export/acteurs/acteur[./etatCivil/infoNaissance/villeNais/text() = 'Nantes'  and count(./mandats/mandat/infosQualite/codeQualite[text() = 'Pr\u00e9sident']) >=1 ]";
		this.pn = (NodeList) this.queryNL(this.expression, root);

		for (int i = 0; i < this.pn.getLength(); i++) {

			// Element el = (Element) nodes.item(i);
			String nom = this.queryString("./etatCivil/ident/nom/text()", this.pn.item(i));
			String prenom = this.queryString("./etatCivil/ident/prenom/text()", this.pn.item(i));

			String personne = "\u0009<personne nom=\"" + prenom + " " + nom + "\">";
			System.out.println(personne);

			// recuperation de tous les mandats

			NodeList mandats = this.queryNL("./mandats/mandat[./infosQualite/codeQualite[text() = 'Pr\u00e9sident']]",
					this.pn.item(i));
			String md;
			for (int j = 0; j < mandats.getLength(); j++) {
				String code = this.queryString("./organes/organeRef/text()", mandats.item(j));
				String deb = this.queryString("./dateDebut/text()", mandats.item(j));
				String fin = this.queryString("./dateFin/text()", mandats.item(j));
				String legislature = this.queryString("./legislature/text()", mandats.item(j));
				String pub = this.queryString("./datePublication/text()", mandats.item(j));

				md = "\u0009\u0009<md ";
				md += "code=\"" + code + "\" ";
				if (deb != null && ! deb.isEmpty()) {
					md += "d\u00e9but=\"" + deb + "\" ";
				}
				if (fin != null && !fin.isEmpty()) {
					md += "fin=\"" +fin + "\" ";
				}

				md += "legislature=\"" + legislature + "\" ";

				if (pub != null && !pub.isEmpty()) {
					md += "pub=\"" + pub + "\" ";
				}
				md += ">";

				// recherche du libelle de l'organe
				// this.readOrganes(dom.doc.getDocumentElement().getFirstChild(), unMandat);
				md += this.queryString("/export/organes/organe[./uid/text() eq "+code+"]/libelle/text()", root) ;
				md += "\n\u0009\u0009</md>";
				System.out.println(md);
			}

			System.out.println("\u0009</personne>");
		}

	}

	public NodeList queryNL(String expr, Node root) {
		try {
			NodeList res = (NodeList) this.path.evaluate(expr, root, XPathConstants.NODESET);
			return res;
		} catch (XPathExpressionException e) {
			return null;
		}
	};

	public Node queryNode(String expr, Node root) {
		try {
			Node res = (Node) this.path.evaluate(expr, root, XPathConstants.NODE);
			return res;
		} catch (XPathExpressionException e) {
			return null;
		}
	};

	public String queryString(String expr, Node root) {
		try {
			String res = (String) this.path.evaluate(expr, root, XPathConstants.STRING);
			return res;
		} catch (XPathExpressionException e) {
			return null;
		}
	};

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
		DomXpath dom = new DomXpath();
		dom.load("AMO30_tous_acteurs_tous_mandats_tous_organes_historique.xml");
		System.out.println("<nantais>");

		dom.read(dom.doc.getDocumentElement());
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
