package org.futurepages.test.page.html.exception;

public class HTMLParserException extends RuntimeException{

	public HTMLParserException(String message, Throwable cause) {
		super(message, cause);
	}

	public HTMLParserException(Throwable cause) {
		super(cause);
	}

	public HTMLParserException(String cause) {
		super(cause);
	}
	
}
