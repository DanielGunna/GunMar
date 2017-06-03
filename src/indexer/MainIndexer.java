package indexer;

import java.io.File;
import java.io.IOException;
import java.nio.channels.ShutdownChannelGroupException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import main.Machine;
import main.Machine.DocumentData;
import utils.FileUtils;
import utils.huffman.Decode;
import utils.huffman.Encode;

public class MainIndexer {
	
	private static HashMap<String, IndexEntry> globalTokens;
	
	
	public static class IndexEntry implements Comparable<IndexEntry> {
		private Integer ocurrency;
		private List<File> files;

		public Integer getOcurrency() {
			return ocurrency;
		}
		
		
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

		@Override
		public int compareTo(IndexEntry o) {
			// TODO Auto-generated method stub
			return (o.getOcurrency() > this.ocurrency) ? -1 : (o.getOcurrency() == ocurrency) ? 0 : 1;
		}
	}
	
	private  static void showIndex() {
		System.out.println("-----------------Current Index-----------------");
		for(Map.Entry<String,IndexEntry> i : globalTokens.entrySet()) {
			System.out.println("|Token: "+i.getKey()+"| nº ocurrency : "+i.getValue().ocurrency+"| ocurrencies: "+i.getValue().getFileNameList()+"|");	
		}
		System.out.println("Nº of tokens"+globalTokens.size());
		System.out.println("-----------------End of Current Index-----------------");
	}
	
	public static void mainIndexer(){
		globalTokens = new HashMap<>();
		while(true){
			if(Machine.data.size() >= 100 && Machine.data.size() <=1000 ){
				Parser parser = new Parser();
				DocumentData data = Machine.getInstace().data.remove(0);
				//showFile(data.getDocumentFile());
				File  parsedfile  =  new File(parser.parseDocument(data.getDocumentFile()));
				//FileUtils.showFile(parsedfile);
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
						//System.out.println("Token: " + token + " " +globalTokens.get(token).toString());
					}
				}
				
				//File compressed  = new File("compressed/"+parsedfile.getName().replace("txt", "")+"huf");
				//Encode encode = new Encode(parsedfile.getPath(),compressed.getPath() );
				//encode.performEncode();
			}else {
				if(globalTokens.size() > 0)
					showIndex();
			}
		}
	}
	
	public static void main(String[] args) {
		MainIndexer.mainIndexer();
	}
}
