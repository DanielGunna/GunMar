package indexer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

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
			String[] lineTokens  =  fileString.split(",");
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
		return fileString = removeStopWords(fileString.split(","));
	}
	
	
	private String removeStopWords(String[] fileString) {

		List<String> words = new ArrayList<String>();
		for (String word : fileString) {

			if (isStopWord(word) == false) {
				words.add(word);
			}

		}
		return String.join(",", words);
	
	}


		private boolean isStopWord(String word) {

			for (String stopWord : StopWords.enStopWords) {
				if (word.equals(stopWord)) {
					return true;
				}
				
			}
			

			for (String stopWord : StopWords.esStopWords) {
				if (word.equals(stopWord)) {
					return true;
				}
			}
			

			for (String stopWord : StopWords.ptStopWords) {
				if (word.equals(stopWord)) {
					return true;
				}
			}

			return false;
		}
		

}
