package net.appuntivari.webscrapers.test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class TestCameraUNI {
	
public static void main(String[] args) throws Throwable {
		
		WebClient browser = new WebClient(BrowserVersion.FIREFOX_3);
		browser.setJavaScriptEnabled(false);		
		HtmlPage page = browser.getPage("http://elezionistorico.interno.it/index.php?tpel=C&dtel=27/03/1994&tpa=I&tpe=L&lev0=0&levsut0=0&lev1=1&levsut1=1&lev2=1&levsut2=2&ne1=1&ne2=101&es0=S&es1=S&es2=S&ms=S");
		

		DomNodeList<HtmlElement> tables = page.getElementsByTagName("table");
		int t = 0;
		for (HtmlElement table : tables) {
			System.out.println("tabella#: "+ t);
			System.out.println("tabella#: "+ table.getTextContent());
			DomNodeList<HtmlElement> trs = table.getElementsByTagName("tr");
			int n=0;
			for(HtmlElement tr : trs ){
				System.out.println("rig#: "+ n);
				System.out.println("rig#: "+ tr.getTextContent());
				n++;
			}
			t++;
		}
		
		
	}

}
