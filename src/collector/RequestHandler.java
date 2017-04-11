package collector;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
 
public class RequestHandler {
	static RequestHandler instance;
	
	private RequestHandler() {
		
	}
	
	public static RequestHandler getInstance(){
		if(instance == null ){
			instance = new RequestHandler();
		}
		return instance;
	}
	
	public Document getDataFromUrl(String url )throws Exception{
		Document document =  Jsoup.connect(url).get();
		return document;
	}
	
	
	
}
