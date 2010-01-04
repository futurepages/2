package org.futurepages.tags;

import javax.servlet.jsp.JspException;
import org.futurepages.core.tags.PrintTag;
import org.futurepages.util.HttpUtil;

/**
 * @author Sergio Oliveira
 */
public class NoCache extends PrintTag {
    
	public String getStringToPrint() throws JspException {
		
		HttpUtil.disableCache(res);
		
        return null;
    }
}