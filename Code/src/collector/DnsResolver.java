
package collector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;


import sun.net.spi.nameservice.dns.*;
import java.rmi.UnknownHostException;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class DnsResolver {
	
	public interface OnAddressResolved{
		void onResolve(String address);
		void onError(Exception ex);
	}

	private static final String TAG = "DNSResolver: ";
	private static final int MAX_CACHED_ADDRESS = 5000;
	private static final String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) "
			+ "Chrome/56.0.2924.87 Safari/537.36";
	private static DnsResolver instance;
	private HashMap<String, InetAddress> addressTable;
	private HashMap<String, CacheMetadata> addressCache;
	private int totalCachedAddress = 0;
	private static final String ROBOTS_DIRECTORY = "robots/";

	private DnsResolver() {
		addressTable = new HashMap<>();
		addressCache = new HashMap<>();
	}

	private void indexAddress(InetAddress address, URI hostName) {
		addressTable.put(hostName.toString(), address);
		//logAddressInfo(address);
		ArrayList <InetAddress> addresses = new ArrayList<>();
		addresses.add(address);
		cacheAddress(new CacheMetadata(addresses, hostName, getParsedRootRobots(address)));
	}
	
	private boolean isCahedOnMemory(URI url){
		return addressCache.containsKey(url.getHost());
	}

	private void cacheAddress(CacheMetadata data) {
		//TODO save data cached on memory to disk when the memory cache overflow
		if (totalCachedAddress <= MAX_CACHED_ADDRESS) {
			cacheOnMemory(data);
		} else {
			clearMemoryCache();
			cacheOnMemory(data);
		}
	}

	private void cacheOnDisk(CacheMetadata data) {
		//TODO implement disk cache strategy
	}

	private void cacheOnMemory(CacheMetadata data) {
		// TODO handle disk cache 
		addressCache.put(data.getUrl().getHost(), data);
	}

	public static DnsResolver getInstance() {
		if (instance == null) {
			instance = new DnsResolver();
		}
		return instance;
	}

	public String resolveAddress(URI hostName) {
		String addr = "";
		if (isCached(hostName)) {
			addr = resolveWithCache(hostName);
		} else {
			addr = resolveWithDns(hostName);
		}
		return addr;
	}
	
	
	public boolean canDownloadData(URI url){
		if(url == null)
			return false;
		for(String path : getRobotRulesByUrl(url)){
			if(url.toString().contains(path.trim())){
				String[] paths  = url.toString().trim().split("/");
				for(String p : paths){
					if(p.equals("")|| p.equals(" "))
						continue;
					if(p.trim().equals(path.trim())){
						System.out.println("The url "+ url+ " violate the rule "+ path + "  dir : ---" + p+"---");
						return false;
					}
						
				}
			}
		}
		return true;
	}
	
	private void clearMemoryCache() {
		addressCache.clear();
		addressTable.clear();
	}

	private List<String> getRobotRulesByUrl(URI url) {
		List<String> rules = new ArrayList<>();
		if(isCahedOnMemory(url)){
			rules   = addressCache.get(url.getHost()).getDisalowedAddresses();			
		}else{
			resolveWithDns(url);
			rules = addressCache.get(url.getHost()).getDisalowedAddresses();
		}
		return rules;
	}

	private boolean isCached(URI hostName) {
		return isCahedOnMemory(hostName);
	}

	private String resolveWithCache(URI hostName) {
		return "";
	}

	private String resolveWithDns(URI hostName) {
		try {
	
			InetAddress ipAddress = Inet4Address.getByName(hostName.getHost().toString());//service.lookupAllHostAddr(hostName.toString());
			indexAddress(ipAddress, hostName);
			return ipAddress.getHostAddress();
		} catch (Exception  e) {
			System.out.println(TAG + "Invalid HostName");
		}  
		return "";
	}

	private void logAddressInfo(InetAddress address) {
        System.out.println(TAG + "Host IP Address: " + address.getHostAddress());
        System.out.println(TAG + "Host Name:  " + address.getHostName());
		System.out.println(TAG + "Host Canonicalname:  " + address.getCanonicalHostName());
	}

	
	private InputStreamReader getUrlStream(String address) {
		URL url;
		HttpsURLConnection conn = null ;
		try {
			url = new URL("https://" + address + "/robots.txt");
			conn =(HttpsURLConnection) url.openConnection();
			conn.setUseCaches(true);
			conn.setReadTimeout(0);
			conn.setConnectTimeout(0);
		} catch (MalformedURLException e) {
			 System.out.println("Malformed ulr error getting url stream :" + address);
		} catch (IOException e) {
			System.out.println("IO error getting url stream :" + address);
		}
	
		return verifyRedirects(conn);
	}
	private InputStreamReader verifyRedirects(HttpsURLConnection conn)   {
		InputStreamReader in =  null;
		try{
			int status = conn.getResponseCode();
			if (status != HttpsURLConnection.HTTP_OK) {
				if (status == HttpsURLConnection.HTTP_MOVED_TEMP || status == HttpsURLConnection.HTTP_MOVED_PERM|| status == HttpURLConnection.HTTP_SEE_OTHER){
					getUrlStream(conn.getHeaderField("Location"));
				}	 
			}else{
				in = new InputStreamReader(conn.getInputStream());
			}
		}catch (UnknownHostException e) {
			// TODO: handle  https connection
			System.out.println("Error handling redirects, possibly associated with https url + " + e.getCause().getMessage() );
		} catch (Exception e) {
			System.out.println("IO error handling redirects ");
		}
		
		return in;
	}

	private ArrayList<String> getRobotsTxt(String address) {
		ArrayList<String> lines = new ArrayList<>();
		File robot = new File(ROBOTS_DIRECTORY+"robots_"+address+".txt");
		try {
			BufferedReader in = new BufferedReader(getUrlStream(address));	
			FileOutputStream outputStream = new FileOutputStream(robot);
			String line = null;
			while ((line = in.readLine()) != null) {
				lines.add(line);
				line+="\n";
				outputStream.write(line.getBytes());
			}
			in.close();
			outputStream.close();
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println("IO error geting robots.txt :" + address);
		}
		return lines;
	}

	private List<String> getParsedRootRobots(InetAddress address) {
		return parseRobotsContent(getRobotsTxt(address.getHostName()));
	}

	private List<String> parseRobotsContent(ArrayList<String> text) {
		ArrayList<String> disallowed = new ArrayList<>();
		String[] rule = null;
		for (String line : text) {
			if (line.contains("Disallow")) {
				line.trim();
				rule = line.split(":");
				if(rule.length>1){
					if(!disallowed.contains(rule[1].trim()))
						disallowed.add(rule[1].trim());
				}
			}
		}
		return disallowed;
	}
	
	

	private class CacheMetadata implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -8219934947788596754L;
		private List<InetAddress> addresses;
		private URI url;
		private List<String> disalowedAddresses;
		private int timesUsed;
		private long lastUse;

 

		public List<InetAddress> getAddress() {
			return addresses;
		}

		public void setAddress(List<InetAddress> address) {
			this.addresses = address;
		}

		public int getTimesUsed() {
			return timesUsed;
		}

		public void setTimesUsed(int timesUsed) {
			this.timesUsed = timesUsed;
		}

		public long getLastUse() {
			return lastUse;
		}

		public void setLastUse(long lastUse) {
			this.lastUse = lastUse;
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
			return "Metadata [address=" + addresses + ", url=" + url + ", disalowedAddresses="
					+ disalowedAddresses.toString() + "]";
		}

		public CacheMetadata(List<InetAddress> address, URI url, List<String> disalowedAddresses) {
			this.addresses = address;
			this.url = url;
			this.disalowedAddresses = disalowedAddresses;
		}

	}
}


