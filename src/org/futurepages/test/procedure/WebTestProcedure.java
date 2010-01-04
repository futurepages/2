package org.futurepages.test.procedure;

import java.util.Collection;

import org.futurepages.test.page.FutureSelenium;
import org.futurepages.test.page.FutureSeleniumFactory;
import org.futurepages.test.page.html.UIElementDiscoverer;
import org.futurepages.test.page.html.UIElementEnum;
import org.futurepages.test.page.html.action.Action;
import org.junit.Assert;

public class WebTestProcedure {
	
	protected static FutureSelenium selenium;

	public static void init() throws Exception{
		selenium = FutureSeleniumFactory.getFutureSeleniumInstance();
	}

	protected static void input(String name, Object... args) {
		selenium.waitForElement(name);
		UIElementEnum elementEnum;
		//try {
			elementEnum = UIElementDiscoverer.getUIElement(name, selenium);
//		} catch (NoUniqueUIElementFoundOnPage e) {
//			System.out.println(e.getMessage());
//			elementEnum = UIElementDiscoverer.getFirstFoundUIElement(name,selenium);
//		}
		Action action = selenium.getAction(elementEnum);
		System.out.println("ACTION: "+action.getClass().getName());
		System.out.println("");
		action.act(name, args);
	}

	protected static void  input(String name, Action action, Object... args) {
		action.act(name, args);
	}
	
	@Deprecated
	protected static void output(Collection<String> texts) {
		output(texts.toArray(new String[texts.size()]));
	}
	
	protected static void checkElements(String errorMessage, String... elementsLocators) {
		for (String elementLocator : elementsLocators) {
			String msg = errorMessage+"; O Elemento '"+elementLocator+"' não foi encontrado.";
			Assert.assertTrue(msg, selenium.isElementPresent(elementLocator));
		}
	}
	
	protected static void checkNotElements(String errorMessage, String... elementsLocators) {
		for (String elementLocator : elementsLocators) {
			String msg = errorMessage+"; O Elemento '"+elementLocator+"' não deveria estar presente na página.";
			Assert.assertFalse(msg, selenium.isElementPresent(elementLocator));
		}
	}
	
	protected static void checkOutput(String errorMessage, String... texts) {
		for (String text : texts) {
			Assert.assertTrue(errorMessage+"; O Texto '"+text+"' não foi encontrado.", selenium.isTextPresent(text));
		}
	}
	
	protected static void checkNotOutput(String errorMessage, String... texts) {
		for (String text : texts) {
			Assert.assertFalse(errorMessage+"; O Texto '"+text+"' foi encontrado.", selenium.isTextPresent(text));
		}
	}

	@Deprecated
	protected static void output(String... text) {
		for (String string : text) {
			Assert.assertTrue("o Texto '"+string+"' não foi encontrado.", selenium.isTextPresent(string));
		}
	}
	
	public static void finalyze(){
		selenium.stop();
	}
}