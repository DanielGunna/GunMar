package utils;

import org.jsoup.select.Elements;

public class ParsedData {
	
	private Elements links;
	private String[] words;
	
	public ParsedData(Elements links, String[] words) {
		this.links = links;
		this.words = words;
	}
	
	public Elements getLinks() {
		return links;
	}
	public void setLinks(Elements links) {
		this.links = links;
	}
	public String[] getWords() {
		return words;
	}
	public void setWords(String[] words) {
		this.words = words;
	}

}
