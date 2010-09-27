package org.futurepages.tags;

import javax.servlet.jsp.JspException;

import org.futurepages.annotations.Tag;
import org.futurepages.annotations.TagAttribute;
import org.futurepages.annotations.TagAttributeOverride;
import org.futurepages.annotations.TagAttributeOverrideArray;
import org.futurepages.core.admin.DefaultUser;
import org.futurepages.core.tags.PrintTag;
import org.futurepages.core.tags.build.ContentTypeEnum;

@Tag(bodyContent = ContentTypeEnum.EMPTY)
@TagAttributeOverrideArray({@TagAttributeOverride(name = "max")})
public class Cookie extends PrintTag{

	@TagAttribute(required = true)
	private String key;

	@TagAttribute(required = false)
	private boolean user = false;

	@Override
	public String getStringToPrint() throws JspException {
		String cookieName = key;
		if(user){
			DefaultUser user = this.action.loggedUser();
			if(user != null){
				cookieName = key + "_"+ user.getLogin();
			}
		}
		Object result = this.action.getCookies().getAttribute(cookieName);
		if(result != null){
			return result.toString();
		}
		return "";
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setUser(boolean user) {
		this.user = user;
	}

}