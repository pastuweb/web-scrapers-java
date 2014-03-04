package net.appuntivari.webscrapers.test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class TestProvince {

	public static void main(String[] args) throws Throwable {
		
		WebClient browser = new WebClient(BrowserVersion.FIREFOX_3);
		browser.setJavaScriptEnabled(false);		
		HtmlPage page = browser.getPage("http://www.comuni-italiani.it/016/index.html");
		

		DomNodeList<HtmlElement> tables = page.getElementsByTagName("table");
		int n=0;
		for (HtmlElement table : tables) {
			System.out.println("tab#: "+ n);
			System.out.println("tab#: "+ table.getTextContent());
			n++;
		}
		
		
	}
}
