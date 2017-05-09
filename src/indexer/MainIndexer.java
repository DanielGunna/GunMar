package indexer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import main.Machine;

public class MainIndexer {
	
	private static HashMap<String, List<File>> globalTokens;
	
	public static void mainIndexer(){
		globalTokens = new HashMap<>();
		while(true){
			if(Machine.data.size() >= 100 && Machine.data.size() <=1000 ){
				Parser parser = new Parser();
				File  parsedfile  = parser.parseDocument(Machine.data.remove(0));
				Tokenizer tokenizer = new Tokenizer();
				List<String> documentTokens = tokenizer.getTokens(parsedfile);
				for(String token :  documentTokens){
					if(globalTokens.containsKey(token)){
						globalTokens.get(token).add(parsedfile);
					}else{
						ArrayList<File> files = new ArrayList<>();
						files.add(parsedfile);
						globalTokens.put(token, files);
					}
				}
				
			}
		}
	}
}
