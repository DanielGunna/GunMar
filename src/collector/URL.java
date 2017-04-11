package collector;

import java.net.URI;

public class URL {
	
	private String url;
	
	public URL(String url) {
		this.url = url;
		format();
	}
	
	private void format() {
		url.replaceAll("https", "http");
		if (url.contains("http://") == false) {
			url = "http://"+url;
		}
	}
	
	public URI toURI() {
		return URI.create(url);
	}
	
	public String toString() {
		return url;
	}
	
}
