package collector;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

public class URL {
	
	private String url;
	
	
	public URL(String url) {
		this.url = url;
		format();
	}
	
	private void format() {
	    url = url.replace("https", "http");
		if (url.contains("http://") == false) {
			url = "http://"+url;
		}
	}
	
	public URI toURI() {
		URI uri = getValidUrl(url);
		if(uri == null)
			return URI.create(url);
		return uri;
	}
	
	public String toString() {
		return url;
	}
	
	private  URI getValidUrl(String myURL){
		URI uri = null;
		try {
	      	java.net.URL url = new java.net.URL(myURL);
	      	String nullFragment = null;
		  	uri = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), nullFragment);
		//  	System.out.println("URI " + uri.toString() + " is OK");
    	} catch (MalformedURLException e) {
    		System.out.println("URL " + myURL + " is a malformed URL");
		} catch (URISyntaxException e) {
			System.out.println("URI " + myURL + " is a malformed URL");
		}
		return uri;
	}
	
}
