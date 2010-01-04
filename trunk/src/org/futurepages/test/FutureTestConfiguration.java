package org.futurepages.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.futurepages.core.config.Params;
import org.futurepages.exceptions.BadFormedConfigFileException;
import org.futurepages.test.page.FutureSeleniumFactory;
import org.futurepages.util.EncodingUtil;
import org.jdom.JDOMException;

public class FutureTestConfiguration {
	
	private final Map<String, String> properties;
	
	private static FutureTestConfiguration instance;

	private FutureTestConfiguration() {
		properties = new HashMap<String, String>();
	}

	public static FutureTestConfiguration getInstance() {
		if (instance == null) {
			instance = new FutureTestConfiguration();
			instance.configure();
		}
		return instance;
	}
	
	public Map<String, String> getProperties() {
		return properties;
	}
 
	private void configure() {
		String file = "";
		String testFile = "futuretest.properties";
		try {
			String classPath = FutureSeleniumFactory.class.getResource("/").getPath();
			File rootDir = new File(URLDecoder.decode(classPath,EncodingUtil.getSystemEncoding()));
			file = rootDir.getAbsolutePath()+"/"+Params.CONFIGURATION_DIR_NAME+"/futuretest.properties";
			readConfigFile(file);
		} catch (IOException e) {
			System.out.println("Arquivo de configuração de teste não encontrado: "+testFile);
		} catch (JDOMException e) {
			throw new BadFormedConfigFileException("Arquivo de configuração de teste mal formado: "+testFile);
		}
	}
	
	private void readConfigFile(String file) throws JDOMException, IOException {
		Properties properties = new Properties();
		InputStream inputStream = new FileInputStream(file);
		properties.load(inputStream);
		registerProperties(properties);
	}
	
	private void registerProperties(Properties properties) {
		insertProperty(properties, "url");
		insertProperty(properties, "browser");
		insertProperty(properties, "repositorio");
	}

	private void insertProperty(Properties properties, String propertyName) {
		String property = properties.getProperty(propertyName);
		if(property!= null && property.length()>0){
			this.properties.put(propertyName, property);
		}
	}
}
