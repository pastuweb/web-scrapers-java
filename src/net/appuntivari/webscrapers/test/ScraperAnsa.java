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

public class ScraperAnsa {
	
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		WebClient browser = new WebClient();
		browser.setJavaScriptEnabled(false);

			String url = "http://www.ansa.it/web/notizie/rubriche/cronaca/cronaca_rss.xml";
			
			DateFormat df = new SimpleDateFormat("dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);

			XmlPage xml = browser.getPage(url);
			Document doc = convert(xml);
			
			NodeList listOfItems = doc.getElementsByTagName("item");
			for (int i = 0; i < listOfItems.getLength(); i++) {
				Node firstItemNode = listOfItems.item(i);
	            if(firstItemNode.getNodeType() == Node.ELEMENT_NODE){
	                Element firstItemElement = (Element)firstItemNode;

	                String title = null;
	                String description = null;
	                String link = null;
	                Date pubDate =null;
					String citta=null;
					String testo=null;
					String urlImmagine=null;

					try {
	                	title = ((Element)firstItemElement.getElementsByTagName("title").item(0)).getChildNodes().item(0).getNodeValue().trim();
					} catch (Exception e) {e.printStackTrace();}
					try {
		                description = ((Element)firstItemElement.getElementsByTagName("description").item(0)).getChildNodes().item(0).getNodeValue().trim();
					} catch (Exception e) {e.printStackTrace();}
					try {
		                link = ((Element)firstItemElement.getElementsByTagName("link").item(0)).getChildNodes().item(0).getNodeValue().trim();
					} catch (Exception e) {e.printStackTrace();}
					try {
		                pubDate = df.parse(((Element)firstItemElement.getElementsByTagName("pubDate").item(0)).getChildNodes().item(0).getNodeValue().trim());
					} catch (Exception e) {e.printStackTrace();}

					HtmlPage page = browser.getPage(link);
					HtmlElement content = page.getElementById("content-corpo");
					if(content==null)
						continue;
					String fullContent = clear(content.getTextContent());
					int indexOfMinus = fullContent.indexOf('-');
					if(indexOfMinus<50&&indexOfMinus>0){
						citta = (fullContent.substring(0, indexOfMinus)).trim();
						if(citta.contains(","))
							citta = citta.substring(0, citta.indexOf(",")).trim();
						if(citta.contains(")"))
							citta = citta.substring(0, citta.indexOf(")")).trim();
						if(citta.startsWith(" "))
							citta = citta.substring(1).trim();
						testo = fullContent.substring(indexOfMinus+1).trim();
					}else{
						testo = fullContent;
					}
					if(testo.startsWith(" "))
						testo = testo.substring(1).trim();

					try {
						urlImmagine = content.getAllHtmlChildElements().iterator().next().getAttribute("src");
						if(StringUtils.isNotEmpty(urlImmagine))
							urlImmagine = "http://www.ansa.it"+urlImmagine;
						else
							urlImmagine = null;
					} catch (Exception e) {
						e.printStackTrace();
					}

					
					
					System.out.println(" title      : "+title);
					System.out.println(" description: "+description);
					System.out.println(" link       : "+link);
					System.out.println(" pubDate    : "+pubDate);
					System.out.println(" citta      : "+citta);
					System.out.println(" testo      : "+testo);
					System.out.println(" immagine   : "+urlImmagine);
					System.out.println(" =========== ");
	                
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

	public static String clear(String dirty){
		String tmp = dirty
		.replaceAll("\\(ANSA\\)","")
		.replaceAll("ANSA","")
		.trim()
		.replaceAll("\\n"," ")
		.replaceAll("\\t"," ");
		if(tmp.startsWith("-"))
			tmp = tmp.substring(1);
		return tmp.trim();
	}
}
