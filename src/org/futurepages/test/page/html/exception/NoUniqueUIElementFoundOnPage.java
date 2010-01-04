package org.futurepages.test.page.html.exception;

public class NoUniqueUIElementFoundOnPage extends Exception {

	public NoUniqueUIElementFoundOnPage(String elementName) {
		super("Mais de um elemento com name='"+elementName+"' encontrados.");
	}
	
}
