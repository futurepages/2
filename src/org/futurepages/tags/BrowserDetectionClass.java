package org.futurepages.tags;

import javax.servlet.jsp.JspException;
import org.futurepages.annotations.Tag;
import org.futurepages.annotations.TagAttribute;
import org.futurepages.core.tags.PrintTag;
import org.futurepages.core.tags.build.ContentTypeEnum;
import org.futurepages.util.BrowserDetection;

/**
 *
 * @author Thiago Rabelo
 *
 * TO-DO levar esta tag para o FUTUREPAGES2
 */
@Tag(bodyContent=ContentTypeEnum.EMPTY)
public class BrowserDetectionClass extends PrintTag {
	
	@TagAttribute(required = false)
	private Boolean browser;

	@TagAttribute(required = false)
	private Boolean engine;

	@TagAttribute(required = false)
	private Boolean mobile;

	public void setBrowser(Boolean browser) {
		this.browser = browser;
	}

	public void setEngine(Boolean engine) {
		this.engine = engine;
	}

	public void setMobile(Boolean mobile) {
		this.mobile = mobile;
	}

	@Override
	public String getStringToPrint() throws JspException {
		StringBuilder sb = new StringBuilder();
		BrowserDetection bd = BrowserDetection.getInstance(req);	
		sb.append("");
		
		if (browser == null && engine == null && mobile == null) {
			sb.append(bd.getEngine());
			
			return sb.toString();
		}
		
		if (browser != null && browser) {
			sb.append(bd.getBrowser());
		} else {
			browser = false;
		}
		
		if (engine != null && engine) {
			sb.append(browser ? " " : "").append(bd.getEngine());
		} else {
			engine = false;
		}
		
		if (mobile != null && mobile) {
			sb.append(browser || engine ? " " : "").append(bd.getMobile() ? "mobile" : "");
		}
		
		return sb.toString();
	}

}
