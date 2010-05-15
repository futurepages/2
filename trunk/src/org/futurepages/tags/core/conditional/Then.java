package org.futurepages.tags.core.conditional;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import org.futurepages.core.tags.ConditionalTag;
import org.futurepages.core.tags.build.ContentTypeEnum;

@org.futurepages.annotations.Tag(bodyContent = ContentTypeEnum.JSP)
public class Then extends IfTag{

	@Override
	public boolean testCondition() throws JspException {
		Tag parent = findAncestorWithClass(this, ConditionalTag.class);
		if (parent != null) {
			ConditionalTag conditional = (ConditionalTag) parent;
			return conditional.isCondition();
		} else {
			throw new JspException("Then not enclosed by a Conditinal tag!");
		}
	}

}
