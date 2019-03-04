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

    public static String determineUsersCountry() {
        try {
            if (NetworkUtils.checkNetworkIsAvailableWithValidInterface()) {
                return getUsersCountry();
            } else {
                return "Offline.";
            }
        } catch (final Throwable T) {
            AcLog.INFO("Failed to initialise GeoUtils.");
            return "Failed.";
        }
    }

    private static String getUsersIPAddress() {
        try {
            final String webPage = "http://checkip.amazonaws.com/";
            final URL url = new URL(webPage);
            final URLConnection urlConnection = url.openConnection();
            final InputStream is = urlConnection.getInputStream();
            final InputStreamReader isr = new InputStreamReader(is);
            int numCharsRead;
            final char[] charArray = new char[1024];
            final StringBuffer sb = new StringBuffer();
            while ((numCharsRead = isr.read(charArray)) > 0) {
                sb.append(charArray, 0, numCharsRead);
            }
            isr.close();
            final String result = sb.toString();
            return result;
        } catch (final IOException e) {
        }
        return "Error getting users IP.";
    }

    private static String getUsersCountry() {

        // Get the IP
        final String ipAddress = getUsersIPAddress();

        // Build a URL
        final URIBuilder builder = new URIBuilder().setScheme("http")
                .setHost("ipinfo.io").setPath("/" + ipAddress + "/country/");

        URI uri;
        try {
            // Convert the URI Builder to a URI, then to a URL
            uri = builder.build();
            final URL url = uri.toURL();

            // Main Check method
            try {
                final URLConnection urlConnection = url.openConnection();
                final InputStream is = urlConnection.getInputStream();
                final InputStreamReader isr = new InputStreamReader(is);
                int numCharsRead;
                final char[] charArray = new char[1024];
                final StringBuffer sb = new StringBuffer();
                while ((numCharsRead = isr.read(charArray)) > 0) {
                    sb.append(charArray, 0, numCharsRead);
                }
                final String temp = sb.toString();
                final String result = temp.replaceAll("(\\r|\\n)", "");
                isr.close();
                return result;
                // Catch block for bad connection
            } catch (final IOException e) {
                AcLog.INFO("Method 1 - Failed.");
            }

            // Secondary method
            try (java.util.Scanner s = new java.util.Scanner(url.openStream(),
                    "UTF-8").useDelimiter("\\A")) {
                final String r = s.next();
                return r.replaceAll("(\\r|\\n)", "");
                // Catch block for bad connection
            } catch (final java.io.IOException e) {
                AcLog.INFO("Method 2 - Failed.");
            }

        }
        // Catch block for all the Bad URI/URL building
        catch (URISyntaxException | MalformedURLException e1) {
            if (e1 instanceof URISyntaxException) {
                AcLog.INFO("Bad URI Syntax for builder.");
            } else {
                AcLog.INFO("Malformed URL.");
            }
            AcLog.INFO("Country Check - Failed.");
        }
        return "Error getting users Country. " + ipAddress;
    }

}
