package org.futurepages.tags;


import javax.servlet.jsp.JspException;

import org.futurepages.core.path.Paths;
import org.futurepages.core.tags.PrintTag;
import org.futurepages.filters.ModuleIdFilter;


/**
 * @author Leandro
 */
public class IncludeModuleFunctions extends PrintTag{
   
	public String getStringToPrint() throws JspException {
		try{
			String moduleId =ModuleIdFilter.getModuleId(action);
			if(moduleId!=null){
				return "<script type=\"text/javascript\" src=\""+Paths.module(req, moduleId)+"/template/functions.js\"></script>";
			}
		}
		catch(Exception ex){
			return "";
		}
		return "";
    }
}