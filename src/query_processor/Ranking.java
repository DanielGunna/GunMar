package query_processor;

public class Ranking {

    public double bm25Score(double tf, double numberOfDocuments, double docLength, double averageDocumentLength, double queryFrequency, double documentFrequency) {
	
		double K = 1.2d * ((1 - 0.75d) + ((0.75d * docLength) / averageDocumentLength));
		double weight = ( ((1.2d + 1d) * tf) / (K + tf) );   //first part
		weight = weight * ( ((8d + 1) * queryFrequency) / (8d + queryFrequency) );    //second part
		
		// multiply the weight with idf
		double idf = weight * Math.log((numberOfDocuments - documentFrequency + 0.5d) / (documentFrequency + 0.5d));   
		return idf;
	}
    
    public double tfNormalization(int tf) {	
    	return 1 + Math.log(tf);
    }
}
