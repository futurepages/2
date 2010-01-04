package org.futurepages.test.page.html.action;

import org.futurepages.test.page.FutureSelenium;

public class ButtonAction extends UIElement implements Action{

	public ButtonAction(FutureSelenium selenium) {
		super(selenium);
	}

	public void act(String name, Object... args) {
		Boolean ajax = false;
		if( args != null && args.length >0){
			Object arg0 = args[0];
			ajax = Boolean.parseBoolean(arg0.toString());
		}
		super.clicar(name, ajax);
	}

	@Override
	public void clicar(String name) {
		selenium.click("//input[@value='"+name+"']");
		selenium.waitForPageToLoad("30000l");
	}

	@Override
	public boolean verificarPresenca(String name) {
		return selenium.isElementPresent("//input[@value='"+name+"']");
	}
}