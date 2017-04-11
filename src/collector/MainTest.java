package collector;

import java.io.IOException;
import java.net.URI;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MainTest {
	private static final String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36";
	
	 public static void main(String[] args) throws Exception {
		 
		 	
		 
	        //Validate.isTrue(args.length == 1, "usage: supply url to fetch");
		 	URL url = new URL("wladmirbrandao.com");
	        print("Fetching %s...", url);
	        DnsResolver.getInstance().resolveAddress(url.toURI());
	        Fetcher fetcher = new Fetcher(url.toString());
	        Document doc = fetcher.fetchDocument();
	        
	        Elements links = doc.select("a[href]");


	        print("\nLinks: (%d)", links.size());
	        for (Element link : links) {
	        
	            print("%s", link.attr("abs:href"));

	       }
	        
	        
	        
	        
	    }

	    private static void print(String msg, Object... args) {
	        System.out.println(String.format(msg, args));
	    }


}
