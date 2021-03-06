package indexer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.channels.ShutdownChannelGroupException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchEvent.Modifier;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.w3c.dom.views.DocumentView;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import collector.Fetcher;
import collector.Fetcher.DocumentFile;
import collector.MainTest;
import collector.URL;
import indexer.MainIndexer.IndexEntry.DocumentEntry;
import main.Machine;
import main.Machine.DocumentData;
import utils.FileUtils;
import utils.huffman.Decode;
import utils.huffman.Encode;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
public class MainIndexer {
	
	public  static HashMap<String, IndexEntry> globalTokens;
	private static final int  SIZE  = 1;
	
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
		
		public ArrayList<DocumentEntry>  getFilesEntries(){
			ArrayList<DocumentEntry> list = new ArrayList<>();
			for (Map.Entry<String, DocumentEntry> e : files.entrySet()) {
				list.add(e.getValue());
			}
			return list;
		}
		
		public ArrayList<String> getFilesList(){
			ArrayList<String> list = new ArrayList<>();
			for(Map.Entry<String, DocumentEntry> e : files.entrySet()){
				list.add(e.getValue().documentFile.getName());
			}
			return list;
		}
		
		public static class DocumentEntry{
			@Expose
			@SerializedName("file_url")
			private String fileUrl;
			private File documentFile;
			@Expose
			@SerializedName("file_ocurrency")
			private Integer fileOcurrencies;
			@Expose
			@SerializedName("file_length")
			private Integer fileLength;
			
			public Integer getFileLength() {
				return fileLength;
			}

			public void setFileLength(Integer fileLength) {
				this.fileLength = fileLength;
			}

			@Override
			public String toString() {
		
				return "{url:"+fileUrl
						+",frequency:"+fileOcurrencies
						+",document:"+documentFile.getName()+"}";
			}
			
			
			
			
			public DocumentEntry(String url, File file, Integer x, Integer fileLength) {
				fileUrl = url;
				documentFile = file;
				fileOcurrencies = x ;
				this.fileLength = fileLength; 
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
		loadIndex();
		if(globalTokens.size() == 0){
			Thread index = new Thread(()->{verifyCorpus();});
			index.start();
		}
		int add = 0;
		while(true){
			if(Machine.getInstace().data.size() > 0){
				//if(globalTokens.size() >= 10  || (add - globalTokens.size())>= 10){
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
						DocumentEntry document = new DocumentEntry(data.getDocumentUrl(),parsedfile,occur.get(token),data.getFileLength());
						entry.getFiles().put(parsedfile.getName(),document);
						globalTokens.get(token).incrementCont(occur.get(token));
					}else{
						HashMap<String,DocumentEntry> files = new HashMap<>();
						DocumentEntry document = new DocumentEntry(data.getDocumentUrl(),parsedfile,occur.get(token),data.getFileLength());
						files.put(parsedfile.getName(),document);
						IndexEntry newEntry = new IndexEntry(occur.get(token), files);
						globalTokens.put(token,newEntry);
						//System.out.println("Token: " + token + " " +globalTokens.get(token).toString());
					}
					
				}
				//File compressed  = new File("compressed/"+parsedfile.getName().replace("txt", "")+"huf");
				//Encode encode = new Encode(parsedfile.getPath(),compressed.getPath() );
				//encode.performEncode();
				//}else 
				if(add != globalTokens.size()&&globalTokens.size() > 0 && (globalTokens.size() - add)>= SIZE){
					add = globalTokens.size();
					//System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Saving index...");
					saveIndex();
				} 
				if(Machine.data.size() ==  0 )add = globalTokens.size();
				//showIndex();
			}else {
				
			}
		}
	}
	
	private static void verifyCorpus() {
		ArrayList<File> parsed = getFilesFromPath("parsed/");
		//readWikipediaBase();
		System.out.println("trying create index...");
		tryRecreateIndex(getListDocumentFile(getFilesFromPath("docs/")),getListDocumentFile(getFilesFromPath("links/")));
	}
	
	private static void readWikipediaBase(){
		 ArrayList<File> corpus = new ArrayList<>();
		 getFiles(corpus,(Paths.get("large/en/")));
		 long start = System.currentTimeMillis();
		 indexDocuments(corpus);
		 long finish = System.currentTimeMillis();
		 System.out.println("Time :"+(finish-start));
	}

	private static void  indexDocuments(ArrayList<File> corpus) {
		ArrayList<Document> documents = new ArrayList<>();
		for(File f : corpus){
			try {
				String file = new String(Files.readAllBytes(Paths.get(f.getPath())));
				Fetcher fetcher = new Fetcher(Jsoup.parse(file),"http://wikipedia.org/wiki/"+f.getName());
				MainTest.executor.execute(fetcher);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static List<File> getFiles(ArrayList<File> fileNames, Path dir) {
	    try(DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
	        for (Path path : stream) {
	            if(path.toFile().isDirectory()) {
	                getFiles(fileNames, path);
	            } else {
	                fileNames.add(path.toFile());
	                System.out.println(path.getFileName());
	            }
	        }
	    } catch(IOException e) {
	        e.printStackTrace();
	    }
	    return fileNames;
	} 
	
	
	private static void tryRecreateIndex(ArrayList<DocumentFile> words,ArrayList<DocumentFile> links) {
		if(words.size() > links.size()){
			comparateList(words, links , true);
		}else if(words.size() < links.size()){
			comparateList(links,words , false);
		}else{
			comparateList(words, links , true);
		}
		System.out.println("Size : "+Machine.data.size()+" "+Machine.data.toString());
	}
	
	
	private static ArrayList<DocumentData> comparateList(ArrayList<DocumentFile> bigList,ArrayList<DocumentFile> minList, boolean which){
		ArrayList<DocumentData> tokens = new ArrayList<>();
		System.out.println("recreating index");
		for(DocumentFile  min : minList){
			for(DocumentFile big : bigList){
				if(min.getUrl().equals(big.getUrl())){
					if(which)
						Machine.data.add(new DocumentData(min.getFile(),big.getFile() , min.getUrl(), big.getWords().length));
					else
						Machine.data.add(new DocumentData(big.getFile(),min.getFile() , min.getUrl(), min.getWords().length));
				}
			}
		}
		System.out.println("successful recreating index");
		return tokens;
	}

	private static ArrayList<DocumentFile> getListDocumentFile(ArrayList<File> document){
		ArrayList<DocumentFile>  list = new ArrayList<>();
		java.util.List<URL> urlList = Collections.synchronizedList(new ArrayList<URL>());
		
		Thread t1 = new Thread(()->{
			for(File f  : document){
				try{
					String content = new String(Files.readAllBytes(Paths.get(f.getPath())));
					DocumentFile file =  new Gson().fromJson(content,DocumentFile.class);
					if(file != null){
						file.setFile(f);
						list.add(file);
					}
					System.out.println(f.getName());
			
					
				}catch (Exception e) {
					// TODO: handle exception
				}
				
			}
		});
		
		Thread t2 = new Thread(()->{
			for(File f  : document){
				try{
					String content = new String(Files.readAllBytes(Paths.get(f.getPath())));
					DocumentFile file =  new Gson().fromJson(content,DocumentFile.class);
					if(file != null){
						file.setFile(f);
						list.add(file);
					}
					System.out.println(f.getName());
					
				}catch (Exception e) {
					// TODO: handle exception
				}
				
			}
		});

		
		
				return list;
	}

	private static  ArrayList<File> getFilesFromPath(String path){
		ArrayList<File> docs = new  ArrayList<>();
		try (Stream<Path> paths = Files.walk(Paths.get(path))) {
		    paths
		        .filter(Files::isRegularFile)
		        .forEach((t)->{docs.add(t.toFile());});
		  
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return docs;
	}
	
	
	private static void loadIndex() {
		File indexFile = new File("index.txt");
		String content = "{}";
		if(indexFile.exists()){
			try {
				content = new String(Files.readAllBytes(Paths.get("index.txt")));
			} catch (IOException e) {
				System.out.println("Error recovering the index");	
				e.printStackTrace();
			}    
		}
		SerializedIndex mIndex =  new Gson().fromJson(content,SerializedIndex.class);
		if(mIndex !=  null  && mIndex.getIndex()!= null){
			System.out.println("Index successful  loaded!");
			globalTokens = mIndex.getIndex();
		}
	}

	private static class SerializedIndex{
		@Expose
		@SerializedName("index")
		private HashMap<String, IndexEntry> index;
		@Expose
		@SerializedName("index_timestamp")
		private long timestamp;
		
		public HashMap<String, IndexEntry> getIndex() {
			return index;
		}

		public void setIndex(HashMap<String, IndexEntry> index) {
			this.index = index;
		}

		public long getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(long timestamp) {
			this.timestamp = timestamp;
		}
	}

	private static void saveIndex() {
		Gson gson = new Gson();
		SerializedIndex index = new SerializedIndex();
		index.setIndex(globalTokens);
		index.setTimestamp(new Timestamp(new Date().getTime()).getTime());
		saveFile(gson.toJson(index),"index.txt");	
	}
	
	private static void saveFile(String data,String name) {
		  PrintWriter writer;
		try {
			writer = new PrintWriter(name, "UTF-8");
			writer.println(data);
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	}
	
	//public static void main(String[] args) {
		//ainIndexer.mainIndexer();
	//}
}
