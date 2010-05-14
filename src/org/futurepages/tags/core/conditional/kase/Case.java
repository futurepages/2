package org.futurepages.tags.core.conditional.kase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.futurepages.core.action.Action;
import org.futurepages.core.consequence.Forward;
import org.futurepages.core.i18n.LocaleManager;
import org.futurepages.tags.core.conditional.IfTag;

public class Case extends IfTag{

	private static final long serialVersionUID = 1L;
	private boolean condition;
	
	@Override
	public int doStartTag() throws JspException {
		this.application = pageContext.getServletContext();
		this.session = pageContext.getSession();
		this.req = (HttpServletRequest) pageContext.getRequest();
		this.res = (HttpServletResponse) pageContext.getResponse();
		this.action = (Action) req.getAttribute(Forward.ACTION_REQUEST);
		this.loc = LocaleManager.getLocale(req);
		this.condition = super.testCondition();
		return EVAL_BODY_BUFFERED;
	}

	protected boolean isCondition() {
		return condition;
	}
	
}
