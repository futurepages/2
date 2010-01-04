package org.futurepages.tags.core.paths;

import javax.servlet.jsp.JspException;

import org.futurepages.core.path.Paths;
import org.futurepages.core.tags.PrintTag;

public class ResourcePath extends PrintTag{
    
    public String getStringToPrint() throws JspException {
        return Paths.resource(req);
    }
}
