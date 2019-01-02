import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

class DomXpath {
	public Document doc;
	NodeList pn;
	String expression;
	XPathFactory xpf;
	XPath path;

	public void read(Element root) {
		// instanciation de xpath
		this.xpf = XPathFactory.newInstance();
		this.path = this.xpf.newXPath();

		// version de xpath 1 donc = au lieu de eq pour chaque requête
		this.expression = "/export/acteurs/acteur[./etatCivil/infoNaissance/villeNais/text() = 'Nantes'  and count(./mandats/mandat/infosQualite/codeQualite[text() = 'Pr\u00e9sident']) >=1 ]";
		// récuperation des acteurs qui sont nantais et qui ont été président.
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
				md += this.queryString("/export/organes/organe[./uid/text() = '"+code+"']/libelle/text()", root) ;
				md += "\n\u0009\u0009</md>";
				System.out.println(md);
			}

			System.out.println("\u0009</personne>");
		}

	}
	// retourne une liste de noeuds Nodelist
	// expr correspond à la requete 
	// root correspond au noeud racine pour la requête
	public NodeList queryNL(String expr, Node root) {
		try {
			NodeList res = (NodeList) this.path.evaluate(expr, root, XPathConstants.NODESET);
			return res;
		} catch (XPathExpressionException e) {
			return null;
		}
	};

	//retourne un noeud
	public Node queryNode(String expr, Node root) {
		try {
			Node res = (Node) this.path.evaluate(expr, root, XPathConstants.NODE);
			return res;
		} catch (XPathExpressionException e) {
			return null;
		}
	};

	// retourne une chaine de caractère string
	public String queryString(String expr, Node root) {
		try {
			String res = (String) this.path.evaluate(expr, root, XPathConstants.STRING);
			return res;
		} catch (XPathExpressionException e) {
			return null;
		}
	};

	// chargement du fichier source
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
//  chargement du fichier source et lancement du programme 
	public static void main(String argv[]) {
		DomXpath dom = new DomXpath();
		dom.load("AMO30_tous_acteurs_tous_mandats_tous_organes_historique.xml");
		System.out.println("<nantais>");
		dom.read(dom.doc.getDocumentElement());
		System.out.println("</nantais>");
	}
}

