package org.futurepages.test.page.html.exception;

public class UIElementEnumUndentifiedException extends RuntimeException{

	public UIElementEnumUndentifiedException(String name) {
		super("Nenhum UIElement identificado com name: '"+name+"'.");
	}
}
