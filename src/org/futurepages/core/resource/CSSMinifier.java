
package org.futurepages.core.resource;

import com.yahoo.platform.yui.compressor.CssCompressor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 *
 * @author leandro
 */
public class CSSMinifier {

	public void execute(List<File> jsFiles) {
		for (File f : jsFiles) {
			FileInputStream fis = null;
			InputStreamReader inReader = null;
			try {
				System.out.println("COMPACTANDO "+f.getAbsolutePath()+"...");
				fis = new FileInputStream(f);
				inReader = new InputStreamReader(fis);
				CssCompressor compressor = new CssCompressor(inReader);
				FileWriter fileWriter = new FileWriter(f);
				compressor.compress(fileWriter, Integer.MAX_VALUE);
				fileWriter.close();
				fis.close();
				inReader.close();
				System.out.println("COMPACTADO: "+f.getAbsolutePath()+"!");
			} catch (Exception ex) {
				System.out.println("ERROR AO COMPACTAR ARQUIVO CSS");
				ex.printStackTrace();
				try {
					fis.close();
					inReader.close();
				} catch (IOException ex2) {
					ex2.printStackTrace();
				}
			}
		}
	}
}
