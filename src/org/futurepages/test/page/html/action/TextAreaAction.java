package org.futurepages.test.page.html.action;

import org.futurepages.test.page.FutureSelenium;

public class TextAreaAction extends UIElement  implements Action {

	public TextAreaAction() {
		super(FutureSelenium.getInstance());
	}

	public TextAreaAction(FutureSelenium futureSelenium) {
		super(futureSelenium);
	}

	/**
	 * Digita um texto num textArea comum
	 */
	public void act(String name, Object... args) {
		if(args != null && args.length >0){
			String valor = args[0].toString();
			selenium.type(name, valor);
		}
	}

}
