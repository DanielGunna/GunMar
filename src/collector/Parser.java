package collector;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import utils.ParsedData;

public class Parser {
	
	private Document doc;

	public Document getDoc() {
		return doc;
	}

	public void setDoc(Document doc) {
		this.doc = doc;
	}
	
	public ParsedData parseDocument() {
		Elements links = doc.select("a[href]");
		String fullText = doc.body().text().replaceAll("\\s+", " ");
		String[] words = fullText.split(" ");
		
		return new ParsedData(links, words);
		
	}
	
	
}
