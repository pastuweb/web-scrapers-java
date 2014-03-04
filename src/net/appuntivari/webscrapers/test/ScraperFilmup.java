package net.appuntivari.webscrapers.test;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class ScraperFilmup {

	public static void main(String[] args) throws Exception {
		
		WebClient browser = new WebClient();
		browser.setJavaScriptEnabled(false);
		
		HtmlPage page = browser.getPage("http://filmup.leonardo.it/sc_starwars3.htm");
		
		
		System.out.println("letta pagina");
		
		DomNodeList<HtmlElement> trs = page.getElementsByTagName("tr");
		String titoloOriginale =null;
		String nazione=null;
		String anno=null;
		String durata=null;
		String regia=null;
		String sitoUfficiale=null;
		String sitoItaliano=null;
		String cast=null;
		String produzione=null;
		String distribuzione=null;
		String uscita=null;
		
		for (HtmlElement tr : trs) {
			DomNodeList<HtmlElement> tds = tr.getElementsByTagName("td");

			for (int i = 0; i < tds.size(); i++) {
				if(tds.get(i).getTextContent()!=null &&	tds.get(i).getTextContent().contains("Titolo originale:"))
					titoloOriginale = tds.get(i+1).getTextContent().trim();

				if(tds.get(i).getTextContent()!=null &&	tds.get(i).getTextContent().contains("Nazione:"))
					nazione = tds.get(i+1).getTextContent().trim();
			
				if(tds.get(i).getTextContent()!=null &&	tds.get(i).getTextContent().contains("Anno:"))
					anno = tds.get(i+1).getTextContent().trim();
			
				if(tds.get(i).getTextContent()!=null &&	tds.get(i).getTextContent().contains("Durata:"))
					durata = tds.get(i+1).getTextContent().trim();
			
				if(tds.get(i).getTextContent()!=null &&	tds.get(i).getTextContent().contains("Regia:"))
					regia = tds.get(i+1).getTextContent().trim();
			
				if(tds.get(i).getTextContent()!=null &&	tds.get(i).getTextContent().contains("Sito ufficiale:"))
					sitoUfficiale = tds.get(i+1).getTextContent().trim();
			
				if(tds.get(i).getTextContent()!=null &&	tds.get(i).getTextContent().contains("Sito italiano:"))
					sitoItaliano = tds.get(i+1).getTextContent().trim();
			
				if(tds.get(i).getTextContent()!=null &&	tds.get(i).getTextContent().contains("Cast:"))
					cast = tds.get(i+1).getTextContent().trim();
			
				if(tds.get(i).getTextContent()!=null &&	tds.get(i).getTextContent().contains("Produzione:"))
					produzione = tds.get(i+1).getTextContent().trim();
			
				if(tds.get(i).getTextContent()!=null &&	tds.get(i).getTextContent().contains("Distribuzione:"))
					distribuzione = tds.get(i+1).getTextContent().trim();
			
				if(tds.get(i).getTextContent()!=null &&	tds.get(i).getTextContent().contains("Uscita prevista:"))
					uscita = tds.get(i+1).getTextContent().trim();
			
			
			}

		}
		
		
		System.out.println("titolo originale: "+titoloOriginale);
		System.out.println("nazione         : "+nazione);
		System.out.println("anno            : "+anno);
		System.out.println("durata          : "+durata);
		System.out.println("regia           : "+regia);
		System.out.println("sitoUfficiale   : "+sitoUfficiale);
		System.out.println("sitoItaliano    : "+sitoItaliano);
		System.out.println("cast            : "+cast);
		System.out.println("produzione      : "+produzione);
		System.out.println("distribuzione   : "+distribuzione);
		System.out.println("uscita          : "+uscita);

	}
}
