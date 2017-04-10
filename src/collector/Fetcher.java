package collector;

import java.io.IOException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Fetcher {
	
	private String url;
	
	public Fetcher(String url) {
		this.url = url;
	}
	
	public Document fetchDocument() throws Exception {
		 return RequestHandler.getInstance().getDataFromUrl(url);
	}

}
