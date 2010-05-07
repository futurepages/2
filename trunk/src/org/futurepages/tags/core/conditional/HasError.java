package org.futurepages.tags.core.conditional;

import javax.servlet.jsp.JspException;
import org.futurepages.core.action.Action;
import org.futurepages.core.tags.ConditionalTag;

/**
 * Verifica se existe erro na tag.
 * 
 * @author Leandro Santana Pereira
 */
public class HasError extends ConditionalTag {

	@Override
	public boolean testCondition() throws JspException {
		if(action!=null){
			return action.hasError();
		} else {
			return req.getParameter(Action.ERROR) != null;
		}
	}
}