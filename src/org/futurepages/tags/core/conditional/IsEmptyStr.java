package org.futurepages.tags.core.conditional;

import javax.servlet.jsp.JspException;

import org.futurepages.core.tags.ConditionalTag;
import org.futurepages.util.Is;

/**
 * @author Sergio Oliveira, Modified by Leandro
 */
public class IsEmptyStr extends ConditionalTag {
    
    private String test;
    
    public void setTest(String test) {
        this.test = test;
    }
    
    public boolean testCondition() throws JspException {
    	return (Is.empty(findValue(test)));
    }
}