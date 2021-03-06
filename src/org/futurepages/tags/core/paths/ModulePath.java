package org.futurepages.tags.core.paths;

import javax.servlet.jsp.JspException;

import org.futurepages.annotations.Tag;
import org.futurepages.annotations.TagAttribute;
import org.futurepages.core.path.Paths;
import org.futurepages.core.tags.PrintTag;
import org.futurepages.core.tags.build.ContentTypeEnum;

@Tag(bodyContent = ContentTypeEnum.EMPTY)
public class ModulePath extends PrintTag{
    
	@TagAttribute
    private String module = "";
    
    public String getStringToPrint() throws JspException {
        return Paths.module(req,this.module);
    }    

    public void setModule(String module) {
        this.module = module;
    }
}