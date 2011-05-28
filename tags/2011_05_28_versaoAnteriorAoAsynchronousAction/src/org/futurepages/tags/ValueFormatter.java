package org.futurepages.tags;

import javax.servlet.jsp.JspException;

import org.futurepages.annotations.Tag;
import org.futurepages.annotations.TagAttribute;
import org.futurepages.core.tags.PrintTag;
import org.futurepages.core.tags.build.ContentTypeEnum;
import org.futurepages.core.formatter.Formatter;
import org.futurepages.core.formatter.FormatterManager;

/**
 * @author Leandro
 */
@Tag(bodyContent = ContentTypeEnum.EMPTY)
public class ValueFormatter extends PrintTag {

	@TagAttribute(required = true)
	private Object object;
	
	@TagAttribute
	private String formatter;

	@Override
	public String getStringToPrint() throws JspException {
		if (formatter != null) {

			Formatter f = FormatterManager.getFormatter(formatter);
			if (f == null) {
				throw new JspException("Cannot find formatter: " + formatter);
			}
			return f.format(object, action.getLocale());
		}
		else{
			return (object!=null)? object.toString() : "";
		}
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public void setFormatter(String formatter) {
		this.formatter = formatter;
	}
}