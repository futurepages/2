package org.futurepages.tags.core.conditional.kase;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import org.futurepages.core.tags.ConditionalTag;

public class Then extends ConditionalTag{

	@Override
	public boolean testCondition() throws JspException {
		Tag parent = findAncestorWithClass(this, Case.class);
		if (parent != null) {
			Case kase = (Case) parent;
			return kase.isCondition();
		} else {
			throw new JspException("Then not enclosed by a Case tag!");
		}
	}

}
