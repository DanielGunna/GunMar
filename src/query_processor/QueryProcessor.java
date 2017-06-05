package query_processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QueryProcessor {
	
	private Ranking ranking;
	private String   query;
	
	public class QueryToken{
		private String token;
		private int frequency;
		private int position;
		
		public QueryToken(String token, int f ,  int p){
			this.setToken(token);
			frequency = f;
			position = p;
		}
		
	
		public void incrementFrequence(){
			frequency++;
		}


		public String getToken() {
			return token;
		}


		public void setToken(String token) {
			this.token = token;
		}
	}
	
	private QueryProcessor(){
		ranking = new Ranking();
	}
	
	
	public void search(String query){
		this.query = query;
		ranking.performSearch(getQueryTokens(query));
	}
	
	
	private HashMap<String,QueryToken> getQueryTokens(String query){
		String[]  terms = query.split("");
		HashMap<String,QueryToken> tokens  = new HashMap<>();
		int x  = 0 ;
		for (String t : terms){
			if(tokens.containsKey(t)){
				tokens.get(t).incrementFrequence();
			}else{
				tokens.put(t,new QueryToken(t,1,x));
			}
			x++;
		}
		return tokens;
	}
	
	
	
 

}
