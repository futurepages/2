package org.futurepages.tags;

import java.util.Date;

import javax.servlet.jsp.JspException;

import org.futurepages.core.tags.PrintTag;
import org.futurepages.util.DateUtil;


/**
 * @author Leandro
 */
public class DateTime extends PrintTag{
   
	private String mask;
	private Date date;
	
	public DateTime(){
		mask = "dd/MM/yyyy";
	}

	public void setMask(String mask) {
		this.mask = mask;
	}
	
    public void setDate(Date date) {
		this.date = date;
	}

	public String getStringToPrint() throws JspException {
		if(date!=null){
    		return DateUtil.format(date,mask);
    	}
    	return DateUtil.viewToday(mask);
    }

}
