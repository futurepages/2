package org.futurepages.tags;

import java.util.Date;

import javax.servlet.jsp.JspException;

import org.futurepages.annotations.Tag;
import org.futurepages.annotations.TagAttribute;
import org.futurepages.core.tags.PrintTag;
import org.futurepages.core.tags.build.ContentTypeEnum;
import org.futurepages.util.DateUtil;

/**
 * @author Leandro
 */
@Tag(bodyContent = ContentTypeEnum.EMPTY)
public class DateTime extends PrintTag{
   
	@TagAttribute(rtexprvalue = false)
	private String mask;
	
	@TagAttribute
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
