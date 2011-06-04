package org.futurepages.core.resource;

import com.yahoo.platform.yui.compressor.JavaScriptCompressor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import org.futurepages.core.exception.DefaultExceptionLogger;

/**
 *
 * @author leandro
 */
public class JavaScriptMinifier {

	public void execute(List<File> jsFiles) {
		for (File f : jsFiles) {
			FileInputStream fis = null;
			InputStreamReader inReader = null;
			try {
				System.out.println("COMPACTANDO "+f.getAbsolutePath()+"...");
				fis = new FileInputStream(f);
				inReader = new InputStreamReader(fis);
				JavaScriptCompressor compressor = new JavaScriptCompressor(inReader, new FpgErrorReporter());
				FileWriter fileWriter = new FileWriter(f);
				compressor.compress(fileWriter, Integer.MAX_VALUE, true, true, true, false);
				fileWriter.close();
				fis.close();
				inReader.close();
				System.out.println("COMPACTADO: "+f.getAbsolutePath()+"!");
			} catch (Exception ex) {
				System.out.println("ERROR AO COMPACTAR ARQUIVO JS");
				DefaultExceptionLogger.getInstance().execute(ex);
				try {
					fis.close();
					inReader.close();
				} catch (IOException ex2) {
					DefaultExceptionLogger.getInstance().execute(ex2);
				}
			}
		}
	}
}
