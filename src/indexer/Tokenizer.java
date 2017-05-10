package indexer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Tokenizer {
	private HashMap<String, Integer> occurrence;
	
	
	
	public List<String> getTokens(File file){
		occurrence = new HashMap<>();
		FileReader reader;
		Set<String> tokens = null;
		try {
			reader = new FileReader(file);
			BufferedReader buff = new BufferedReader(reader);
			String line = "";
			String fileString = "";
			while((line = buff.readLine())!=null){
				String[] lineTokens  =  line.split(",");
				getOcurrency(lineTokens);
				tokens.addAll(Arrays.asList(lineTokens));
			}
			reader.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getList(tokens);
	}

	private void getOcurrency(String[] lineTokens) {
		for(String s : lineTokens){
			if(occurrence.containsKey(s)){
				Integer occurence = occurrence.get(s);
				occurrence.put(s,occurence++);
			}else{
				occurrence.put(s,1);
			}
		}
		
	}

	public HashMap<String,Integer> getOcurrency(){
		return occurrence;
	}
	
	
	private List<String> getList(Set<String> tokens) {
		if(tokens == null || tokens.size() == 0){
			return new ArrayList<>();
		}
		ArrayList<String> tokenList = new ArrayList<>();
		for(String token : tokens){
			tokenList.add(token);
		}
		return tokenList;
	}
	
	
}
