package org.futurepages.tags.core.webcomponent;

import java.io.IOException;
import java.io.StringWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import org.futurepages.annotations.Tag;
import org.futurepages.annotations.TagAttribute;
import org.futurepages.core.path.Paths;
import org.futurepages.core.tags.build.ContentTypeEnum;

@Tag(bodyContent = ContentTypeEnum.SCRIPTLESS)
public final class ImportComponentRes extends SimpleTagSupport {

	public static final String WEB_COMPONENTS_CONTAINER_KEY = WebComponentsContainer.WEB_COMPONENTS_CONTAINER_KEY;

	@TagAttribute(required = true)
	private String key;

	@TagAttribute(required = false)
	private String version;

	@TagAttribute
	private boolean noCSS = false;

	@TagAttribute
	private boolean noJS = false;

	@TagAttribute
	private boolean jsInHead = false;

	private WebComponentsContainer webComponentsContainer;

	@Override
	public void doTag() throws JspException, IOException {
		WebComponentsContainer container = getWebComponentsContainer();
		if (container != null) {
			HttpServletRequest req = (HttpServletRequest) ((PageContext) getJspContext()).getRequest();
			StringBuffer buffer = new StringBuffer();
			if (getJspBody() != null) {
				if (!container.getComponents().containsKey(uniqueKey())) {
					StringWriter out = new StringWriter();
					getJspBody().invoke(out);
					buffer.append(out.getBuffer());
				}
			}
			if (container.isBodyEvaluated()) {
				if (!noJS || !noCSS) {
					if (!container.getComponents().containsKey(uniqueKey())) {
						if (!noJS) {
							appendJSto(req, buffer);
						}
						if (!noCSS) {
							appendCSSto(req, buffer);
						}
					}
				}
			}
			getJspContext().getOut().print(buffer);
			addToContainer();
		} else {
			throw new JspException("WebComponentsContainer não encontrado. Utilize a tag 'webComponentsContainer'");
		}
	}

	public WebComponentsContainer getWebComponentsContainer() {
		if (webComponentsContainer == null) {
			HttpSession session = ((PageContext) getJspContext()).getSession();
			webComponentsContainer = (WebComponentsContainer) session.getAttribute(WEB_COMPONENTS_CONTAINER_KEY);
		}
		return webComponentsContainer;
	}

	private void addToContainer() {
		getWebComponentsContainer().addComponent(uniqueKey(), this);
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public boolean isNoCSS() {
		return noCSS;
	}

	public void setNoCSS(boolean noCSS) {
		this.noCSS = noCSS;
	}

	public boolean isNoJS() {
		return noJS;
	}

	public void setNoJS(boolean noJS) {
		this.noJS = noJS;
	}

	public boolean isJsInHead() {
		return this.jsInHead;
	}

	public void setJsInHead(boolean jsInHead) {
		this.jsInHead = jsInHead;
	}

	private String uniqueKey() {
		return this.key + ">" + this.version;
	}

	public void appendJSto(HttpServletRequest req, StringBuffer buffer) {
		buffer.append("<script src=\"" + Paths.resource(req) + "/" + key + "/" + version + "/" + key + ".js\" type=\"text/javascript\"></script>");
	}

	public void appendCSSto(HttpServletRequest req, StringBuffer buffer) {
		buffer.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"" + Paths.resource(req) + "/" + key + "/" + version + "/" + key + ".css\" media=\"all\"/>");
	}
}
