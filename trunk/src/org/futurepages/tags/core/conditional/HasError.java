package org.futurepages.tags.core.conditional;

import javax.servlet.jsp.JspException;
import org.futurepages.core.tags.ConditionalTag;

/**
 * @author Sergio Oliveira
 */
public class HasError extends ConditionalTag {

	@Override
	public boolean testCondition() throws JspException {
		return (action!=null)? action.hasError() : false;
	}
}
