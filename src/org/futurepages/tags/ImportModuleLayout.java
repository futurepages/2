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