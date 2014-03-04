package net.appuntivari.webscrapers.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class ScraperFacebook {

	public static void main(String[] args) throws Exception {
		
		WebClient browser = new WebClient(BrowserVersion.FIREFOX_3);
		HtmlPage loginPage = browser.getPage("https://it-it.facebook.com/");
		
		String username="test@gmail.com";
		String password="test";
		
		
		System.out.println(loginPage.getTitleText());
		loginPage
			.getElementById("email")
			.setAttribute("value", username);
		
		loginPage
		.getElementById("pass")
		.setAttribute("value", password);
	
		loginPage
		.getElementById("pass_placeholder")
		.setAttribute("value", password);
	
		
		HtmlPage homePage = null;
		DomNodeList<HtmlElement> inputs = loginPage.getElementsByTagName("input");
		for (HtmlElement input : inputs) {
			if("submit".equals(input.getAttribute("type"))){
				System.out.println("Click sul pulsante "+input.getAttribute("value"));
				homePage = input.click();
				break;
			}
		}
		
		System.out.println("letta pagina: "+homePage.getTitleText());
		
		//pagina profilo
		HtmlPage profilo = null;
		DomNodeList<HtmlElement> links = homePage.getElementsByTagName("a");
		for (HtmlElement a : links) {
			if(a.getAttribute("class")!=null && a.getAttribute("class").equals("fbxWelcomeBoxProfileLink"))
				profilo = a.click();
				
		}
		System.out.println("letta pagina: "+profilo.getTitleText());
		System.out.println("sleeping 2 seconds");
		Thread.sleep(2000);
		links = profilo.getElementsByTagName("a");
		System.out.println("read "+links.size()+" links");
		HtmlPage amici = null;
	    String url = null;
		for (HtmlElement a : links) {
			if(a.getTextContent()!=null && a.getTextContent().equals("Mostra tutti")){
				//amici = a.click();
				url = a.getAttribute("href");
				amici = browser.getPage(a.getAttribute("href"));
				break;
			}
		}
		System.out.println("letta pagina: "+amici.getTitleText());
		
		List<UserDTO> ret = new ArrayList<UserDTO>();
		ret.addAll( getAmici(browser, amici) );
		
		boolean continua=true;
	    if(ret.size()<50)
	    	continua=false;
	    int n=0;
	    while(continua){
	    	n+=50;
		    for (HtmlElement span : amici.getElementsByTagName("span")) {
				if("forwardWrapper".equals(span.getAttribute("bindpoint")) && !span.getAttribute("class").contains("UIPager_ButtonDisabled")){
					amici = browser.getPage(url+"&offset="+n);
					System.out.println("url: "+url+"&offset="+n);
				    System.out.println("read page title: "+amici.getTitleText());
					List<UserDTO> tmp = getAmici(browser, amici);
				    System.out.println("numero amici: "+tmp.size());
					if(tmp.size()<50)
						continua=false;
					else
						continua=true;
					ret.addAll(tmp);
					break;//break if
				}
		    }
	    }
		
	    for (int i = 0; i < ret.size(); i++) {
	    	UserDTO user = ret.get(i);
			System.out.println(" ==> "+user.getName()+", "+user.getUrl());			
		}
	}
	
	
	public static List<UserDTO> getAmici(WebClient session, HtmlPage paginaAmici){
		List<UserDTO> ret = new ArrayList<UserDTO>();
		HtmlElement divContainer = paginaAmici.getElementById("FriendsPage_Container");
		int n=0;
		while(divContainer.getFirstChild()==null && n<60){//max wait secondi
			n++;
			System.out.println("waiting ajax load...");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		for (HtmlElement div : divContainer.getElementsByTagName("div")) {
			for (HtmlElement divLink : div.getElementsByTagName("div")) {
				if("UIObjectListing_MetaData".equals(divLink.getAttribute("class")))
					for (HtmlElement a : divLink.getElementsByTagName("a")) {
						String url = "http://www.facebook.com"+a.getAttribute("href");
						String id = null;
						if(url.contains("/profile.php"))//id numerico
							id = url.substring(url.lastIndexOf("=")+1);
						else                                   //id alfabetico
							id = a.getAttribute("href").substring(1);
						String nome = a.getTextContent().trim();
						UserDTO dto = new UserDTO();
						dto.setName(nome);
						dto.setProfileId(id);
						dto.setUrl(url);
						ret.add(dto);
						System.out.println(" >>> "+dto.getName()+", "+dto.getUrl());

					}
				}
			}
		return ret;
	}

	public static KeyValueDTO[] getProperties(WebClient session, String facebookId) throws Exception {
		List<KeyValueDTO> ret = new ArrayList<KeyValueDTO>();
		
		String urlInfo = getInfoPage(facebookId);
		
		HtmlPage infoPage = session.getPage(urlInfo);
	    System.out.println("read page title: "+infoPage.getTitleText());
		for (HtmlElement div : infoPage.getElementsByTagName("div")) {
			if("info_section".equals(div.getAttribute("class"))){

				DomNodeList<HtmlElement> dts = div.getElementsByTagName("dt");
				DomNodeList<HtmlElement> dds = div.getElementsByTagName("dd");
				if(dts.size()!=dds.size())
					System.err.println("dds: "+dds.size()+", dts:"+dts.size());

				for (int i = 0; i < dts.size() && i< dds.size(); i++) {
					String key = dts.get(i).getTextContent().trim();
					String value = dds.get(i).getTextContent().trim();
					String xml = dds.get(i).asXml();
					//salvo l'xml solo se il contenuto e' differente dal testo
					if(xml!=null){
						xml = xml.replace("<dd>", "");
						xml = xml.replace("</dd>", "");
						xml = xml.trim();
						if(xml.equals(value))
							xml = null;
					}

					if(StringUtils.isNotEmpty(key) && key.endsWith(":")){
						key = key.substring(0, key.length()-1).trim();
					}
					if(StringUtils.isNotEmpty(key) && StringUtils.isNotEmpty(value) )
						ret.add(new KeyValueDTO(key,value, xml));
				}
				
			}
		}
		return ret.toArray(new KeyValueDTO[]{});
	}

	protected static String getInfoPage(String facebookId){
		try {
			Long.parseLong(facebookId);
			return "http://www.facebook.com"+"/profile.php"+"?v=info&id="+facebookId;
		} catch (Exception e) {//se va in eccezione il parsing l'id del profilo non e' numerico ma alfabetico
			return "http://www.facebook.com"+"/"+facebookId+"?v=info"; 
		}
	}

	
	public static class UserDTO {

		private String url;
		private String profileId;
		private String name;
		
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public String getProfileId() {
			return profileId;
		}
		public void setProfileId(String profileId) {
			this.profileId = profileId;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	}
	

	public static class KeyValueDTO {

		private String key;
		private String value;
		private String xmlValue;
		
		public KeyValueDTO() {
			super();
		}
		
		public KeyValueDTO(String key, String value, String xmlValue) {
			super();
			this.key = key;
			this.value = value;
			this.xmlValue = xmlValue;
		}
		
		public String getKey() {
			return key;
		}
		public void setKey(String key) {
			this.key = key;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}

		public String getXmlValue() {
			return xmlValue;
		}

		public void setXmlValue(String xmlValue) {
			this.xmlValue = xmlValue;
		}
		
		
	}


}
