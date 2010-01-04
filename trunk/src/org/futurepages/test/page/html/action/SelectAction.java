package org.futurepages.test.page.html.action;

import org.futurepages.test.page.FutureSelenium;
import org.futurepages.test.page.html.exception.WebElementNotFoundOnPageException;

public class SelectAction extends UIElement implements Action {

	/**
	 * 
	 * @param selenium
	 */
	public SelectAction(FutureSelenium selenium) {
		super(selenium);
	}

	/**
	 * @param selectName
	 *            element identifier
	 * @param args
	 *            : select arguments: args[0]: option label
	 */
	public void act(String selectName, Object... args) {
		if (args != null && args.length > 0) {
			String option = "";
			option = args[0].toString();
			if(!option.equals("")){
				String optionLocator = "label="+option;
				selenium.waitForElement(optionFindQuery(selectName, option));
				selenium.select(selectName, optionLocator);
			}
		}
	}

	private void waitForOption(String selectName,String label) {
		final String optionFindQuery = optionFindQuery(selectName, label);
		for (int second = 0;; second++) {
			if (second <= 5) {
				if (selenium.isElementPresent(optionFindQuery)){
					return;
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {}
			}else{
				throw new WebElementNotFoundOnPageException("'option' :"+label);
			}
		}
	}

	private String optionFindQuery(String selectName, String label) {
		return "//option[@value='"+label+"' or text()='"+label+"']";
	}

}
