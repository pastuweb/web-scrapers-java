package net.appuntivari.webscrapers.test;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.xml.XmlPage;

public class ScraperAppuntiVari {
	
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		WebClient browser = new WebClient();
		browser.setJavaScriptEnabled(false);

			String url = "http://www.appuntivari.net/home/-/asset_publisher/JL5WlQX1kGr1/rss";
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);

			XmlPage xml = browser.getPage(url);
			Document doc = convert(xml);
			
			NodeList listOfItems = doc.getElementsByTagName("entry");
			for (int i = 0; i < listOfItems.getLength(); i++) {
				Node firstItemNode = listOfItems.item(i);
	            if(firstItemNode.getNodeType() == Node.ELEMENT_NODE){
	                Element firstItemElement = (Element)firstItemNode;

	                String titolo = null;
	                String link = null;
	                String autore=null;
	                Date aggiornatoIl =null;
	                Date pubblicatoIl =null;

					try {
	                	titolo = ((Element)firstItemElement.getElementsByTagName("title").item(0)).getChildNodes().item(0).getNodeValue().toString();
					} catch (Exception e) {e.printStackTrace();}
					try {
		                link = ((Element)firstItemElement.getElementsByTagName("link").item(0)).getAttribute("href").toString();
					} catch (Exception e) {e.printStackTrace();}
					try {
		                autore = ((Element)firstItemElement.getElementsByTagName("author").item(0)).getElementsByTagName("name").item(0).getChildNodes().item(0).getNodeValue().toString();
					} catch (Exception e) {e.printStackTrace();}
					try {
						aggiornatoIl = df.parse(((Element)firstItemElement.getElementsByTagName("updated").item(0)).getChildNodes().item(0).getNodeValue().trim());
					} catch (Exception e) {e.printStackTrace();}
					try {
						pubblicatoIl = df.parse(((Element)firstItemElement.getElementsByTagName("published").item(0)).getChildNodes().item(0).getNodeValue().trim());
					} catch (Exception e) {e.printStackTrace();}

					System.out.println(" ################### ");
					System.out.println(" FEEDS NUOVI APPUNTI ");
					System.out.println(" ################### ");
					System.out.println(" titolo        : "+titolo);
					System.out.println(" link          : "+link);
					System.out.println(" autore        : "+autore);
					System.out.println(" aggiornato il : "+aggiornatoIl);
					System.out.println(" pubblicato il : "+pubblicatoIl);
					System.out.println(" ################### ");
	                
	            }
			}
				
	}
	
	public static Document convert(XmlPage xmlPage) throws Exception {
		DOMSource domSource = new DOMSource(xmlPage);
		StringWriter writer = new StringWriter();
		StreamResult streamResult = new StreamResult(writer);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer serializer = tf.newTransformer();
		serializer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
		serializer.transform(domSource, streamResult);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder parser = factory.newDocumentBuilder();
		return parser.parse(new ByteArrayInputStream(writer.getBuffer().toString().getBytes("ISO-8859-1")));

	}

}
