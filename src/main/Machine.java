package main;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import collector.MainTest;
import indexer.Analyzer;
import indexer.MainIndexer;


public  class Machine {
	private  static Machine instance; 
	
	public Machine(){
		instance = this;
	}
	
	public static  Machine getInstace(){
		return instance;
	}
	
	public class DocumentData{
		private String documentUrl;
		private File documentFile;
		
		public String getDocumentUrl() {
			return documentUrl;
		}

		public void setDocumentUrl(String documentUrl) {
			this.documentUrl = documentUrl;
		}


		public File getDocumentFile() {
			return documentFile;
		}

		public void setDocumentFile(File documentFile) {
			this.documentFile = documentFile;
		}

		public File getUrlsFile() {
			return urlsFile;
		}

		public void setUrlsFile(File urlsFile) {
			this.urlsFile = urlsFile;
		}

		private File urlsFile;
		
		public DocumentData(File a,File b, String url){
			urlsFile  = a;
			documentUrl = url;
			documentFile = b; 
		}
	}
	
	
	private void showFile(File words) {
		try (Stream<String> stream = Files.lines(Paths.get(words.getPath()))) {
	        stream.forEach(System.out::println);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public  void addFile(File links,File words, String url){
		data.add(new DocumentData(links, words, url));
	}
	
	public  static  List<DocumentData> data = Collections.synchronizedList(new ArrayList<DocumentData>());
	
	
	protected void mainMachine(String[] args){
		Thread collectorthread = new Thread(()->{
			MainTest.mainCollector(args);
		});
		Thread indexerThread  = new Thread(()->{
			MainIndexer.mainIndexer();
		});
		collectorthread.start();
		indexerThread.start();
	}
	
	
	public static void main(String[] args ) throws Exception{
		//Analyzer a = new Analyzer();
		//a.createFullInvertedIndex();
		new Machine().mainMachine(args);
		///try {
			//indexerThread.wait(10000000);
		//} catch (InterruptedException e) {
		//	e.printStackTrace();
		//}
	
		
	}

}
