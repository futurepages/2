package org.futurepages.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

/**
 * Encapsulates all file operations.
 */
public class FileUtil {

	private static FileUtil instance;

	private FileUtil() {}

	public static FileUtil getInstance() {
		if (instance == null) {
			instance = new FileUtil();
		}
		return instance;
	}

	public static String getStringContent(String path) throws FileNotFoundException, IOException {
		File file = new File(path);

		if (!file.exists()) {
			return null;
		}

		FileReader fr = new FileReader(file);
		char[] buffer = new char[(int) file.length()];
		fr.read(buffer);
		fr.close();
		return new String(buffer);
	}

	public static String getStringContent(Class cls, String path) throws FileNotFoundException, IOException {
		return getStringContent(classRealPath(cls)+"/"+path);
	}

	public static String[] getStringLines(Class cls, String path) throws FileNotFoundException, IOException {
		return getStringLines(classRealPath(cls)+"/"+path);
	}

	public static String[] getStringLines(String path) throws FileNotFoundException, IOException {
		return getStringContent(path).split("\r\n");
	}

	public static void putKeyValue(Map<String, String> map, URL srcURL, String targetPath) throws Exception {
		putStrings(map, srcURL, targetPath);
	}

	public static void putStrings(Map<String, String> map, URL sourceUrl, String targetUrl) throws Exception {
		BufferedInputStream source = new BufferedInputStream(sourceUrl.openStream());
		BufferedOutputStream target = null;
		try {
			byte[] content = replaceAll(map, source).getBytes();
			target = new BufferedOutputStream(new FileOutputStream(targetUrl));
			target.write(content);
			target.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (target != null) {
				target.close();
			}
		}
	}

	private static String replaceAll(Map<String, String> map, InputStream source) throws IOException {
		try {
			if (source == null) {
				throw new Exception(new FileNotFoundException("File doesn´t exist."));
			}
			byte[] content = new byte[source.available()];
			source.read(content);
			String layout = new String(content);

			for (String key : map.keySet()) {
				layout = StringUtils.replace(layout, key, map.get(key));
			}
			return layout;
		} catch (Exception e) {
			return "";
		} finally {
			if (source != null) {
				source.close();
			}
		}
	}

	/**
	 * Puts the given items into the given key´s place by replacing it.
	 * @param items
	 * @param key
	 * @param fileUrl
	 * @throws Exception
	 */
	public static void putKeyValue(String key, String value, String sourceUrl) throws Exception {
		putKeyValue(key, value, sourceUrl, sourceUrl);
	}

	public static void putKeyValue(Map<String, String> map, String sourceUrl) throws Exception {
		putKeyValue(map, sourceUrl, sourceUrl);
	}

	/**
	 * Puts the given items into the given key´s place by replacing it.
	 * @param key
	 * @param value
	 * @param sourceUrl
	 * @param targetUrl
	 * @throws Exception
	 */
	public static void putKeyValue(String key, String value, String sourceUrl, String targetUrl) throws Exception {
		Map<String, String> strings = new HashMap<String, String>();
		strings.put(key, value);
		putStrings(strings, sourceUrl, targetUrl);
	}

	public static void putKeyValue(Map<String, String> map, String sourceUrl, String targetUrl) throws Exception {
		putStrings(map, sourceUrl, targetUrl);
	}

	/**
	 * Replaces the file keys with the given key-value map.
	 * @param map
	 * @param sourceUrl
	 * @return String
	 * @throws Exception
	 */
	public static String replaceAll(Map<String, String> map, String sourceUrl) throws Exception {
		return replaceAll(map, new FileInputStream(sourceUrl));
	}

	/**
	 * Puts the given strings into the file.
	 * @param map
	 * @param sourceUrl
	 * @param targetUrl
	 * @throws Exception
	 */
	public static void putStrings(Map<String, String> map, String sourceUrl, String targetUrl) throws Exception {
		FileOutputStream target = null;
		try {
			byte[] content = replaceAll(map, sourceUrl).getBytes();
			target = new FileOutputStream(targetUrl);
			target.write(content);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (target != null) {
				target.close();
			}
		}
	}

	public static void createTextFile(String content, String targetUrl) throws Exception {
		FileOutputStream target = null;
		try {
			byte[] contentBytes = content.getBytes();
			target = new FileOutputStream(targetUrl);
			target.write(contentBytes);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (target != null) {
				target.close();
			}
		}
	}

	public static boolean deleteFile(String pathArquivo) {
		boolean success = (new File(pathArquivo)).delete();
		return success;
	}

	public static void moveFileToDirectory(String pathArquivo, String pathDiretorio) throws Exception {
		// File (or directory) to be moved
		File file = new File(pathArquivo);

		// Destination directory
		File dir = new File(pathDiretorio);

		// Move file to new directory
		boolean success = file.renameTo(new File(dir, file.getName()));
		if (!success) {
			throw new Exception("Não foi possível renomear/mover arquivo.");
		}
	}

	public static String extensionFormat(String pathFileName) {
		String[] explodedName = pathFileName.split("\\.");
		return explodedName[explodedName.length - 1].toLowerCase();
	}

	public static String classRealPath(Class<?> klass) throws UnsupportedEncodingException {
		return EncodingUtil.correctPath(klass.getResource("").getPath());
	}

	public static void copy(String fromFileName, String toFileName) throws IOException {

		File fromFile = new File(fromFileName);
		File toFile = new File(EncodingUtil.correctPath(toFileName));
		if (toFile.isDirectory()) {
			toFile = new File(toFile, fromFile.getName());
		}

		if (!toFile.exists()) {
			String parent = toFile.getParent();
			if (parent == null) {
				parent = System.getProperty("user.dir");
			}
		}

		FileInputStream from = null;
		FileOutputStream to = null;
		try {
			from = new FileInputStream(fromFile);
			to = new FileOutputStream(toFile);
			byte[] buffer = new byte[4096];
			int bytesRead;

			while ((bytesRead = from.read(buffer)) != -1) {
				to.write(buffer, 0, bytesRead); // write
			}
		} finally {
			if (from != null) {
				try {
					from.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (to != null) {
				try {
					to.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static String convertStreamToString(InputStream is) {
		/*
		 * To convert the InputStream to String we use the BufferedReader.readLine()
		 * method. We iterate until the BufferedReader return null which means
		 * there's no more data to read. Each line will appended to a StringBuilder
		 * and returned as String.
		 */
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return sb.toString();
	}

    public static void writeFile(final URL fromURL, final File toFile)
            throws IOException {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new BufferedInputStream(fromURL.openStream());
            out = new BufferedOutputStream(new FileOutputStream(toFile));
            int len;
            byte[] buffer = new byte[4096];
            while ((len = in.read(buffer, 0, buffer.length)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
        } finally {
            in.close();
            out.close();
        }
    }

    public static void writeFile(final URL fromURL, final String toFile)
            throws IOException {
        writeFile(fromURL, new File(toFile));
    }

	class PatternFileParser extends FileParser<File>{
		private String pattern;
		public PatternFileParser(String pattern) {
			this.pattern = pattern;
		}

		@Override
		File parse(File file){
			if((pattern == null) || (pattern.equals("")) || (file.getName().matches(pattern))){
				return file;
			}
			return null;
		}
	}

	/**
	 * @return all the files from the directory 'directory'
	 */
	public static ArrayList<File> listFilesFromDirectory(File directory, boolean recursive) {
		return new ArrayList<File>(new FileUtil().filesFromDirectory(directory, recursive, ""));
	}

	/**
	 * @return all the files from the directory 'directory' which matches with 'pattern'
	 */
	public Collection<File> filesFromDirectory(File directory, boolean recursive, String pattern) {
		return listResourcesFromDirectory(directory, new PatternFileParser(pattern), recursive);
	}

	public static ArrayList<File> listFilesFromDirectory(File directory, boolean recursive, String pattern) {
		final Collection<File> collection = new FileUtil().filesFromDirectory(directory, recursive, pattern);
		return new ArrayList<File>(collection);
	}

	<T extends Object> Collection<T> listResourcesFromDirectory(File directory, FileParser<T> parser, boolean recursive) {
		Set<T> files = new HashSet<T>();
		if(directory != null){
			resourcesFromDirectory(directory, parser, recursive, files);
		}
		return files;
	}

	private <T extends Object> void resourcesFromDirectory(File directory, FileParser<T> parser, boolean recursive, Set<T> resources) {

		if(directory.exists()){

			if(directory.isFile()){
				T parsed = parser.parse(directory);
				if( parsed != null ){
					resources.add(parsed);
				}

			}else{
				final File[] subFiles = directory.listFiles();
				for (File file : subFiles) {
					if(!file.isFile()){
						if(recursive){
							resourcesFromDirectory(file, parser, recursive, resources);
						}
					} else{
						resourcesFromDirectory(file, parser, recursive, resources);
					}
				}
			}
		}
	}

	public File getSubFile(File file, String name) {
		for (File sub : file.listFiles()) {
			if(sub.getName().equals(name)){
				return sub;
			}
		}
		return null;
	}
}