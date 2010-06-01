package org.futurepages.tags.core.conditional;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import org.futurepages.core.tags.ConditionalTag;
import org.futurepages.core.tags.build.ContentTypeEnum;

@org.futurepages.annotations.Tag(bodyContent = ContentTypeEnum.JSP)

public class Else extends IfTag {
	
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
			boolean result = checkParent(conditional);
			return result;
		} else {
			throw new JspException("Then not enclosed by a Conditinal tag!");
		}
	}

	private boolean checkParent(ConditionalTag conditional) {
		System.out.println("Else.checkParent()  "+this.getId());
		System.out.println("pai (	"+conditional.getClass().getSimpleName() + " id: "+conditional.getId() +"): "+conditional.isCondition());
		boolean result = !conditional.isCondition();
		System.out.println("retorno:  !pai: "+result);
		System.out.println("---------------");
		
		return result;
	}
}
