package org.futurepages.tags;

import javax.servlet.jsp.JspException;

import org.futurepages.core.tags.PrintTag;
import org.futurepages.core.action.Action;

public class Success extends PrintTag{
    
    public String getStringToPrint() throws JspException {
        if (action == null){
                return req.getParameter(Action.SUCCESS);
        }

        String success = (String) action.getOutput().getValue(Action.SUCCESS);

        if(success==null){
            success = (String) action.getInput().getValue(Action.SUCCESS);
        }
        if(success==null){
            success = (String) pageContext.findAttribute(Action.SUCCESS);
        }

        return (success!=null)? success : "";
    }



}
