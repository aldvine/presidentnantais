import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
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

	XPathExpression expression;
	XPathFactory xpf;
	XPath path;

	public void read(Element root) {
		this.xpf = XPathFactory.newInstance();
		this.path = this.xpf.newXPath();

		// version de xpath 1 donc = au lieu de eq
		this.compile(
				"/export/acteurs/acteur[./etatCivil/infoNaissance/villeNais/text() = 'Nantes'  and count(./mandats/mandat/infosQualite/codeQualite[text() = 'Pr\u00e9sident']) ]");
		this.pn = (NodeList)this.queryNL(root);

		for (int i = 0; i < this.pn.getLength(); i++) {

			this.compile("/acteur");
			// Element el = (Element) nodes.item(i);
			System.out.println(this.pn.item(i).getFirstChild().getNodeValue());
			Node nom = this.queryNode((Element)this.pn.item(i));
			System.out.println("\nnom");
			System.out.println(nom);
			System.out.println("finnom");

			// String personne = "<personne nom=\"" + this.acteur.nom + " " +
			// this.acteur.prenom + "\">";
			// System.out.println(personne);
			// String md;
			// for (Mandat unMandat : this.acteur.mandats) {

			// md = "<md ";
			// md += "code=\"" + unMandat.organeRef + "\" ";
			// if (unMandat.dateDebut != null && !unMandat.dateDebut.isEmpty()) {
			// md += "d\u00e9but=\"" + unMandat.dateDebut + "\" ";
			// }
			// if (unMandat.dateFin != null && !unMandat.dateFin.isEmpty()) {
			// md += "fin=\"" + unMandat.dateFin + "\" ";
			// }
			// md += "legislature=\"" + unMandat.legislature + "\" ";
			// if (unMandat.datePublication != null && !unMandat.datePublication.isEmpty())
			// {
			// md += "pub=\"" + unMandat.datePublication + "\" ";
			// }
			// md += ">";

			// this.organe = new Organe();
			// // recherche du libelle de l'organe
			// // this.readOrganes(dom.doc.getDocumentElement().getFirstChild(), unMandat);
			// md += this.organe.libelle;
			// md += "</md>";
			// System.out.println(md);
			// }

			// System.out.println("</personne>");
		}

	}

	public NodeList queryNL(Node root) {
		try {
			NodeList res = (NodeList) this.expression.evaluate(root, XPathConstants.NODESET);
			return res;
		} catch (XPathExpressionException e) {
			return null;
		}
	};

	public Node queryNode(Node root) {
		try {
			Node res = (Node) this.expression.evaluate(root, XPathConstants.NODE);
			return res;
		} catch (XPathExpressionException e) {
			return null;
		}
	};

	public void compile(String expr) {
		try {
			this.expression = this.path.compile(expr);
		} catch (XPathExpressionException e) {

			e.printStackTrace();
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
