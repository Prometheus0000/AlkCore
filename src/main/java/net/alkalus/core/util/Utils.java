package net.alkalus.core.util;

import java.awt.Color;
import java.awt.Graphics;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang3.EnumUtils;

import net.alkalus.api.objects.data.AutoMap;
import net.alkalus.api.objects.misc.AcLog;
import net.alkalus.core.locale.LocaleCache;
import net.alkalus.core.util.math.MathUtils;
import net.alkalus.core.util.sys.SystemUtils;

public class Utils {

	public static final int WILDCARD_VALUE = Short.MAX_VALUE;


	/**
	 *
	 * @param colourStr
	 *            e.g. "#FFFFFF"
	 * @return String - formatted "rgb(0,0,0)"
	 */
	public static String hex2RgbFormatted(final String hexString) {
		final Color c = new Color(Integer.valueOf(hexString.substring(1, 3), 16),
				Integer.valueOf(hexString.substring(3, 5), 16), Integer.valueOf(hexString.substring(5, 7), 16));

		final StringBuffer sb = new StringBuffer();
		sb.append("rgb(");
		sb.append(c.getRed());
		sb.append(",");
		sb.append(c.getGreen());
		sb.append(",");
		sb.append(c.getBlue());
		sb.append(")");
		return sb.toString();
	}

	/**
	 *
	 * @param colourStr
	 *            e.g. "#FFFFFF"
	 * @return
	 */
	public static Color hex2Rgb(final String colorStr) {
		return new Color(Integer.valueOf(colorStr.substring(1, 3), 16), Integer.valueOf(colorStr.substring(3, 5), 16),
				Integer.valueOf(colorStr.substring(5, 7), 16));
	}

	/**
	 *
	 * @param colourInt
	 *            e.g. 0XFFFFFF
	 * @return Colour
	 */
	public static Color hex2Rgb(final int colourInt) {
		return Color.decode(String.valueOf(colourInt));
	}

	/**
	 *
	 * @param colourInt
	 *            e.g. 0XFFFFFF
	 * @return short[]
	 */
	public static short[] hex2RgbShort(final int colourInt) {
		final Color rgb = Color.decode(String.valueOf(colourInt));
		final short[] rgba = { (short) rgb.getRed(), (short) rgb.getGreen(), (short) rgb.getBlue(),
				(short) rgb.getAlpha() };
		return rgba;
	}

	public static String byteToHex(final byte b) {
		final int i = b & 0xFF;
		return Integer.toHexString(i);
	}

	public static Object[] convertListToArray(final List<Object> sourceList) {
		final Object[] targetArray = sourceList.toArray(new Object[sourceList.size()]);
		return targetArray;
	}

	public static List<Object> convertArrayToFixedSizeList(final Object[] sourceArray) {
		final List<Object> targetList = Arrays.asList(sourceArray);
		return targetList;
	}

	public static List<Object> convertArrayToList(final Object[] sourceArray) {
		final List<Object> targetList = new ArrayList<>(Arrays.asList(sourceArray));
		return targetList;
	}

	public static List<Object> convertArrayListToList(final ArrayList<Object> sourceArray) {
		final List<Object> targetList = new ArrayList<Object>(Arrays.asList(sourceArray));
		return targetList;
	}

	public static int rgbtoHexValue(final int r, final int g, final int b) {
		if ((r > 255) || (g > 255) || (b > 255) || (r < 0) || (g < 0) || (b < 0)) {
			return 0;
		}
		final Color c = new Color(r, g, b);
		String temp = Integer.toHexString(c.getRGB() & 0xFFFFFF).toUpperCase();

		// System.out.println( "hex: " + Integer.toHexString( c.getRGB() &
		// 0xFFFFFF ) + " hex value:"+temp);
		temp = Utils.appenedHexNotationToString(String.valueOf(temp));
		//Logger.WARNING("Made " + temp + " - Hopefully it's not a mess.");
		//Logger.WARNING("It will decode into " + Integer.decode(temp) + ".");
		return Integer.decode(temp);
	}

	/*
	 * http://javadevnotes.com/java-left-pad-string-with-zeros-examples
	 */
	public static String padWithZerosLefts(final String originalString, final int length) {
		final StringBuilder sb = new StringBuilder();
		while ((sb.length() + originalString.length()) < length) {
			sb.append('0');
		}
		sb.append(originalString);
		final String paddedString = sb.toString();
		return paddedString;
	}
	
	public static String padWithZerosRight(final int value, final int length) {
		String originalString = String.valueOf(value);
		final StringBuilder sb = new StringBuilder();
		while ((sb.length() + originalString.length()) < length) {
			sb.append('0');
		}
		//sb.append(originalString);
		if (sb.length() > 0)
		originalString = (originalString + sb.toString());
		final String paddedString = sb.toString();
		return originalString;
	}

	/*
	 * Original Code by Chandana Napagoda -
	 * https://cnapagoda.blogspot.com.au/2011/03/java-hex-color-code-generator.
	 * html
	 */
	public static Map<Integer, String> hexColourGenerator(final int colorCount) {
		final int maxColorValue = 16777215;
		// this is decimal value of the "FFFFFF"
		final int devidedvalue = maxColorValue / colorCount;
		int countValue = 0;
		final HashMap<Integer, String> hexColorMap = new HashMap<>();
		for (int a = 0; (a < colorCount) && (maxColorValue >= countValue); a++) {
			if (a != 0) {
				countValue += devidedvalue;
				hexColorMap.put(a, Integer.toHexString(0x10000 | countValue).substring(1).toUpperCase());
			} else {
				hexColorMap.put(a, Integer.toHexString(0x10000 | countValue).substring(1).toUpperCase());
			}
		}
		return hexColorMap;
	}

	/*
	 * Original Code by Chandana Napagoda -
	 * https://cnapagoda.blogspot.com.au/2011/03/java-hex-color-code-generator.
	 * html
	 */
	public static Map<Integer, String> hexColourGeneratorRandom(final int colorCount) {
		final HashMap<Integer, String> hexColorMap = new HashMap<>();
		for (int a = 0; a < colorCount; a++) {
			String code = "" + (int) (Math.random() * 256);
			code = code + code + code;
			final int i = Integer.parseInt(code);
			hexColorMap.put(a, Integer.toHexString(0x1000000 | i).substring(1).toUpperCase());
			//Logger.WARNING("" + Integer.toHexString(0x1000000 | i).substring(1).toUpperCase());
		}
		return hexColorMap;
	}

	public static String appenedHexNotationToString(final Object hexAsStringOrInt) {
		final String hexChar = "0x";
		String result;
		if (hexAsStringOrInt.getClass() == String.class) {

			if (((String) hexAsStringOrInt).length() != 6) {
				final String temp = padWithZerosLefts((String) hexAsStringOrInt, 6);
				result = temp;
			}
			result = hexChar + hexAsStringOrInt;
			return result;
		} else if (hexAsStringOrInt.getClass() == Integer.class || hexAsStringOrInt.getClass() == int.class) {
			String aa = String.valueOf(hexAsStringOrInt);
			if (aa.length() != 6) {
				final String temp = padWithZerosLefts(aa, 6);
				result = temp;
			}
			else {
				result = hexChar + String.valueOf(hexAsStringOrInt);				
			}
			return result;
		} else {
			return null;
		}
	}

	public static Integer appenedHexNotationToInteger(final int hexAsStringOrInt) {
		final String hexChar = "0x";
		String result;
		AcLog.WARNING(String.valueOf(hexAsStringOrInt));
		result = hexChar + String.valueOf(hexAsStringOrInt);
		return Integer.getInteger(result);
	}

	public static boolean invertBoolean(final boolean booleans) {
		if (booleans == true) {
			return false;
		}
		return true;
	}
	
	public static String sanitizeString(final String input, final char[] aDontRemove) {
		
		String output;
		AutoMap<String> aToRemoveMap = new AutoMap<String>();

		aToRemoveMap.put(" ");
		aToRemoveMap.put("-");
		aToRemoveMap.put("_");
		aToRemoveMap.put("~");
		aToRemoveMap.put("?");
		aToRemoveMap.put("!");
		aToRemoveMap.put("@");
		aToRemoveMap.put("#");
		aToRemoveMap.put("$");
		aToRemoveMap.put("%");
		aToRemoveMap.put("^");
		aToRemoveMap.put("&");
		aToRemoveMap.put("*");
		aToRemoveMap.put("(");
		aToRemoveMap.put(")");
		aToRemoveMap.put("{");
		aToRemoveMap.put("}");
		aToRemoveMap.put("[");
		aToRemoveMap.put("]");
		aToRemoveMap.put(" ");
		
		for (String s : aToRemoveMap) {
			for (char e : aDontRemove) {
			if (s.charAt(0) == e) {
				aToRemoveMap.remove("s");
			}
			}
		}		
		output = input;
		for (String A : aToRemoveMap) {
			output = output.replace(A, "");
		}		
		return output;

	}

	public static String sanitizeString(final String input) {
		String temp;
		String output;

		temp = input.replace(" ", "");
		temp = temp.replace("-", "");
		temp = temp.replace("_", "");
		temp = temp.replace("?", "");
		temp = temp.replace("!", "");
		temp = temp.replace("@", "");
		temp = temp.replace("#", "");
		temp = temp.replace("(", "");
		temp = temp.replace(")", "");
		temp = temp.replace("{", "");
		temp = temp.replace("}", "");
		temp = temp.replace("[", "");
		temp = temp.replace("]", "");
		temp = temp.replace(" ", "");
		output = temp;
		return output;

	}

	public static String sanitizeStringKeepBrackets(final String input) {
		String temp;
		String output;

		temp = input.replace(" ", "");
		temp = temp.replace("-", "");
		temp = temp.replace("_", "");
		temp = temp.replace("?", "");
		temp = temp.replace("!", "");
		temp = temp.replace("@", "");
		temp = temp.replace("#", "");
		temp = temp.replace(" ", "");
		output = temp;
		return output;

	}

	public static String[] parseVersion(final String version) {
		return parseVersion(version, "//.");
	}

	public static String[] parseVersion(final String version, final String delimiter) {
		final String[] versionArray = version.split(delimiter);
		return versionArray;
	}

	public static Versioning compareModVersion(final String currentVersion, final String expectedVersion) {
		return compareModVersion(currentVersion, expectedVersion, "//.");
	}

	public static Versioning compareModVersion(final String currentVersion, final String expectedVersion,
			final String delimiter) {
		final String[] a = parseVersion(currentVersion, delimiter);
		final String[] b = parseVersion(expectedVersion, delimiter);
		final int[] c = new int[a.length];
		final int[] d = new int[b.length];
		for (int r = 0; r < a.length; r++) {
			c[r] = Integer.parseInt(a[r]);
		}
		for (int r = 0; r < b.length; r++) {
			d[r] = Integer.parseInt(b[r]);
		}
		final Versioning[] e = new Versioning[MathUtils.returnLargestNumber(c.length, d.length)];
		for (int r = 0; r < e.length; r++) {

			if (c[r] > d[r]) {
				e[r] = Versioning.NEWER;
			} else if (c[r] < d[r]) {
				e[r] = Versioning.OLDER;
			} else if (c[r] == d[r]) {
				e[r] = Versioning.EQUAL;
			}
		}

		for (int r = 0; r < e.length; r++) {
			if (e[0] == Versioning.NEWER) {
				return Versioning.NEWER;
			} else if (e[0] == Versioning.OLDER) {
				return Versioning.OLDER;
			} else {
				if (e[r] == Versioning.OLDER) {

				}

				return Versioning.NEWER;
			}
		}

		return null;
	}


	public static enum Versioning {
		EQUAL(0), NEWER(1), OLDER(-1);
		private final int versioningInfo;

		private Versioning(final int versionStatus) {
			this.versioningInfo = versionStatus;
		}

		public int getTexture() {
			return this.versioningInfo;
		}
	}
	
	public static SecureRandom generateSecureRandom(){
		SecureRandom secRan;
		String secRanType;		
		
		if (SystemUtils.isWindows()){
			secRanType = "Windows-PRNG";
		}
		else {
			secRanType = "NativePRNG";
		}		
		try {
			secRan = SecureRandom.getInstance(secRanType);
			// Default constructor would have returned insecure SHA1PRNG algorithm, so make an explicit call.
			byte[] b = new byte[64] ;
			secRan.nextBytes(b);
			return secRan;
		}
		catch (NoSuchAlgorithmException e) {
			return null;
		} 
	}	
	

	public static String calculateChecksumMD5(Object bytes) {  
		byte[] result = new byte[] {};
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		try {
		  out = new ObjectOutputStream(bos);   
		  out.writeObject(bytes);
		  out.flush();
		  result = bos.toByteArray();
		}
		catch (IOException e) {
		} finally {
		    try {
				bos.close();
			}
			catch (IOException e) {}
		}		
		return calculateChecksumMD5(result);
	}
	
	public static String calculateChecksumMD5(byte[] bytes) {        
	    MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
		    md.update(bytes);
		    byte[] digest = md.digest();
		    String myHash = DatatypeConverter.printHexBinary(digest).toUpperCase();
		    return myHash;
		}
		catch (NoSuchAlgorithmException e) {
			return null;
		}
	}

	public static String getCurrentTimeAndDate() {
		long yourmilliseconds = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");    
		Date resultdate = new Date(yourmilliseconds);
		return sdf.format(resultdate);
	}
	
	/*public static AutoMap<File> getAllResourcesOnClassPath(){
		File file;

		//file = new File(LocaleCache.class.getResource("locale/"+g+".lang").getFile());
		
		
	}*/

}
