package indexer;

import java.io.File;

import utils.FileUtils;

public class Analyzer {

	public void createFullInvertedIndex() throws Exception {
		File folder = new File("words/");
		File[] listOfFiles = folder.listFiles();
 
		for (File file : listOfFiles) {
		    if (file.isFile()) {
		    	System.out.println("PASSOU");
		        FileUtils.openRead(file.getCanonicalPath());
		        System.out.println(FileUtils.readAll());
		        FileUtils.close();
		    }
		}
	}
	
}
