package org.futurepages.test.page.html.exception;

public class WebElementNotFoundOnPageException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public WebElementNotFoundOnPageException(String elementName) {
		super("Nenhum elemento '"+elementName+"' encontrado.");
	}
	
}
