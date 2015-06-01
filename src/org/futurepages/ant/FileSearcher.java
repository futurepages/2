package org.futurepages.ant;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;

/**
 *
 * @author Mohammad Faisal
 */
public class FileSearcher {

	Collection<File> files = null;

	public void init(String pathName){
		File root = new File(pathName);
		FilenameFilter filter = getFilter();
		files = listFileTree(root, filter);
	}

	private FilenameFilter getFilter() {
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				File file = new File(dir, name);
				if(file.isDirectory()){
					if(name.endsWith("svn")){
						return false;
					}
					return true;
				}
				if(name.endsWith("ModuleManger.java")){
					return false;
				}

				boolean isCode = name.endsWith("jsp") || name.endsWith("java") || name.endsWith("js") || name.endsWith("tag") ;
				return isCode;
			}
		};
		return filter;
	}

	public Collection<File> listFileTree(File dir, FilenameFilter filter) {
		Set<File> fileTree = new HashSet<File>();
		for (File entry : dir.listFiles(filter)) {
			if (entry.isFile()) {
				fileTree.add(entry);
			} else {
				fileTree.addAll(listFileTree(entry, filter));
			}
		}
		return fileTree;
	}

	class Result implements Comparable<Result>{
		String pivo;
		List<String> paths = new LinkedList<String>();
		int qtdFound(){
			return paths.size();
		}

		boolean exists(){
			return qtdFound()>0;
		}


		@Override
		public int compareTo(Result o) {

			int thisFound = this.paths.size();
			int otherFound = o.paths.size();

			if (thisFound == otherFound)
				return this.pivo.compareTo(o.pivo);
			else if (thisFound > otherFound)
				return 1;
			else
				return -1;
		}
	}

	public Map<String, Result> search(Collection<String> textToMatch) throws IOException {
		String[] array = textToMatch.toArray(new String[textToMatch.size()]);
		return search(array);
	}

	public Map<String, Result> notFound(Collection<String> textToMatch) throws IOException {
		String[] array = textToMatch.toArray(new String[textToMatch.size()]);
		return notFound(array);
	}

	public Map<String, Result> notFound(String... textToMatch) throws IOException {
		String content;

		Map<String, Result> results = new HashMap<String, Result>();

		for (String pivo : textToMatch) {
			Result result = new Result();
			result.pivo = pivo;
			results.put(pivo, result);
			result.paths.add("some file");
		}
		List<String> restantes = Arrays.asList(textToMatch);

		Collection<File> listOfFiles = this.files;
		for (File file : listOfFiles) {
			content = FileUtils.readFileToString(file);
			for (Iterator<String> it = restantes.iterator(); it.hasNext();) {
				if (content.contains(it.next())) {
					it.remove();
					continue;
				}
			}
		}

		for (String restante : restantes) {
			results.get(restante).paths.clear();
		}

		return results;

	}

	public Map<String, Result> search(String... textToMatch) throws IOException {
		String content;

		Map<String, Result> results = new HashMap<String, Result>();

		for (String pivo : textToMatch) {
			Result result = new Result();
			result.pivo = pivo;
			results.put(pivo, result);
		}
		Collection<File> listOfFiles = this.files;
		for (File file : listOfFiles) {
			content = FileUtils.readFileToString(file);
			for (String pivo : textToMatch) {
				if (content.contains(pivo)) {
					Result r = results.get(pivo);
					r.paths.add(file.getAbsolutePath());
				}
			}
		}

		return results;
	}
}