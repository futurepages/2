package org.futurepages.tags.core.conditional;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import org.futurepages.core.tags.ConditionalTag;

public abstract class ConditionalContextTag  extends IfTag{
	
	public int doStartTag() throws JspException {
		init();
		eval();
		if(this.testCondition() && (this.context || !print)){
			return EVAL_BODY_BUFFERED;
		}
		return SKIP_BODY;
	}
	
	@Override
	public boolean testCondition() throws JspException {
		Tag parent = findAncestorWithClass(this, ConditionalTag.class);
		if (parent != null) {
			ConditionalTag conditional = (ConditionalTag) parent;
			boolean result = contextualCondition(conditional);
			return result;
		} else {
			throw new JspException(this.getClass().getSimpleName() +" not enclosed by a Conditinal tag!");
		}
	}

	protected abstract boolean contextualCondition(ConditionalTag conditional) ;
}
