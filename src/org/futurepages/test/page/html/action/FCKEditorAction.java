package org.futurepages.test.page.html.action;

import org.futurepages.test.page.FutureSelenium;

public class FCKEditorAction extends UIElement implements Action {

	public FCKEditorAction() {
		super(FutureSelenium.getInstance());
	}

	@Override
	public void act(String name, Object... args) {
		String script = "FCKeditorAPI.GetInstance(\'"+name+"\').SetHTML(\'"+args[0]+"\')";
		selenium.runScript(script);
	}
}

