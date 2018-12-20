import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
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

    XPath path;


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
		// dom.read(dom.doc.getDocumentElement().getFirstChild(), dom);
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
