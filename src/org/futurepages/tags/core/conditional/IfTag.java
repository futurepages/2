package org.futurepages.tags.core.conditional;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import org.futurepages.core.tags.ConditionalTag;
import org.futurepages.core.tags.cerne.Context;
import org.futurepages.tags.Out;
import org.futurepages.util.Is;

/**
 * @author Sergio Oliveira, Modified by Leandro
 */
public class IfTag extends ConditionalTag {

	private String test = null;
	private String value = null;
	private String dynValue = null;

	public void setTest(String test) {
		this.test = test;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setDynValue(String dynValue) {
		this.dynValue = dynValue;
	}

	public boolean testCondition() throws JspException {
		if (Is.empty(test)) {
			return Boolean.parseBoolean(value);
		} else {
			if (dynValue != null && value != null) {

				throw new JspException("Invalid IfTag: cannot have value and dynValue at the same time!");
			}

			Tag parent = findAncestorWithClass(this, Context.class);
			Object obj = null;

			try {
				obj = Out.getValue(parent, test, pageContext, true);
				if (obj == null) {
					obj = action.getInput().getValue(test);
				}
			} catch (Exception ex) {
				//by Leandro
				if(action!=null){
					obj = action.getInput().getValue(test);
				}
			}
			Object dynObj = null;

			if (dynValue != null) {

				dynObj = Out.getValue(parent, dynValue, pageContext, true);
			}

			if (obj == null) {
				if (value != null && value.equals("null")) {
					return true;
				}

				if (dynValue != null && dynObj == null) {
					return true;
				}

				//throw new JspException("NullPointerException on IfTag: test expression " + test + " evaluated to null!");

				return false; // no need to throw nasty exception here...

			}

			if (obj instanceof Boolean && dynValue == null && value == null) {

				Boolean b = (Boolean) obj;

				return b.booleanValue();
			}

			if (dynValue != null) {

				if (dynObj == null) {
					return false;
				}

				return obj.equals(dynObj);

			} else {

				if (obj instanceof Boolean) {
					Boolean b = (Boolean) obj;
					if (value != null) {
						if (!value.equalsIgnoreCase("false") && !value.equalsIgnoreCase("true")) {
							throw new JspException("Invalid IfTag: value must be a boolean: " + test + " / " + value);
						}
						boolean flag = value.equalsIgnoreCase("true");
						return b.booleanValue() == flag;
					}
					return b.booleanValue();
				} else if (obj instanceof Integer) {
					Integer i = (Integer) obj;
					if (value == null) {
						throw new JspException("Invalid IfTag: value must be present for integer: " + test);
					}
					try {
						return i.intValue() == Integer.parseInt(value);
					} catch (NumberFormatException e) {
						throw new JspException("Invalid IfTag: value must be an integer: " + test + " / " + value);
					}
				} else if (obj instanceof Character) {

					Character c = (Character) obj;

					if (value == null) {
						throw new JspException("Invalid IfTag: value must be present for character: " + test);
					} else if (value.length() != 1) {
						throw new JspException("Invalid IfTag: value is not a char: " + value);
					}

					return c.charValue() == value.charAt(0);

				} else {
					if (value == null) {
						throw new JspException("Invalid IfTag: value must be present: " + test);
					}
					return value.equals(obj);
				}

			}
		}
	}
}