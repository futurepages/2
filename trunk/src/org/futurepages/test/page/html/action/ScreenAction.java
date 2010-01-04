package org.futurepages.test.page.html.action;

import org.futurepages.test.page.FutureSelenium;


public class ScreenAction extends UIElement implements Action{

	public ScreenAction(FutureSelenium selenium) {
		super(selenium);
	}

	/**
	 * @param url: url da página a ser acessada
	 * @param args[0]: tempo limite de espera.
	 */
	public void act(String url, Object... args) {
		if(args != null && args.length >0){
			open(url, args[0].toString());
		}else{
			open(url);
		}
	}

	public void open(String url) {
		open(url, "6000");
	}
	
	public void open(String url, String timeOut) {
		selenium.open(url);
		selenium.waitForPageToLoad(timeOut);
	}
}
