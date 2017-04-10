package collector;

 

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;

import sun.net.spi.nameservice.dns.*;
import java.rmi.UnknownHostException;
import java.util.ArrayList;
 
import java.util.HashMap;
import java.util.List;
 

public class DnsResolver{
	
	
	private static final String TAG ="DNSResolver: "; 
	private static final int MAX_CACHED_ADDRESS = 5000;
	private static final String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) "
											+ "Chrome/56.0.2924.87 Safari/537.36";
	private static DnsResolver instance;
	private HashMap<String,InetAddress> addressTable;
	private HashMap<String,CacheMetadata> addressCache;
	private int totalCachedAddress = 0 ;
	

	private DnsResolver() {
		addressTable = new HashMap<>();
		addressCache = new HashMap<>();
	}
	
	private void indexAddress(InetAddress address, URI hostName){
		addressTable.put(hostName.toString(), address);
		logAddressInfo(address);
		cacheAddress(new CacheMetadata(address, hostName,getParsedRootRobots(address)));
	}
	
	private void cacheAddress(CacheMetadata data) {
		if(totalCachedAddress <= MAX_CACHED_ADDRESS){
			cacheOnMemory(data);
		}else{
			cacheOnDisk(data);
		}
	}
		
	private void cacheOnDisk(CacheMetadata data) {
	}

	private void cacheOnMemory(CacheMetadata data) {
		addressCache.put(data.getUrl().getHost(), data); 
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
	
	private ArrayList<String> getRobotsTxt(InetAddress address){
		ArrayList<String> lines = new ArrayList<>();
		try{
			URL url = new URL("http://"+address.getHostAddress()+"/robots.txt");
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			String line = null;
	        while((line = in.readLine()) != null) {
	            lines.add(line);
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
		return  lines;
	}
	
	private List<String> getParsedRootRobots(InetAddress address){
		return parseRobotsContent(getRobotsTxt(address));		
	}

	private List<String> parseRobotsContent(ArrayList<String> text) {
		ArrayList<String> disallowed= new ArrayList<>();
		for(String  line : text ){
			if(line.contains("Disallow")){
				disallowed.add(line.split(":")[1].trim());
			}
		}
		System.out.print(disallowed.toString());
	    return disallowed;
	}
	
	private class CacheMetadata implements Serializable{
		private InetAddress address;
		private URI url;
		private List<String> disalowedAddresses;
		private int timesUsed;
		private long lastUse;
		
		public InetAddress getAddress() {
			return address;
		}
		public void setAddress(InetAddress address) {
			this.address = address;
		}
		public URI getUrl() {
			return url;
		}
		public void setUrl(URI url) {
			this.url = url;
		}
		public List<String> getDisalowedAddresses() {
			return disalowedAddresses;
		}
		public void setDisalowedAddresses(List<String> disalowedAddresses) {
			this.disalowedAddresses = disalowedAddresses;
		}
		@Override
		public String toString() {
			return "Metadata [address=" + address + ", url=" + url + ", disalowedAddresses=" + disalowedAddresses.toString() + "]";
		}
		public CacheMetadata(InetAddress address, URI url, List<String> disalowedAddresses) {
			this.address = address;
			this.url = url;
			this.disalowedAddresses = disalowedAddresses;
		}
			
	}
}

