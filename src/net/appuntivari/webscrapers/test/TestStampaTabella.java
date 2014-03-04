package net.appuntivari.webscrapers.test;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class TestStampaTabella {

	public static void main(String[] args) throws Throwable{
		
		WebClient browser = new WebClient();
		HtmlPage page = browser.getPage("http://elezionistorico.interno.it/index.php?tpel=C&dtel=13/04/2008&tpa=I&tpe=A&lev0=0&levsut0=0&es0=S&ms=S");

		DomNodeList<HtmlElement> tables = page.getElementsByTagName("table");
		for (HtmlElement table : tables) {
			if("dati".equals(table.getAttribute("class"))){
				DomNodeList<HtmlElement> trs = table.getElementsByTagName("tr");
				for (HtmlElement tr : trs) {
					DomNodeList<HtmlElement> ths = tr.getElementsByTagName("th");
					for (HtmlElement th : ths) {
						System.out.println("th: "+th.asText());
					}
					DomNodeList<HtmlElement> tds = tr.getElementsByTagName("td");
					for (HtmlElement td : tds) {
						System.out.println("td: "+td.asText());
					}
					System.out.println("==============");
				}
			}
		}
		
	}
}
