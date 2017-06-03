package indexer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;

import utils.FileUtils;
import utils.StopWords;

public class Parser {
	
	
	public String parseDocument(File file){
		FileReader reader;
		File  parsed = FileUtils.openWrite("parsed/"+file.getName());
		try {
			reader = new FileReader(file);
			BufferedReader buff = new BufferedReader(reader);
			String line = "";
			String fileString = "";
			while((line = buff.readLine())!=null){
				fileString+=line;
			}
			reader.close();
			FileUtils.print(getParsedFile(fileString));
		} catch (Exception e) {
			e.printStackTrace();
		}
		FileUtils.close();
		return parsed.getPath();
	}

	private String getParsedFile(String fileString) {
		fileString = fileString.replace("á","a");
		fileString = fileString.replace("à","a");
		fileString = fileString.replace("ê","e");
		fileString = fileString.replace("â","a");
		fileString = fileString.replace("-","");
		fileString = fileString.replace("!","");
		fileString = fileString.replace(".","");
		fileString = fileString.replace("ç","c");
		fileString = fileString.replace("?","");
		fileString = fileString.replace("ã","a");
		fileString = fileString.replace("õ","o");
		fileString = fileString.replace("é","e");
		fileString = fileString.replace("í","i");
		fileString = fileString.replace("ó","o");
		fileString = fileString.replace("ú","u");
		fileString = fileString.replace("ü","u");
		fileString = fileString.replace("(","");
		fileString = fileString.replace(")","");
		fileString = fileString.replace("{","");
		fileString = fileString.replace("}","");
		return fileString = removeStopWords(fileString);
	}
	
	
	private String removeStopWords(String fileString) {
		
		//for (String stopWord : StopWords.enStopWords) {
		//	fileString = fileString.replace(stopWord,"");
		//}
		

		//for (String stopWord : StopWords.esStopWords) {
		//	fileString = fileString.replace(stopWord,"");
		//}
		

		//for (String stopWord : StopWords.ptStopWords) {
		//	fileString = fileString.replace(stopWord,"");
		//}
		
		return fileString;
	
	}

}
