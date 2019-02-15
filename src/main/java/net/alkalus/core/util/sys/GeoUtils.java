package net.alkalus.core.util.sys;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.client.utils.URIBuilder;

import net.alkalus.api.objects.misc.AcLog;

public class GeoUtils {

	public static String determineUsersCountry(){
		try {
		if (NetworkUtils.checkNetworkIsAvailableWithValidInterface()){
			return getUsersCountry();			
		}
		else {
			return "Offline.";
		}
		} catch (Throwable T){
			AcLog.INFO("Failed to initialise GeoUtils.");
			return "Failed.";
		}
	}

	private static String getUsersIPAddress() {
		try {
			String webPage = "http://checkip.amazonaws.com/";
			URL url = new URL(webPage);
			URLConnection urlConnection = url.openConnection();
			InputStream is = urlConnection.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			int numCharsRead;
			char[] charArray = new char[1024];
			StringBuffer sb = new StringBuffer();
			while ((numCharsRead = isr.read(charArray)) > 0) {
				sb.append(charArray, 0, numCharsRead);
			}
			isr.close();
			String result = sb.toString();
			return result;
		} catch (IOException e) {}
		return "Error getting users IP.";
	}

	private static String getUsersCountry() {
		
		//Get the IP
		String ipAddress = getUsersIPAddress();
		
		//Build a URL
		URIBuilder builder = new URIBuilder()
	            .setScheme("http")
	            .setHost("ipinfo.io")
	            .setPath("/"+ipAddress+"/country/");

	    URI uri;
		try {
			//Convert the URI Builder to a URI, then to a URL
			uri = builder.build();
		    URL url = uri.toURL();
		    
		    //Main Check method
		    try {
				URLConnection urlConnection = url.openConnection();
				InputStream is = urlConnection.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);
				int numCharsRead;
				char[] charArray = new char[1024];
				StringBuffer sb = new StringBuffer();
				while ((numCharsRead = isr.read(charArray)) > 0) {
					sb.append(charArray, 0, numCharsRead);
				}
				String temp = sb.toString();
				String result = temp.replaceAll("(\\r|\\n)", "");
				isr.close();
				return result;
				//Catch block for bad connection
			} catch (IOException e) {
				AcLog.INFO("Method 1 - Failed.");
			}  
		    
		    //Secondary method
		    try (java.util.Scanner s = new java.util.Scanner(url.openStream(), "UTF-8").useDelimiter("\\A")) {
				String r = s.next();
				return r.replaceAll("(\\r|\\n)", "");
				//Catch block for bad connection
			} catch (java.io.IOException e) {
				AcLog.INFO("Method 2 - Failed.");			
			}
		    
		}
		//Catch block for all the Bad URI/URL building
		catch (URISyntaxException | MalformedURLException e1) {
			if (e1 instanceof URISyntaxException){
				AcLog.INFO("Bad URI Syntax for builder.");
			}
			else {
				AcLog.INFO("Malformed URL.");				
			}
			AcLog.INFO("Country Check - Failed.");
		}
		return "Error getting users Country. "+ipAddress;
	}

}
