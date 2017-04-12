package collector;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import utils.FileUtils;
import utils.ParsedData;

public class Fetcher implements Runnable {
	
	private String url;
	Parser parser;
	ParsedData pd;
	
	public Fetcher(String url) {
		this.url = url;
	}
	
	public Document fetchDocument() throws Exception {
		 String addr  = DnsResolver.getInstance().resolveAddress(URI.create(url));
		 return RequestHandler.getInstance().getDataFromUrl(addr);
	}
	
	private void sendDocumentToParser(Document doc) {
		parser = new Parser();
		parser.setDoc(doc);
		pd = parser.parseDocument();

		FileUtils.openWrite("docs/"+url.replace("/", "")+".txt");
		FileUtils.println(pd.getLinks());
		FileUtils.close();
	}
	
	@Override
    public void run() { 
        try {
			sendDocumentToParser(fetchDocument());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

}
