package org.futurepages.tags;


import javax.servlet.jsp.JspException;

import org.futurepages.annotations.Tag;
import org.futurepages.annotations.TagAttribute;
import org.futurepages.core.config.Params;
import org.futurepages.core.path.Paths;
import org.futurepages.core.tags.PrintTag;
import org.futurepages.core.tags.build.ContentTypeEnum;
import org.futurepages.filters.ModuleIdFilter;
import org.futurepages.util.Is;

/**
 * @author Leandro
 */
@Tag(bodyContent = ContentTypeEnum.EMPTY)
public class IncludeModuleFunctions extends PrintTag{

	@TagAttribute(required=false)
	private String autoImportModule = null;

	public void setAutoImportModule(String autoImportModule) {
		this.autoImportModule = autoImportModule;
	}

	@Override
	public String getStringToPrint() throws JspException {
		try{
			StringBuffer sb = new StringBuffer();
			boolean autoImporting = !Is.empty(autoImportModule);
			String moduleId =ModuleIdFilter.getModuleId(action);
			if(autoImporting){
				sb.append(scriptFunctionsTag(autoImportModule));
				if(!Is.empty(moduleId) && !moduleId.equals(autoImportModule)){
					sb.append(scriptFunctionsTag(moduleId));
				}
			} else if(!Is.empty(moduleId)){
					sb.append(scriptFunctionsTag(moduleId));
			}
			return sb.toString();
		}
		catch(Exception ex){
			return "";
		}
    }

	private String scriptFunctionsTag(String moduleId){
		return "<script type=\"text/javascript\" src=\""+Paths.module(req,moduleId)+"/template/functions.js"+Params.get("RELEASE_QUERY")+"\"></script>";
	}
}