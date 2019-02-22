package net.alkalus.core.util.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Optional;

import net.alkalus.api.objects.data.AutoMap;
import net.alkalus.api.objects.misc.AcLog;

public class FileUtils {

	private static final Charset utf8 = StandardCharsets.UTF_8;

	public static boolean doesFileExist(File f) {
		if (f != null && f.exists() && !f.isDirectory()) {
			return true;
		}
		return false;
	}
	

	public static boolean doesFileExist(String filename, String extension) {
		return doesFileExist(getWorkingDirectory(), filename, extension);
	}
	
	public static boolean doesFileExist(String path, String filename, String extension) {
		return getFile(path, filename, extension) != null;
	}
	
	public static File createFile(String filename, String extension) {
		return createFile(getWorkingDirectory(), filename, extension);
	}

	public static File createFile(String path, String filename, String extension) {		
		if (!extension.startsWith(".")) {
			extension = ("."+extension);
		}
		if (!path.endsWith("/")) {
			path = path + "/";
		}
		File file = new File(path + filename + extension);
		boolean blnCreated = false;
		AcLog.INFO("Trying to use path "+file.getPath());
		try {
			AcLog.INFO("Trying to use path "+file.getCanonicalPath());
			AcLog.INFO("Trying to use absolute path "+file.getAbsolutePath());
			blnCreated = file.createNewFile();
		} catch (IOException ioe) {
			AcLog.INFO("Error while creating a new empty file :" + ioe);
			return null;
		}
		return blnCreated ? file : null;
	}
	
	public static boolean removeFile(String filename, String extension) {
		return removeFile(getWorkingDirectory(), filename, extension);
	}
	
	public static boolean removeFile(String path, String filename, String extension) {		
		if (doesFileExist(path, filename, extension)) {
			File f = getFile(path, filename, extension);
			return removeFile(f);
		}
		else {
			return false;
		}		
	}
	
	public static boolean removeFile(File aFile) {
		if (aFile.exists()) {
			return aFile.delete();
		}
		else {
			return false;
		}
	}

	public static File getFile(String filename, String extension) {
		return getFile(getWorkingDirectory(), filename, extension);
	}

	public static File getFile(String path, String filename, String extension) {
		if (path == null || path.length() <= 0) {
			path = "";
		}
		else {
			if (!path.endsWith("/")) {
				path = path + "/";
			}
		}
		if (filename == null || filename.length() <= 0) {
			return null;
		}
		if (extension == null || extension.length() <= 0) {
			extension = ".txt";
		}
		else {
			if (!extension.startsWith(".")) {
				extension = "." + extension;				
			}			
		}
		File file = new File(path + filename + extension);
		boolean doesExist = doesFileExist(file);

		if (doesExist) {
			AcLog.INFO("Found File: " + file.getAbsolutePath());
			return file;
		} else {
			AcLog.INFO("Creating file, as it was not found.");
			return createFile(path, filename, extension);	
		}
	}

	public static boolean appendListToFile(File file, List<String> content) {
		try {
			long oldSize;
			long newSize;
			if (doesFileExist(file)) {
				Path p = Paths.get(file.getPath());		
				if (p != null && Files.isWritable(p)) {
					oldSize = Files.size(p);	
					try {	
						Files.write(p, content, utf8, StandardOpenOption.APPEND);
					} catch (IOException e) {
						e.printStackTrace();
					}
					newSize = Files.size(p);
					return newSize > oldSize;
				}
			}
		} catch (IOException e) {
		}
		return false;
	}
	
	public static boolean appendLineToFile(File file, String content) {
		try {
			long oldSize;
			long newSize;
			if (doesFileExist(file)) {
				Path p = Paths.get(file.getPath());		
				if (p != null && Files.isWritable(p)) {
					oldSize = Files.size(p);	
					try {						
						Writer output;
						output = new BufferedWriter(new FileWriter(file, true)); 
						output.append(content);
						output.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					newSize = Files.size(p);
					return newSize > oldSize;
				}
			}
		} catch (IOException e) {
		}
		return false;
	}
	
	public static String getWorkingDirectory() {
		   String workingDir = System.getProperty("user.dir");
		   AcLog.WARNING("Current working directory : " + workingDir);
		   return workingDir;
	}
	
	public static AutoMap<String> readLines(File f){
		try {
			List<String> aData = org.apache.commons.io.FileUtils.readLines(f, utf8);
			AutoMap<String> aValues = new AutoMap<String>();
			if (aData.isEmpty()) {
				return aValues;
			}
			else {
				for (String g : aData) {
					aValues.put(g);
				}
				return aValues;
			}			
		} catch (IOException e) {
			return new AutoMap<String>();
		}
	}


	public static String getFileExtension(File aFile) {
		return getFileExtension(aFile.getAbsolutePath());
	}
	
	public static String getFileExtension(String aAbsolutePathOfFile) {
		Optional<String> a = Optional.ofNullable(aAbsolutePathOfFile).filter(f -> f.contains(".")).map(f -> f.substring(aAbsolutePathOfFile.lastIndexOf(".") + 1));
		return a.get();
	}
	
}
