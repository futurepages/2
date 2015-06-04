package org.futurepages.tags.core.webcomponent;

import org.futurepages.annotations.Tag;
import org.futurepages.core.tags.build.ContentTypeEnum;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.io.StringWriter;

@Tag(bodyContent = ContentTypeEnum.SCRIPTLESS)
public final class Footer extends SimpleTagSupport {


	private WebContainer myContainer;

	@Override
	public void doTag() throws JspException, IOException {
		getMyContainer();
		if (myContainer != null) {
				StringWriter evalResult = new StringWriter(); //escreve o body da tag
				if(getJspBody()!=null){
					getJspBody().invoke(evalResult);
					myContainer.addFooterContent(evalResult.getBuffer().toString());
				}
		}
	}

	private WebContainer getMyContainer() {
		if (myContainer == null) {
			myContainer = WebContainer.get();
		}
		return myContainer;
	}
}