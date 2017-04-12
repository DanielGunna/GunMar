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
	
	private long startTime = 0;
	private long stopTime = 0;

	public Fetcher(String url) {
		startTime = 0;
		stopTime = 0;
		this.url = url;
	}

	public Document fetchDocument() throws Exception {
		startTime = System.currentTimeMillis();
		if(DnsResolver.getInstance().canDownloadData(new URL(url).toURI())){
			DnsResolver.getInstance().resolveAddress(URI.create(url));
			return RequestHandler.getInstance().getDataFromUrl(url);
		}else{
			System.out.println("Disallowed url :" + url);
		}
	
	    return null;
    }

private void sendDocumentToParser(Document doc) {
	parser = new Parser();
	parser.setDoc(doc);
	pd = parser.parseDocument();
	stopTime = System.currentTimeMillis();
    long elapsedTime = stopTime - startTime;
    System.out.println("COLETA: "+url+" -> "+elapsedTime);
	FileUtils.openWrite("docs/" + url.replace("/", "") + ".txt");
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
