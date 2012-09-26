package org.futurepages.tags.core.webcomponent;

import java.io.IOException;
import java.io.StringWriter;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.futurepages.annotations.Tag;
import org.futurepages.annotations.TagAttribute;
import org.futurepages.core.tags.build.ContentTypeEnum;
import org.futurepages.util.StringUtils;

@Tag(bodyContent = ContentTypeEnum.SCRIPTLESS)
public final class WebContainer extends SimpleTagSupport {

	static ThreadLocal<WebContainer> threadLocal = new ThreadLocal();
	
	@TagAttribute
	private String xmlns;

	@TagAttribute
	private String lang;

	@TagAttribute
	private String dir = "";

	@TagAttribute
	private String id = "";

	@TagAttribute
	private String headFile = null;

	private boolean bodyEvaluated = false;

	private Map<String, ImportComponentRes> components;


	public WebContainer() {
		super();
		this.setXmlns("http://www.w3.org/1999/xhtml");
		this.setLang("pt-br");
	}

	public static void importRes(JspContext page, String key, String version, boolean noJS, boolean noCSS, boolean jsInHead) throws JspException, IOException {
		importRes(page, key, version, noJS, noCSS, jsInHead, null, false);
	}

	public static void importRes(JspContext page, String key, String version, boolean noJS, boolean noCSS, boolean jsInHead, String moduleId, boolean pseudo) throws JspException, IOException {
		ImportComponentRes icr = new ImportComponentRes();
		icr.setKey(key);
		icr.setVersion(version);
		icr.setJsInHead(jsInHead);
		icr.setModuleId(moduleId);
		icr.setPseudo(pseudo);
		icr.setJspContext(page);
		icr.doTag();
	}

	Map<String, ImportComponentRes> getComponents() {
		if (components == null) {
			components = new LinkedHashMap<String, ImportComponentRes>();
		}
		return components;
	}

	public boolean isBodyEvaluated() {
		return bodyEvaluated;
	}

	public void addComponent(String componentUniqueKey, ImportComponentRes component) {
		if (!getComponents().containsKey(componentUniqueKey)) {
			getComponents().put(componentUniqueKey, component);
		}
	}

	@Override
	public void doTag() throws JspException, IOException {
		HttpServletRequest req = (HttpServletRequest) ((PageContext) getJspContext()).getRequest();

		threadLocal.set(this);

		StringWriter evalResult = new StringWriter();
		StringBuffer headBufferBegin = new StringBuffer();
		StringBuffer headBufferEnd = new StringBuffer();
		StringBuffer footerBuffer = new StringBuffer();

		headBufferBegin.append(StringUtils.concat("<html",id,xmlns,lang,dir,"><head>"));
		
		getJspBody().invoke(evalResult); //invoca o conteúdo dentro do container

		for (ImportComponentRes component : getComponents().values()) {
			if (!component.isNoCSS() && !component.isPseudo()) {
				component.appendCSSto(req, headBufferEnd);
			}
			if (!component.isNoJS() && !component.isPseudo()) {
				if (component.isJsInHead()) {
					component.appendJSto(req, headBufferEnd);
				} else {
					component.appendJSto(req, footerBuffer);
				}
			}
		}
		headBufferEnd.append("</head><body>");
		footerBuffer.append("</body></html>");

		getJspContext().getOut().print(headBufferBegin);
		if (headFile != null) {
			try {
				((PageContext) getJspContext()).include(headFile, false);
			} catch (ServletException ex) {
				throw new JspException("Impossível incluir arquivo HEAD '" + headFile + "'. Motivo: " + ex.getMessage());
			}
		}
		getJspContext().getOut().print(headBufferEnd);
		getJspContext().getOut().print(evalResult.getBuffer());
		getJspContext().getOut().print(footerBuffer);

		bodyEvaluated = true;
		threadLocal.remove(); // quase equivalente a threadLocal.set(null);
	}

	public void setHeadFile(String headFile) {
		this.headFile = headFile;
	}

	public void setDir(String dir) {
		this.dir = " dir=\""+dir+"\"";
	}

	public void setId(String id) {
		this.id = " id=\""+id+"\"";
	}

	public void setLang(String lang) {
		this.lang = StringUtils.concat(" xml:lang=\"",lang,"\" lang=\"",lang+"\"");
	}

	public void setXmlns(String xmlns) {
		this.xmlns = " xmlns=\""+ xmlns+"\"";
	}

	static WebContainer get() {
		return threadLocal.get();
	}
}