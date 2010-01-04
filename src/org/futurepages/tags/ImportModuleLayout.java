package org.futurepages.tags;

import javax.servlet.jsp.JspException;
import org.futurepages.core.path.Paths;
import org.futurepages.core.tags.PrintTag;
import org.futurepages.filters.ModuleIdFilter;

/**
 * @author Leandro
 */
public class ImportModuleLayout extends PrintTag{
   
	public String getStringToPrint() throws JspException {
		try{
			String moduleId = ModuleIdFilter.getModuleId(action);
			if(moduleId!=null){
				return "<link rel=\"stylesheet\" type=\"text/css\" href=\""+Paths.module(req, moduleId)+"/template/layout.css\" media=\"all\"/>";
			}
		}
		catch(Exception ex){
			return "";
		}
		return "";
    }
}