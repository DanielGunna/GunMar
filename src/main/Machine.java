package main;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import collector.MainTest;


public class Machine {
	
	public class DocumentData{
		private String documentUrl;
		private File documentFile;
		public DocumentData(File b, String a){
			documentUrl = a;
			documentFile = b; 
		}
	}
	
	
	public static void addFile(File file , String url){
		//DocumentData doc = new DocumentData(file, url);
		data.add(file);
	}
	
	public  static  List<File> data = Collections.synchronizedList(new ArrayList<File>());
	
	
	public static void main(String[] args ){
		Thread collectorthread = new Thread(()->{
			MainTest.mainCollector(args);
		});
		Thread indexerThread  = new Thread(()->{
			
		});
		collectorthread.start();
		indexerThread.start();
		try {
			indexerThread.wait(10000000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	
		
	}

}
