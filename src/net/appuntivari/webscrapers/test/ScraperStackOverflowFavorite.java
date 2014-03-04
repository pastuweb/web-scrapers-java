package net.appuntivari.webscrapers.test;

import java.util.List;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/*
 * 
 * Questo parser / scanner è stato usato nel plugin MyStackOverflowFavorite-portlet
 * 
 */


public class ScraperStackOverflowFavorite {

	public static void main(String[] args) throws Exception {
		
		WebClient browser = new WebClient();
		browser.setJavaScriptEnabled(false);		
		
		
		String val_meta_info = null;
		HtmlPage page = browser.getPage("http://stackoverflow.com/users/2723164/pastuweb?tab=favorites");
		
		System.out.println("lettura pagina");

		HtmlElement favorite = page.getElementById("user-tab-favorites");		
		DomNodeList<HtmlElement> divs = favorite.getElementsByTagName("div");
		
		for (HtmlElement div : divs) {
			if(div.getAttribute("class").equals("user-tab-content")){
						
						DomNodeList<HtmlElement> divs_user_tab_content = div.getElementsByTagName("div");
						for (HtmlElement div_u_t_c : divs_user_tab_content) {

							if(div_u_t_c.getAttribute("class").equals("user-questions")){
								
								DomNodeList<HtmlElement> divs_user_questions= div_u_t_c.getElementsByTagName("div");
								for (HtmlElement div_u_q : divs_user_questions) {
									
									DomNodeList<HtmlElement> divs_q_summ= div_u_q.getElementsByTagName("div");
									for (HtmlElement div_q_summ : divs_q_summ) {
										
										if(div_q_summ.getAttribute("class").contains("question-counts")){
											//Voti
												HtmlElement votes = (HtmlElement)div_q_summ.getElementsByTagName("div").item(0);
												String val_votes = ((HtmlElement)votes.getElementsByTagName("div").item(0)).getTextContent().trim();
												System.out.println("votes: "+ val_votes);
										}
										
										if(div_q_summ.getAttribute("class").contains("summary")){
																						
											//titolo
											String val_titolo =  ((HtmlElement)div_q_summ.getFirstChild()).getTextContent().trim();
											System.out.println("titolo: "+val_titolo);
											
											//link del titolo
											String val_link =  "http://stackoverflow.com"+((HtmlElement)((HtmlElement)div_q_summ.getFirstChild()).getFirstChild()).getAttribute("href").toString();
											System.out.println("link: "+val_link);
											
											//meta-info
											DomNodeList<HtmlElement> divs_meta_info= div_q_summ.getElementsByTagName("div");
											for (HtmlElement item_meta_info : divs_meta_info) {
												if(item_meta_info.getAttribute("class").contains("tags")){
												
													val_meta_info = new String("");
													DomNodeList<HtmlElement> meta_info= item_meta_info.getElementsByTagName("a");
													for (HtmlElement a_meta_info : meta_info) {
														if(a_meta_info.getAttribute("class").contains("post-tag")){
															val_meta_info = val_meta_info+""+a_meta_info.getTextContent().trim()+",";
															
														}
													}
												}
												
												
											}
											System.out.println("meta-info: "+val_meta_info.substring(0, val_meta_info.length()-1));
											
										}
									}
									
									
									
								}
								
							}
						}

					}
				}
				
			
	}
}
