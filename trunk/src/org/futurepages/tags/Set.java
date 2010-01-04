package org.futurepages.tags;


import javax.servlet.jsp.JspException;

import org.futurepages.core.tags.PrintTag;


/**
 * @author Leandro
 */
public class Set extends PrintTag{
   
	private String var;
	private Object value;

	public void setVar(String var) {
		this.var = var;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getStringToPrint() throws JspException {
		pageContext.setAttribute(var, value);
		return "";
	}

}