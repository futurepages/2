package org.futurepages.tags;

import org.apache.taglibs.standard.tag.common.core.SetSupport;
import org.futurepages.annotations.Tag;
import org.futurepages.annotations.TagAttribute;
import org.futurepages.core.tags.build.ContentTypeEnum;

/**
 * @author Leandro
 * // pageContext.setAttribute(var, value);
 */
@Tag(bodyContent = ContentTypeEnum.JSP)
public class Set extends SetSupport {

	@TagAttribute(rtexprvalue =false)
	private String var;
	
	@TagAttribute(name  = "value")
	private String valueF;

	@TagAttribute(name  = "target")
	private String targetF;

	@TagAttribute(name  = "property")
	private String propertyF;

	@TagAttribute(rtexprvalue = false)
	private String scope;

	public void setValue(Object v) {
		this.value = v;
		this.valueSpecified = true;
	}

	public void setTarget(Object target) {
		this.target = target;
	}

	public void setProperty(String property) {
		this.property = property;
	}
}