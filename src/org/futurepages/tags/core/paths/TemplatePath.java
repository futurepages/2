package org.futurepages.tags.core.paths;

import javax.servlet.jsp.JspException;

import org.futurepages.core.path.Paths;
import org.futurepages.core.tags.PrintTag;

public class TemplatePath extends PrintTag{

	private String module;

	public void setModule(String module) {
		this.module = module;
	}
    
    public String getStringToPrint() throws JspException {
		return Paths.template(req,module);
    }
}