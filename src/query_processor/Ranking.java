package query_processor;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import indexer.MainIndexer;
import indexer.MainIndexer.IndexEntry;
import indexer.MainIndexer.IndexEntry.DocumentEntry;
import main.Machine;
import main.Machine.DocumentData;
import query_processor.QueryProcessor.QueryToken;
import utils.stringsimilarity.Cosine;

public class Ranking {
	

    public double bm25Score(double tf, double numberOfDocuments, double docLength, double averageDocumentLength, double queryFrequency, double documentFrequency) {
		double K = 1.2d * ((1 - 0.75d) + ((0.75d * docLength) / averageDocumentLength));
		double weight = ( ((1.2d + 1d) * tf) / (K + tf) );   //first part
		weight = weight * ( ((8d + 1) * queryFrequency) / (8d + queryFrequency) );    //second part
		double idf = weight * Math.log((numberOfDocuments - documentFrequency + 0.5d) / (documentFrequency + 0.5d));   
		return idf;
	}
    
    public double tfNormalization(int tf) {	
    	return 1 + Math.log(tf);
    }
    
    private class RankEntry implements Comparable<RankEntry>{
    	@Expose
    	@SerializedName("document")
    	public DocumentEntry documentEntry;
    	@Expose
    	@SerializedName("document_score")
    	public double score;
		@Override
		public int compareTo(RankEntry o) {
			return (o.score > this.score) ? -1 : (o.score == score) ? 0 : 1;
		}
		public RankEntry(DocumentEntry documentEntry, double score) {
			super();
			this.documentEntry = documentEntry;
			this.score = score;
		}
		
		@Override
		public String toString() {
			return new Gson().toJson(this);
		}
		public void incrementScore(double newScore) {
			score+=newScore;
		}
	
    }

	public void performSearch(HashMap<String,QueryToken> query) {
		HashMap<String,RankEntry> idfs = new HashMap<>();
		int qtDocument = new File("docs").list().length;
		HashMap<String,IndexEntry> index = MainIndexer.globalTokens;
		for(Map.Entry<String,QueryToken> entry : query.entrySet()){
			for(Map.Entry<String, IndexEntry> token : index.entrySet()) {
				Cosine  comparator = new Cosine();
				String key = token.getKey();
				if(comparator.similarity(key, entry.getValue().getToken()) >= 0.8){
					IndexEntry a = MainIndexer.globalTokens.get(key);
					for(DocumentEntry doc : a.getFilesEntries()) {
						double docScore = bm25Score(tfNormalization(doc.getFileOcurrencies()), qtDocument, doc.getFileLength(), getAvgDocumentLength(),entry.getValue().getFrequency(),a.getFilesEntries().size());
						if(idfs.containsKey(doc.getFileUrl())) {
							idfs.get(doc.getFileUrl()).incrementScore(docScore);
						}else {
							idfs.put(doc.getFileUrl() ,new RankEntry(doc,docScore));
						}
					}
				}
			}
		}
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> "
				+ "\nRESULT"+idfs.toString() + "index size "+ MainIndexer.globalTokens.size());
	}
	

	private double getAvgDocumentLength() {
		 int sumDocLenth = 0 ; 
		 for(DocumentData data : Machine.data) {
			sumDocLenth+=data.getFileLength();
		 }
		return sumDocLenth/Machine.data.size();
			 
	}
}
