package org.futurepages.tags;

import javax.servlet.jsp.JspException;
import org.futurepages.core.tags.PrintTag;

public class Error extends PrintTag {

	public String getStringToPrint() throws JspException {
		return action.getError();
	}
}