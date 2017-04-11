package collector;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.Semaphore;

import javax.print.attribute.standard.Finishings;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utils.ParsedData;

public class MainTest {
	private static final String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36";
	private static final int MAX_NUM_THREADS = 32;
	
	public static void main(String[] args) {

		// Validate.isTrue(args.length == 1, "usage: supply url to fetch");
		URL url = new URL("www.wikipedia.com");
		print("Fetching %s...", url);
		DnsResolver.getInstance().resolveAddress(url.toURI());
		Fetcher fetcher = new Fetcher(url.toString());
		Document doc;
		try {
			doc = fetcher.fetchDocument();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}

		Parser parser = new Parser();
		parser.setDoc(doc);
		ParsedData pd = parser.parseDocument();
		Semaphore s = new Semaphore(MAX_NUM_THREADS);
		for (String link : pd.getLinks()) {

			try {
				s.acquire();
				execute(link, s);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			

		}

	}
	
	private static void execute(String link, Semaphore s) {
		new Thread() {

			@Override
			public void run() {
				try {
					URL url = new URL(link);
					System.out.println(url.toString());
					DnsResolver.getInstance().resolveAddress(url.toURI());
					Fetcher fetcher = new Fetcher(url.toString());
					Document doc = null;
					try {
						doc = fetcher.fetchDocument();
					} catch (Exception e) {
						interrupt();
					}

					Parser parser = new Parser();
					parser.setDoc(doc);
					ParsedData pd = parser.parseDocument();

					for (String word : pd.getLinks()) {
						System.out.println(word);
					}
				} finally {
					s.release();
				}

			}
		}.start();
	}

	private static void print(String msg, Object... args) {
		System.out.println(String.format(msg, args));
	}

}
