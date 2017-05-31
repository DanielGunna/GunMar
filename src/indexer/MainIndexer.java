package indexer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import main.Machine;
import main.Machine.DocumentData;
import utils.huffman.Encode;

public class MainIndexer {
	
	private static HashMap<String, IndexEntry> globalTokens;
	
	
	public static class IndexEntry{
		private Integer ocurrency;
		private List<File> files;
		
		
		@Override
		public String toString() {
			return "{occurency:"+ocurrency
					+ ","
					+ "files:}"+ getFileNameList();
		}
		
		private String getFileNameList() {
			ArrayList<String> filesName = new ArrayList<>();
			for(File file : files){
				filesName.add(file.getName());
			}	
			return filesName.toString();
		}

		public List<File> getFiles(){
			return files;
		}
		
		public void incrementCont(Integer c){
			ocurrency+=c;
		}
		public IndexEntry(Integer x , List<File> files){
			this.files= files;
			ocurrency = x ;
		}
	}
	
	public static void mainIndexer(){
		globalTokens = new HashMap<>();
		while(true){
			if(Machine.data.size() >= 100 && Machine.data.size() <=1000 ){
				Parser parser = new Parser();
				DocumentData data = Machine.getInstace().data.remove(0);
				File  parsedfile  = parser.parseDocument(data.getDocumentFile());
				Tokenizer tokenizer = new Tokenizer();
				List<String> documentTokens = tokenizer.getTokens(parsedfile);
				HashMap<String, Integer> occur = tokenizer.getOcurrency();
				for(String token :  documentTokens){
					if(globalTokens.containsKey(token)){
						globalTokens.get(token).getFiles().add(parsedfile);
						globalTokens.get(token).incrementCont(occur.get(token));
					}else{
						ArrayList<File> files = new ArrayList<>();
						files.add(parsedfile);
						IndexEntry newEntry = new IndexEntry(occur.get(token), files);
						globalTokens.put(token,newEntry);
						System.out.println("Ocurrency :"+newEntry.toString());
					}
				}
				
				Encode encode = new Encode(parsedfile.getPath(), new File("compressed/"+parsedfile.getName()).getPath());
				encode.performEncode();
			}
		}
	}
}
