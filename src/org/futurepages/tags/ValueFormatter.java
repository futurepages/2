package org.futurepages.tags;

import javax.servlet.jsp.JspException;

import org.futurepages.core.tags.PrintTag;
import org.futurepages.core.formatter.Formatter;
import org.futurepages.core.formatter.FormatterManager;

/**
 * @author Leandro
 */
public class ValueFormatter extends PrintTag {

	private Object object;
	private String formatter;

	public String getStringToPrint() throws JspException {
		if (formatter != null) {

			Formatter f = FormatterManager.getFormatter(formatter);
			if (f == null) {
				throw new JspException("Cannot find formatter: " + formatter);
			}
			return f.format(object, action.getLocale());
		}
		else{
			return object.toString();
		}
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public void setFormatter(String formatter) {
		this.formatter = formatter;
	}
}
