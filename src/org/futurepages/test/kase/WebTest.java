package org.futurepages.test.kase;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import org.futurepages.test.procedure.WebTestProcedure;

public abstract class WebTest<P extends WebTestProcedure> {
	
	@BeforeClass
	public static void beginBrowser() throws Exception {
		WebTestProcedure.init();
	}	
	
	@AfterClass
	public static void stopBrowser() {
//		WebTestProcedure.finalyze();
	}
}