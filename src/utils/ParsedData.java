package utils;

import org.jsoup.select.Elements;

public class ParsedData {
	
	private String[] links;
	private String[] words;
	
	public ParsedData(String[] links, String[] words) {
		this.links = links;
		this.words = words;
	}
	
	public String[] getLinks() {
		return links;
	}
	public void setLinks(String[] links) {
		this.links = links;
	}
	public String[] getWords() {
		return words;
	}
	public void setWords(String[] words) {
		this.words = words;
	}

}
