package collector;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.*;
import org.jsoup.Connection.Response;

import java.net.InetAddress;
import java.net.URI;
import java.net.URL;

import sun.net.spi.nameservice.dns.*;
import java.rmi.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.management.InstanceAlreadyExistsException;

public class DnsResolver{
	
	private static DnsResolver instance;
	private static final String TAG ="DNSResolver: "; 
	private HashMap<String,InetAddress> addressTable;
	private RequestHandler requestHandler;
	private static final String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) "
			+ "Chrome/56.0.2924.87 Safari/537.36";
	
	private DnsResolver() {
		addressTable = new HashMap<>();
		requestHandler = new RequestHandler();
	}
	
	private void indexAddress(InetAddress address, URI hostName){
		addressTable.put(hostName.toString(), address);
		logAddressInfo(address);
		getRootRobots(address);
	}
	
	public static DnsResolver getInstance(){
		if(instance == null ){
			instance = new DnsResolver();
		}
		return instance;
	}
	
	
	public void resolveAddress(URI hostName){
		if(isCached(hostName)){
			resolveWithCache(hostName);
		}else{
			resolveWithDns(hostName);
		}

	}
	
	private boolean isCached(URI hostName) {
		return false;
	}

	private void resolveWithCache(URI hostName) {		
	}

	private void resolveWithDns(URI hostName){
		try{
			DNSNameService service = new DNSNameService();
			InetAddress[] ipAddress = service.lookupAllHostAddr(hostName.toString());
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
			byte[] doc = Jsoup.connect("http://"+address.getHostAddress()+"/robots.txt")
					.userAgent(userAgent)
					.execute()
					.bodyAsBytes();
			parseRobotsContent(Arrays.toString(doc));
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	private void parseRobotsContent(String text) {
		String regex = "Disallow:";
	    Pattern pattern = Pattern.compile(regex,Pattern.DOTALL);
	    Matcher matcher = pattern.matcher(text);
	    System.out.println(text);
	}
	
 
	 


}

