package org.futurepages.tags.core.webcomponent;

import java.io.IOException;
import java.io.StringWriter;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.futurepages.annotations.Tag;
import org.futurepages.core.tags.build.ContentTypeEnum;

@Tag(bodyContent=ContentTypeEnum.SCRIPTLESS)
public final class WebComponentsContainer extends SimpleTagSupport {

	private boolean bodyEvaluated = false;

	public static final String WEB_COMPONENTS_CONTAINER_KEY = "webComponentsContainer";

	private Map<String, ImportComponentRes> components;

	private StringWriter out = new StringWriter();

	public static void importRes(HttpSession session, String key, String version, boolean noJS, boolean noCSS, boolean jsInHead) {
		ImportComponentRes icr = new ImportComponentRes();
		icr.setKey(key);
		icr.setVersion(version);
		icr.setJsInHead(jsInHead);
		WebComponentsContainer wcc = (WebComponentsContainer) session.getAttribute(WEB_COMPONENTS_CONTAINER_KEY);
		wcc.addComponent(key, icr);
	}

	public static void importRes(JspContext jspContext, String key, String version, boolean noJS, boolean noCSS, boolean jsInHead) {
		importRes(((PageContext) jspContext).getSession(), key, version, noJS, noCSS, jsInHead);
	}

	
	Map<String, ImportComponentRes> getComponents() {
		if (components == null) {
			components = new LinkedHashMap<String,ImportComponentRes>();
		}
		return components;
	}

	public boolean isBodyEvaluated() {
		return bodyEvaluated;
	}
	
	public void addComponent(String componentUniqueKey, ImportComponentRes component) {
		if(!getComponents().containsKey(componentUniqueKey)){
			getComponents().put(componentUniqueKey, component);
		}
	}
	
	@Override
	public void doTag() throws JspException, IOException {		
	    HttpSession session = ((PageContext) getJspContext()).getSession();
		HttpServletRequest req = (HttpServletRequest) ((PageContext) getJspContext()).getRequest();
		session.setAttribute(WEB_COMPONENTS_CONTAINER_KEY, this);
		StringWriter evalResult  = new StringWriter();
		StringBuffer headBuffer    = new StringBuffer();
		StringBuffer footerBuffer = new StringBuffer();

		getJspBody().invoke(evalResult);

		for (ImportComponentRes component : getComponents().values()){
			if(!component.isNoCSS()){
				component.appendCSSto(req,headBuffer);
			}
			if(!component.isNoJS()){
				if(component.isJsInHead()){
					component.appendJSto(req,headBuffer);
				}
				else{
					component.appendJSto(req,footerBuffer);
				}
			}
		}
		out.write(headBuffer.toString());
		out.write(evalResult.getBuffer().toString());
		out.write(footerBuffer.toString());
		getJspContext().getOut().print(out.getBuffer());
		bodyEvaluated = true;
		//Nao consegui eliminar na sessao o componentContainer, por algum motivo dá erro na tela de simulacao de publicidade
		//session.setAttribute(WEB_COMPONENTS_CONTAINER_KEY,null);
	}
}