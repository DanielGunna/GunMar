package query_processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import indexer.MainIndexer;
import indexer.MainIndexer.IndexEntry.DocumentEntry;
import query_processor.QueryProcessor.QueryToken;

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

	public void performSearch(HashMap<String,QueryToken> query) {
		ArrayList<ArrayList<DocumentEntry>> documents = new ArrayList<>();
		
		
		for(Map.Entry<String,QueryToken> entry : query.entrySet()){
			if(MainIndexer.globalTokens.containsKey(entry.getValue().getToken())){
				documents.add(MainIndexer.globalTokens.get(entry.getValue().getToken()).getFilesEntries());
			}
		}
		
		ArrayList<Double> idfs = new ArrayList<>();
		for(ArrayList<DocumentEntry> list : documents){
			
		}
		
		
	
	}
}
