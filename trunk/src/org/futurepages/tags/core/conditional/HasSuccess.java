
package org.futurepages.tags.core.conditional;

import javax.servlet.jsp.JspException;

import org.futurepages.core.tags.ConditionalTag;

public class HasSuccess extends ConditionalTag {

	public boolean testCondition() throws JspException {
		if(action!=null){
			return action.hasSuccess();
		}
		return false;
	}
}