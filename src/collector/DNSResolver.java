package collector;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.*;
import java.net.InetAddress;
import sun.net.spi.nameservice.dns.*;
import java.rmi.UnknownHostException;
import java.util.HashMap;

public class DNSResolver{
	private static final String TAG ="DNSResolver: "; 
	private HashMap<String,InetAddress> addressTable;
	private RequestHandler requestHandler;
	public DNSResolver() {
		addressTable = new HashMap<>();
		requestHandler = new RequestHandler();
	}
	
	private void indexAddress(InetAddress address, String hostName){
		addressTable.put(hostName, address);
		logAddressInfo(address);
		getRootRobots(address);
	}
	
	public void resolveAddress(String hostName){
		try{
			DNSNameService service = new DNSNameService();
			InetAddress[] ipAddress = service.lookupAllHostAddr(hostName);
			indexAddress(ipAddress[1],hostName);
		}catch (UnknownHostException e){
			System.out.println(TAG+"Invalid HostName");
		}catch(Exception e){e.printStackTrace();}
	}
	
	
	private void logAddressInfo(InetAddress address){
		System.out.println(TAG+"Host IP Address: "+address.getHostAddress());
		System.out.println(TAG+"Host Name:  "+ address.getHostName());
		System.out.println(TAG+"Host Canonicalname:  "+ address.getCanonicalHostName());
	}
	
	private void getRootRobots(InetAddress address){
		try {
			Document doc = Jsoup.connect("http://"+address.getHostAddress()+"/robots.txt")
					.userAgent("GunMar-DNSResolver")
					.get();
			parseRobotsBody(doc.body().text());
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	private void parseRobotsBody(String text) {
	
		
	}
	 


}

