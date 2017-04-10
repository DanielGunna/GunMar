package collector;

import java.io.IOException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Fetcher {
	
	private URL url;
	
	public Fetcher(URL url) {
		this.url = url;
	}
	
	public Document fetchDocument() throws IOException {
		 return Jsoup.parse(url.openStream(), "UTF-8", url.toString());
	}

}
