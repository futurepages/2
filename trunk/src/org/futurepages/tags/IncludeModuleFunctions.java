package org.futurepages.tags;


import javax.servlet.jsp.JspException;

import org.futurepages.annotations.Tag;
import org.futurepages.core.path.Paths;
import org.futurepages.core.tags.PrintTag;
import org.futurepages.core.tags.build.ContentTypeEnum;
import org.futurepages.filters.ModuleIdFilter;

/**
 * @author Leandro
 */
@Tag(bodyContent = ContentTypeEnum.EMPTY)
public class IncludeModuleFunctions extends PrintTag{
   
	@Override
	public String getStringToPrint() throws JspException {
		try{
			StringBuffer sb = new StringBuffer();
			sb.append("<script type=\"text/javascript\" src=\""+Paths.module(req,"portal")+"/template/functions.js\"></script>");
			String moduleId =ModuleIdFilter.getModuleId(action);
			if(moduleId!=null && !moduleId.equals("portal")){
				sb.append("<script type=\"text/javascript\" src=\""+Paths.module(req, moduleId)+"/template/functions.js\"></script>");
			}
			return sb.toString();
		}
		catch(Exception ex){
			return "";
		}
    }
}