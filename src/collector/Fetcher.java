package collector;

import java.io.File;
import java.io.IOException;
import java.net.URI;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import main.Machine;
import main.Machine.DocumentData;
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
		if(DnsResolver.getInstance().canDownloadData(getUrl())){
			DnsResolver.getInstance().resolveAddress(URI.create(url));
			return RequestHandler.getInstance().getDataFromUrl(url);
		}else{
			System.out.println("Disallowed url :" + url);
		}
	
	    return null;
    }

private URI getUrl() {
	if(url.equals("")|| url.equals(" ")|| url.equals("http://"))
		return null;
	return new URL(url).toURI();
}

public class DocumentFile{
	@Expose
	@SerializedName("url")
	private String url;
	@Expose
	@SerializedName("words_list")
	private String[]  words;
	public String[] getWords() {
		return words;
	}
	public void setWords(String[] words) {
		this.words = words;
	}
	
	
	
}

private void sendDocumentToParser(Document doc) {
	if(doc == null)
		return;
	parser = new Parser();
	parser.setDoc(doc);
	pd = parser.parseDocument();
	if(pd == null)
		return;
	stopTime = System.currentTimeMillis();
    long elapsedTime = stopTime - startTime;
    //System.out.println("COLETA: "+url+" -> "+elapsedTime);
    
    for (String s : pd.getLinks()) {
    	MainTest.urlList.add(new collector.URL(s));
    }
     
	File fileLinks = FileUtils.openWrite("links/" + url.replace("/", "") + ".txt");
	String[] links  = pd.getLinks();
	if(links !=null)
		FileUtils.println(links);
	FileUtils.close();
	
	File fileText = FileUtils.openWrite("docs/" + url.replace("/", "") + ".txt");
	String[] text  = pd.getWords();
	if(text !=null)
		FileUtils.println(text);
	FileUtils.close();
	Machine.getInstace().addFile(fileLinks,fileText,url,text.length);
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
