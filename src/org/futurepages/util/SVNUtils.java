package org.futurepages.util;

import java.io.File;
import java.io.IOException;

public class SVNUtils {

	public static void cleanCopy(String source, String target) throws IOException {
		File origem = new File(source);
		cleanCopy("",  target, origem.listFiles());
		removeDeleted(source, target);
	}

	private static void cleanCopy(String deep, String target, File... contents) throws IOException {

		for (File origem : contents) {
			if (origem.isFile()) {
				copy(origem, target + "\\" + deep);
			} else {
				String nomeFile = origem.getName();
				if (!nomeFile.equals(".svn") ) {
					cleanCopy(deep + "\\" + nomeFile, target, origem.listFiles());
				}
			}
		}
	}

	private static void copy(File origem, String dest) throws IOException {
		File file = new File(dest);
		if (!file.exists()) {
			file.mkdirs();
		}
		FileUtil.copy(origem.getAbsolutePath(), dest);
	}

	private static void removeDeleted(String origem, String target) throws IOException {
		File destino = new File(target);
		removeDeleted("", origem, destino);
	}

	private static void removeDeleted(String sufixo, String source, File destino) throws IOException {
		File origem = new File(source+"\\"+sufixo);
		removeDeleted(sufixo, origem, destino);
	}

	private static void removeDeleted(String sufixo, File origem, File destino) throws IOException {
		if(!destino.getName().equals(".svn")){
			boolean temDestino = destino.exists();
			boolean naoTemOrigem = !origem.exists();
			if(temDestino && naoTemOrigem ){
				destino.delete();
			}else{
				if(destino.isDirectory() ){
					for (File filho : destino.listFiles()) {
						String novoSuf = filho.getName();
						removeDeleted(novoSuf, origem.getAbsolutePath()  ,filho);
					}
				}
			}
		}
	}

}
