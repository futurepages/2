package org.futurepages.tags.core;

import javax.servlet.jsp.JspException;

import org.futurepages.core.config.Params;
import org.futurepages.core.tags.PrintTag;

public class App extends PrintTag{

    private String attribute;
    private String param;
    
    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public void setParam(String param) {
        this.param = param;
    }
    
    public String getStringToPrint() throws JspException {
        if(param!=null){
            try {
               return Params.get(param);
            } catch (Exception ex) {
                System.out.println("erro: "+ex);
            }
        }
        else if(attribute!=null){
           return (String) application.getAttribute(attribute);
        }
        return "";
    }
}