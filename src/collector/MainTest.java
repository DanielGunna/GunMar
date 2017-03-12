package collector;

import org.jsoup.nodes.Document;

public class MainTest {
	
	public static void main(String[] args){
		//try{
		//	Document doc  =  RequestHandler.getInstance().getDataFromUrl("https://www.youtube.com/watch?v=F-XhH1FTKm0");
	//		System.out.println(doc.toString());
	//	}catch (Exception e ){
	//		e.printStackTrace();
		//}
		new DNSResolver().resolveAddress("www.google.com");
	}

}
