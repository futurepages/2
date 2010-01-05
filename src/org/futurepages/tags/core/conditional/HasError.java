package org.futurepages.tags.core.conditional;

import javax.servlet.jsp.JspException;
import org.futurepages.core.tags.ConditionalTag;

/**
 * Verifica se existe erro na tag.
 * 
 * @author Leandro Santana Pereira
 */
public class HasError extends ConditionalTag {

	@Override
	public boolean testCondition() throws JspException {
		return (action!=null)? action.hasError() : false;
	}
}