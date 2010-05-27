package org.futurepages.tags.core.conditional;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import org.futurepages.core.tags.ConditionalTag;
import org.futurepages.core.tags.build.ContentTypeEnum;

@org.futurepages.annotations.Tag(bodyContent = ContentTypeEnum.JSP)
public class Then extends IfTag{

	@Override
	public boolean testCondition() throws JspException {
		System.out.println("Then.testCondition()");
		Tag parent = findAncestorWithClass(this, ConditionalTag.class);
		if (parent != null) {
			ConditionalTag conditional = (ConditionalTag) parent;
			if(Else.class.isAssignableFrom(conditional.getClass())){
				return !conditional.isCondition();
			}else{
				return conditional.isCondition();
			}
		} else {
			throw new JspException("Then not enclosed by a Conditinal tag!");
		}
	}
}