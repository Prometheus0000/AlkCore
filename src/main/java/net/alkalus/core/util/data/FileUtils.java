package net.alkalus.core.util.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

    public static boolean doesFileExist(final File f) {
        if (f != null && f.exists() && !f.isDirectory()) {
            return true;
        }
        return false;
    }

    public static boolean doesFileExist(final String filename, final String extension) {
        return doesFileExist(getWorkingDirectory(), filename, extension);
    }

    public static boolean doesFileExist(final String path, final String filename,
            final String extension) {
        return getFile(path, filename, extension) != null;
    }

    

    public static File createFolder(final String filename) {
        return createDirectory(filename);
    }    
    public static File createDirectory(final String filename) {
        return createDirectory(getWorkingDirectory(), filename);
    }    

    public static File createFolder(String path, final String filename) {
        return createDirectory(path, filename);        
    }
    public static File createDirectory(String path, final String filename) {
        return createFileObject_Base(path, filename, "", true);
    }
    
    

    public static File createFile(final String filename, final String extension) {
        return createFile(getWorkingDirectory(), filename, extension);
    }

    public static File createFile(String path, final String filename, String extension) {
        return createFileObject_Base(path, filename, extension, false);
    }
    
    
    /**
     * Creates a File, may be a directory. Wrapped by classes with clearer names and visibility.
     * @param path
     * @param filename
     * @param extension
     * @param isDirectory
     * @return
     */
    private static File createFileObject_Base(String path, final String filename, String extension, boolean isDirectory) {
        if (!extension.startsWith(".")) {
            extension = ("." + extension);
        }
        if (!path.endsWith("/")) {
            path = path + "/";
        }
        final File file = new File(path + filename + (!isDirectory ? extension : ""));
        boolean blnCreated = false;
        AcLog.INFO("Trying to use path " + file.getPath());
        try {
            AcLog.INFO("Trying to use path " + file.getCanonicalPath());
            AcLog.INFO("Trying to use absolute path " + file.getAbsolutePath());
            blnCreated = !isDirectory ? file.createNewFile() : file.mkdir();
        } catch (final IOException ioe) {
            AcLog.INFO("Error while creating a new empty file object :" + ioe + " | " + isDirectory);
            return null;
        }
        return blnCreated ? file : null;
    }

    public static boolean removeFile(final String filename, final String extension) {
        return removeFile(getWorkingDirectory(), filename, extension);
    }

    public static boolean removeFile(final String path, final String filename,
            final String extension) {
        if (doesFileExist(path, filename, extension)) {
            final File f = getFile(path, filename, extension);
            return removeFile(f);
        } else {
            return false;
        }
    }

    public static boolean removeFile(final File aFile) {
        if (aFile.exists()) {
            return aFile.delete();
        } else {
            return false;
        }
    }

    public static File getFile(final String filename, final String extension) {
        return getFile(getWorkingDirectory(), filename, extension);
    }

    public static File getFile(String path, final String filename, String extension) {
        if (path == null || path.length() <= 0) {
            path = "";
        } else {
            if (!path.endsWith("/")) {
                path = path + "/";
            }
        }
        if (filename == null || filename.length() <= 0) {
            return null;
        }
        if (extension == null || extension.length() <= 0) {
            extension = ".txt";
        } else {
            if (!extension.startsWith(".")) {
                extension = "." + extension;
            }
        }
        final File file = new File(path + filename + extension);
        final boolean doesExist = doesFileExist(file);

        if (doesExist) {
            AcLog.INFO("Found File: " + file.getAbsolutePath());
            return file;
        } else {
            AcLog.INFO("Creating file, as it was not found.");
            return createFile(path, filename, extension);
        }
    }

    public static boolean appendListToFile(final File file, final List<String> content) {
        try {
            long oldSize;
            long newSize;
            if (doesFileExist(file)) {
                final Path p = Paths.get(file.getPath());
                if (p != null && Files.isWritable(p)) {
                    oldSize = Files.size(p);
                    try {
                        Files.write(p, content, utf8,
                                StandardOpenOption.APPEND);
                    } catch (final IOException e) {
                        e.printStackTrace();
                    }
                    newSize = Files.size(p);
                    return newSize > oldSize;
                }
            }
        } catch (final IOException e) {
        }
        return false;
    }

    public static boolean appendLineToFile(final File file, final String content) {
        try {
            long oldSize;
            long newSize;
            if (doesFileExist(file)) {
                final Path p = Paths.get(file.getPath());
                if (p != null && Files.isWritable(p)) {
                    oldSize = Files.size(p);
                    try {
                        Writer output;
                        output = new BufferedWriter(new FileWriter(file, true));
                        output.append(content);
                        output.close();
                    } catch (final IOException e) {
                        e.printStackTrace();
                    }
                    newSize = Files.size(p);
                    return newSize > oldSize;
                }
            }
        } catch (final IOException e) {
        }
        return false;
    }

    public static String getWorkingDirectory() {
        final String workingDir = System.getProperty("user.dir");
        AcLog.WARNING("Current working directory : " + workingDir);
        return workingDir;
    }

    public static AutoMap<String> readLines(final File f) {
        if (f == null) {
            return new AutoMap<String>();
        }
        try {
            final List<String> aData = org.apache.commons.io.FileUtils.readLines(f,
                    utf8);
            final AutoMap<String> aValues = new AutoMap<String>();
            if (aData.isEmpty()) {
                return aValues;
            } else {
                for (final String g : aData) {
                    aValues.put(g);
                }
                return aValues;
            }
        } catch (final IOException e) {
            return new AutoMap<String>();
        }
    }

    public static String getFileExtension(final File aFile) {
        return getFileExtension(aFile.getAbsolutePath());
    }

    public static String getFileExtension(final String aAbsolutePathOfFile) {
        final Optional<String> a = Optional.ofNullable(aAbsolutePathOfFile)
                .filter(f -> f.contains(".")).map(f -> f
                        .substring(aAbsolutePathOfFile.lastIndexOf(".") + 1));
        return a.get();
    }
    
    public static boolean wipeFileIfExists(File aFile) {
        if (!removeFile(aFile)) {
            if (!doesFileExist(aFile)) {
                return false;
            }
        }        
        try {
            return aFile.createNewFile();
        } catch (IOException e) {
            return false;
        }
    }
    
    /**
     * Not a conversion, but it shows you how to read “input Stream” and write it into another new file via FileOutputStream
     * @param aDataStream - The Input Data.
     * @param aOutputFile - A blank File, mainly used for giving the FileOutputStream some data and then later, populting the File object with the Data Stream.
     * @return - a File containing the Input Stream.
     */
    public static File convertFileInputStream(InputStream aDataStream, File aOutputFile) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            // read this file into InputStream
            inputStream = aDataStream;

            // write the inputStream to a FileOutputStream
            outputStream = new FileOutputStream(aOutputFile);

            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    // outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return aOutputFile;
    }
    
    
    
    
    
    
    
    
    private static File bTempStorage_Internal;
    
    public static File getTempStorage() {
        if (bTempStorage_Internal != null) {
            return bTempStorage_Internal;
        }
        else {
            try {
               File f = File.createTempFile("GTPP_", ".tmp");
               if (f != null) {
                   File aTempFolder = f.getParentFile();
                   if (aTempFolder != null) {
                       return aTempFolder;
                   }
               }
            } catch (IOException e) {
            }
            return FileUtils.createDirectory("temp");
        }
    }

}
