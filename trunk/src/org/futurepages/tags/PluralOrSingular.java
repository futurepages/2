package org.futurepages.tags;


import javax.servlet.jsp.JspException;

import org.futurepages.core.tags.PrintTag;


/**
 * @author Leandro
 */
public class PluralOrSingular extends PrintTag{
   
	private long value;

    private boolean valueBefore;
    private String singular;
    private String plural;
    private String zero;
	    
	public String getStringToPrint() throws JspException {
		String before = valueBefore ? value+" " : "" ;
        if(value==1){
            return before+singular;
        }
        if((value == 0) && (zero!=null)){
            return zero;
        }
        return before+plural;
    }

	public void setValue(long value) {
		this.value = value;
	}

    public void setSingular(String singular) {
        this.singular = singular;
    }

	public void setValueBefore(boolean valueBefore) {
		this.valueBefore = valueBefore;
	}

	 public void setPlural(String plural) {
        this.plural = plural;
    }

    public void setZero(String zero) {
        this.zero = zero;
    }
}
