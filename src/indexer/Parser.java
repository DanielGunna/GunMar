package indexer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;

import utils.StopWords;

public class Parser {
	
	
	public File parseDocument(File file){
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
		return file;
	}

	private String getParsedFile(String fileString) {
		removeStopWords(fileString);
		fileString.replace("á","a");
		fileString.replace("à","a");
		fileString.replace("ê","e");
		fileString.replace("â","a");
		fileString.replace("-","");
		fileString.replace("!","");
		fileString.replace(".","");
		fileString.replace("ç","c");
		fileString.replace("?","");
		fileString.replace("ã","a");
		fileString.replace("õ","o");
		fileString.replace("é","e");
		fileString.replace("í","i");
		fileString.replace("ó","o");
		fileString.replace("ú","u");
		fileString.replace("ü","u");
		fileString.replace("(","");
		fileString.replace(")","");
		fileString.replace("{","");
		fileString.replace("}","");
		return fileString;
	}
	
	
	private String removeStopWords(String fileString) {
		
		for (String stopWord : StopWords.enStopWords) {
			fileString.replace(fileString, stopWord);
		}
		

		for (String stopWord : StopWords.esStopWords) {
			fileString.replace(fileString, stopWord);
		}
		

		for (String stopWord : StopWords.ptStopWords) {
			fileString.replace(fileString, stopWord);
		}
		
		return fileString;
	
	}

}
