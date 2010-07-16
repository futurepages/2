package org.futurepages.tags;

import javax.servlet.jsp.JspException;

import org.futurepages.annotations.Tag;
import org.futurepages.core.tags.PrintTag;
import org.futurepages.core.tags.build.ContentTypeEnum;
import org.futurepages.util.HttpUtil;

/**
 * @author Sergio Oliveira
 */
@Tag(bodyContent = ContentTypeEnum.EMPTY)
public class NoCache extends PrintTag {
    
	@Override
	public String getStringToPrint() throws JspException {
		if(action.hasNoCache()){
			HttpUtil.disableCache(res);
			session.removeAttribute("noCache");
		}
        return null;
    }
}