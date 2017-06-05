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

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import indexer.MainIndexer.IndexEntry.DocumentEntry;
import main.Machine;
import main.Machine.DocumentData;
import utils.FileUtils;
import utils.huffman.Decode;
import utils.huffman.Encode;

public class MainIndexer {
	
	private static HashMap<String, IndexEntry> globalTokens;
	
	
	public static class IndexEntry implements Comparable<IndexEntry> {
		@Expose
		@SerializedName("index_ocurrency")
		private Integer ocurrency;
		@Expose
		@SerializedName("index_files")
		private HashMap<String,DocumentEntry> files;

		public Integer getOcurrency() {
			return ocurrency;
		}
		
		public static class DocumentEntry{
			@Expose
			@SerializedName("file_url")
			private String fileUrl;
			private File documentFile;
			@Expose
			@SerializedName("file_ocurrency")
			private Integer fileOcurrencies;
			
			@Override
			public String toString() {
				// TODO Auto-generated method stub
				return "{url:"+fileUrl
						+",frequency:"+fileOcurrencies
						+",document:"+documentFile.getName()+"}";
			}
			
			
			public DocumentEntry(String url, File file, Integer x) {
				fileUrl = url;
				documentFile = file;
				fileOcurrencies = x ;
			// TODO Auto-generated constructor stub
			}
			public String getFileUrl() {
				return fileUrl;
			}
			public void setFileUrl(String fileUrl) {
				this.fileUrl = fileUrl;
			}
			public Integer getFileOcurrencies() {
				return fileOcurrencies;
			}
			public void setFileOcurrencies(Integer fileOcurrencies) {
				this.fileOcurrencies = fileOcurrencies;
			}
			public File getDocumentFile() {
				return documentFile;
			}
			public void setDocumentFile(File documentFile) {
				this.documentFile = documentFile;
			}
			
		}
		
		
		@Override
		public String toString() {
			return "{occurency:"+ocurrency
					+ ","
					+ "files:"+ getFileNameList()+"}";
		}
		
		private String getFileNameList() {
			ArrayList<String> filesName = new ArrayList<>();
			for(Map.Entry<String, DocumentEntry> file : files.entrySet()){
				filesName.add(file.getValue().toString());
			}	
			return filesName.toString();
		}

		public HashMap<String,DocumentEntry> getFiles(){
			return files;
		}
		
		public void incrementCont(Integer c){
			ocurrency+=c;
		}
		public IndexEntry(Integer x , HashMap<String,DocumentEntry> files){
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
			if(Machine.getInstace().data.size() >= 2){
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
						IndexEntry entry  = globalTokens.get(token);
						DocumentEntry document = new DocumentEntry(data.getDocumentUrl(),parsedfile,occur.get(token));
						entry.getFiles().put(parsedfile.getName(),document);
						globalTokens.get(token).incrementCont(occur.get(token));
					}else{
						HashMap<String,DocumentEntry> files = new HashMap<>();
						DocumentEntry document = new DocumentEntry(data.getDocumentUrl(),parsedfile,occur.get(token));
						files.put(parsedfile.getName(),document);
						IndexEntry newEntry = new IndexEntry(occur.get(token), files);
						globalTokens.put(token,newEntry);
						//System.out.println("Token: " + token + " " +globalTokens.get(token).toString());
					}
				}
				
				File compressed  = new File("compressed/"+parsedfile.getName().replace("txt", "")+"huf");
				Encode encode = new Encode(parsedfile.getPath(),compressed.getPath() );
				encode.performEncode();
				//showIndex();
			}else {
				if(globalTokens.size() > 0){
					saveIndex();
				}
			}
		}
	}
	
	private static class SerializedIndex{
		@Expose
		@SerializedName("index")
		private HashMap<String, IndexEntry> index;

		public HashMap<String, IndexEntry> getIndex() {
			return index;
		}

		public void setIndex(HashMap<String, IndexEntry> index) {
			this.index = index;
		}
		
	}

	private static void saveIndex() {
		Gson gson = new Gson();
		SerializedIndex index = new SerializedIndex();
		index.setIndex(globalTokens);
		FileUtils.openWrite("index.txt");
		FileUtils.print(gson.toJson(index));
		FileUtils.close();	
	}
	
	//public static void main(String[] args) {
		//ainIndexer.mainIndexer();
	//}
}
