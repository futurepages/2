package org.futurepages.test.page.html.action;

import org.futurepages.test.page.FutureSelenium;

import com.thoughtworks.selenium.SeleniumException;

public class LinkAction extends UIElement implements Action{

	public LinkAction(FutureSelenium selenium) {
		super(selenium);
	}

	public void clicar(String nomeLink){
		try {
			super.clicar(nomeLink);
		} catch (SeleniumException e) {
			super.clicar("link="+nomeLink);
		}
	}

	public void act(String name, Object... args) {
		clicar(name);

	}
}
