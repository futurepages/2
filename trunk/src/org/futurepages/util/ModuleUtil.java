package org.futurepages.util;

import org.futurepages.core.config.Params;

public class ModuleUtil {

	public static String moduleId(Class klass){
		String className = klass.getName();
		if(!className.startsWith(Params.MODULES_PACK)){
			return null;
		}
		return The.firstTokenAfter(className, Params.MODULES_PACK , ".");
	}

//	/**
//	 * Varredura dos diret�rios "modules/ ** / identifica��o dos diret�rios 'tags'"
//	 * @return Map<String,List<File>> com os diret�rios modules / ** /tags
//	 */
//	public static Map<String, List<File>> getDirectoriesFromModules(File[] modules , String dirName) {
//		Map<String, List<File>> tagFilesFound = new HashMap<String, List<File>>();
//		if (modules != null) {
//			for (File module : modules) {
//				for (File moduleElement : module.listFiles()) {
//					if (moduleElement.getName().equals(dirName)) {
//						tagFilesFound.put(module.getName(), Arrays.asList(moduleElement.listFiles()));
//					}
//				}
//			}
//		}
//		return tagFilesFound;
//	}

}