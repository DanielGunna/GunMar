package collector;

import java.io.IOException;

import org.jsoup.HttpStatusException;
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
	
	public Document getDataFromUrl(String url ){
		Document document = null;
		try{
			String  urlToResolve = new String(new URL(url).toString().getBytes("UTF-8"));
			document =  Jsoup.connect(urlToResolve).get();
		}catch(HttpStatusException e){
			System.out.println("Http Error:" + e.getStatusCode());
		} catch (IOException e) {
			System.out.println("IO Error:" + e.getLocalizedMessage());
		}
		return document;
	}
	
	
	
}
