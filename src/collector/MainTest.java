package collector;

import java.awt.List;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import javax.print.attribute.standard.Finishings;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utils.ParsedData;

public class MainTest {
	private static final String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36";
	private static final int MAX_NUM_THREADS = 8;
	public static java.util.List<URL> urlList = Collections.synchronizedList(new ArrayList<URL>());
	
	public static void main(String[] args) {
		
		initUrlListWithSeed();
		ExecutorService executor = Executors.newFixedThreadPool(MAX_NUM_THREADS);
		while(!urlList.isEmpty()) {
			
			URL url = (URL) urlList.remove(0);
			Fetcher worker = new Fetcher(url.toString());
			executor.execute(worker);
		
	        
		} 
		executor.shutdown();
		
        while (!executor.isTerminated()) {
        }
        
        
        
		System.out.println("FINISHED");

	}
	
	private static void initUrlListWithSeed() {
		urlList.add(new URL("http://www.wladmirbrandao.com"));
		urlList.add(new URL("www.ibckoinonia.com.br"));
		urlList.add(new URL("www.pucminas.br"));
		urlList.add(new URL("www.atletico.com.br"));
		urlList.add(new URL("www.wikipedia.org"));
		urlList.add(new URL("www.globo.com"));
		urlList.add(new URL("www.uol.com.br"));
		urlList.add(new URL("www.terra.com.br"));
	}
	
}
