package collector;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
		if(doc !=null){
			Elements linksElem = doc.select("a[href]");
			
			String fullText = doc.body().text().replaceAll("\\s+", " ");
			String[] words = fullText.split(" ");
			String[] links = new String[linksElem.size()];
			int i = 0;
			for (Element link : linksElem) {
				links[i++] = link.absUrl("href");
			}
			return new ParsedData(links, words);
		}
			
		return null;
		
	}
	
	
}
