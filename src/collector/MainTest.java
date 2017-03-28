package collector;

import java.io.IOException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MainTest {
	
	 public static void main(String[] args) throws IOException {
	        //Validate.isTrue(args.length == 1, "usage: supply url to fetch");
	        String url = "http://globo.com";
	        print("Fetching %s...", url);

	        Document doc = Jsoup.parse(new URL(url).openStream(), "UTF-8", url);
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
