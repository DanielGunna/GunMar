package collector;


import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;





public class MainTest {
	private static final String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36";
	private static final int MAX_NUM_THREADS = 8;
	public static java.util.List<URL> urlList = Collections.synchronizedList(new ArrayList<URL>());
	public static ExecutorService executor = Executors.newFixedThreadPool(MAX_NUM_THREADS);
	public static void mainCollector(String[] args) {
		
		initUrlListWithSeed();
		int cont = 0;
		
		while(true) {
			if (urlList.isEmpty()){
				//System.out.println("Url list is empty!");
				continue;
			}
			
			if (urlList.size() > 2000){
				//System.out.println("Url list overflow!");
				continue;
			}
			
			URL url = (URL) urlList.remove(0);
			//Fetcher worker = new Fetcher(url.toString());
			//executor.execute(worker);
			cont++;
	        
		} 
		//executor.shutdown();
//		
//        while (!executor.isTerminated()) {
//        }
        
        
        
	//	System.out.println("FINISHED");

	}
	
	private static void initUrlListWithSeed() {
		urlList.add(new URL("http://pt.wikipedia.org/wiki/Categoria:Pessoas_vivas"));
		urlList.add(new URL("http://pt.wikipedia.org/wiki/Categoria:Instituições"));
		//urlList.add(new URL("http://globo.com"));
		//urlList.add(new URL("http://www.nytimes.com/"));
		//urlList.add(new URL("http://www.dicionariodenomesproprios.com.br/"));
	    //urlList.add(new URL("http://www.yahoo.com"));
		//urlList.add(new URL("http://dmoztools.net/"));

	}
	
}
