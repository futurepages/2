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
   
	@Override
	public String getStringToPrint() throws JspException {
		try{
			StringBuffer sb = new StringBuffer();
			sb.append("<link rel=\"stylesheet\" type=\"text/css\" href=\""+Paths.module(req,"portal")+"/template/layout.css\" media=\"all\"/>");
			String moduleId = ModuleIdFilter.getModuleId(action);
			if(moduleId!=null && !moduleId.equals("portal")){
				sb.append("<link rel=\"stylesheet\" type=\"text/css\" href=\""+Paths.module(req, moduleId)+"/template/layout.css\" media=\"all\"/>");
			}
			return sb.toString();
		}
		catch(Exception ex){
			return "";
		}
    }
}