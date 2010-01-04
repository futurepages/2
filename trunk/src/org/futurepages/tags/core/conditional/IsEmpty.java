package org.futurepages.tags.core.conditional;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import org.futurepages.core.tags.ConditionalTag;
import org.futurepages.core.tags.cerne.ListContext;
import org.futurepages.tags.Out;

/**
 * @author Sergio Oliveira
 */
public class IsEmpty extends ConditionalTag {
	
	private String test = null;
	
	public void setTest(String test) {
		this.test = test;
	}
    
    public boolean testCondition() throws JspException {
    	
		Tag parent = findAncestorWithClass(this, ListContext.class);
		
        if (parent != null && test == null) {
        	
			ListContext tag = (ListContext) parent;
            List<Object> list = tag.getList();
			return list == null || list.size() == 0;
			
		} else {
			
			if (test != null) {
				
				Object value = Out.getValue(test, pageContext, false);
				
				if (value == null) return true; // should do this ???
				
				if (value instanceof Collection) {
					
					Collection col = (Collection) value;
					
					return col.isEmpty();
					
				} else if (value instanceof Map) {
					
					Map map = (Map) value;
					
					return map.isEmpty();
					
				} else if (value instanceof Object[]) {
					
					Object[] array = (Object[]) value;
					
					return array.length == 0;
					
				}
				
				throw new JspException("IsEmpty: Value " + test + " is not a Collection, Array or a Map!");
				
			}
		}

		throw new JspException("IsEmpty: Could not find list context !!!");
	}
}