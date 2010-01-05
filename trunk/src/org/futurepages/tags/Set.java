package org.futurepages.tags;

import org.apache.taglibs.standard.tag.common.core.SetSupport;

/**
 * @author Leandro
 * // pageContext.setAttribute(var, value);
 */
public class Set extends SetSupport {

	public void setValue(Object value) {
		this.value = value;
		this.valueSpecified = true;
	}

	public void setTarget(Object target) {
		this.target = target;
	}

	public void setProperty(String property) {
		this.property = property;
	}
}