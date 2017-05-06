package indexer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;

public class Parser {
	
	
	
	
	public void parseDocument(File file){
		FileReader reader;
		FileWriter writer;
		try {
			writer = new FileWriter(file);
			reader = new FileReader(file);
			BufferedReader buff = new BufferedReader(reader);
			String line = "";
			String fileString = "";
			while((line = buff.readLine())!=null){
				fileString+=line;
			}
			writer.write(getParsedFile(fileString));
			writer.close();
			reader.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	
	}

	private String getParsedFile(String fileString) {
		fileString.replace("á","a");
		fileString.replace("à","a");
		fileString.replace("ê","e");
		fileString.replace("â","a");
		fileString.replace("-","");
		fileString.replace("ã","a");
		fileString.replace("õ","o");
		fileString.replace("é","e");
		fileString.replace("í","i");
		fileString.replace("ó","o");
		fileString.replace("ú","u");
		
		return fileString;
	}

}