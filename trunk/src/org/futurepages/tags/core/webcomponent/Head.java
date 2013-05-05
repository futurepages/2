package org.futurepages.tags.core.webcomponent;

import java.io.IOException;
import java.io.StringWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import org.futurepages.annotations.Tag;
import org.futurepages.core.tags.build.ContentTypeEnum;

@Tag(bodyContent = ContentTypeEnum.SCRIPTLESS)
public final class Head extends SimpleTagSupport {


	private WebContainer myContainer;

	@Override
	public void doTag() throws JspException, IOException {
		getMyContainer();
		if (myContainer != null) {
				StringWriter evalResult = new StringWriter(); //escreve o body da tag
				if(getJspBody()!=null){
					getJspBody().invoke(evalResult);
					myContainer.addHeadContent(evalResult.getBuffer().toString());
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