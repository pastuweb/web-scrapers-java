package net.appuntivari.webscrapers.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class ScraperGoogle {

	static Map<String,String> urls = new HashMap<String,String>();
	
	public static void main(String[] args) throws Exception {
		
		String ricerca  = "polistes Scents of self site:www.sekj.org filetype:pdf";
		
		WebClient browser = new WebClient();
		browser.setJavaScriptEnabled(false);
		
		HtmlPage searchPage = browser.getPage("http://www.google.it");

		DomNodeList<HtmlElement> inputs = searchPage.getElementsByTagName("input");
		for (HtmlElement input : inputs) {
			if("lst".equals(input.getAttribute("class"))){
				input.setAttribute("value", ricerca);
			}
		}
		HtmlPage resultPage = null;
		for (HtmlElement input : inputs) {
			if("Cerca con Google".equals(input.getAttribute("value"))){
				resultPage = input.click();
			}
		}
		
		if(resultPage!=null){
			readPage(resultPage);			
		}else{
			System.out.println("pagina dei risultati non trovata");
		}

		new File("output/polistes/").mkdirs();
		
		System.out.println("found "+urls.size()+" results");
		
		for (Iterator iterator = urls.keySet().iterator(); iterator.hasNext();) {
			String url = (String) iterator.next();
			String title = urls.get(url);
			System.out.println("wget "+url+" -> "+title);
			
			String code = url.substring(url.lastIndexOf("/"), url.lastIndexOf('.'));
			
			Page pdf = browser.getPage(url);
			byte[] data = pdf.getWebResponse().getContentAsBytes();
			FileOutputStream fw = new FileOutputStream(new File("output/polistes/"+clear(code)+".pdf"));
			fw.write(data);
			fw.close();
			System.out.println("written file: "+"output/polistes/"+clear(code)+".pdf");
			
		}
		
	}
	
	static String clear(String dirty){
		return dirty
		.replaceAll("\\.", "")
		.replaceAll(":", "")
		.replaceAll("\\\\", "").trim();
	}
	
	public static void readPage(HtmlPage resultPage)throws IOException{
		System.out.println("letta pagina: "+resultPage.getTitleText());	
		DomNodeList<HtmlElement> lista = resultPage.getElementsByTagName("li");
		for (HtmlElement li : lista) {
			if("g".equals(li.getAttribute("class"))){
				String link = ((HtmlElement)li.getElementsByTagName("a").item(0)).getAttribute("href");
				String title =((HtmlElement)li.getElementsByTagName("a").item(0)).getTextContent().trim();
				urls.put(link, title);
			}
		}
		
		for (HtmlElement a : resultPage.getElementsByTagName("a")) {
			if("Avanti".equals(a.getTextContent())){
				readPage((HtmlPage)a.click());
			}
		}
		
		
	}
	
}
