package org.futurepages.test.page;

import java.util.HashMap;
import java.util.Map;

import org.futurepages.test.FutureTestConfiguration;
import org.openqa.selenium.server.SeleniumServer;

public class FutureSeleniumFactory {

	private static FutureSelenium selenium;
	private static Map<String, String> properties = new HashMap<String, String>();

	public static FutureSelenium getFutureSeleniumInstance() throws Exception {

		if (selenium == null) {
			properties = new HashMap<String, String>();
			startSeleniumServer();

			defaultConfig();
			properties = FutureTestConfiguration.getInstance().getProperties();
			selenium = FutureSelenium.init("localhost",  5561, properties.get("browser"),
					properties.get("url"));
			selenium.start();
			return selenium; 
		} else {
			return selenium;
		}
	} 


	public static void startSeleniumServer() throws Exception{
		try {
			SeleniumServer.main(new String[]{"-port" ,"5561"});
			System.out.println("Selenium Server started....");
		} catch (org.mortbay.util.MultiException e1) {
			System.out.println("ERRO....");
			e1.printStackTrace();
		}
	}

	private static void defaultConfig() {
		properties.put("url","http://localhost:8080");
		properties.put("browser","*iexplore");
	}
}
