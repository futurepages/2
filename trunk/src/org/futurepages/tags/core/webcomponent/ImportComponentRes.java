package org.futurepages.tags.core.webcomponent;

import java.io.IOException;
import java.io.StringWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import org.futurepages.annotations.Tag;
import org.futurepages.annotations.TagAttribute;
import org.futurepages.core.path.Paths;
import org.futurepages.core.tags.build.ContentTypeEnum;
import static org.futurepages.util.StringUtils.concat;

@Tag(bodyContent = ContentTypeEnum.SCRIPTLESS)
public final class ImportComponentRes extends SimpleTagSupport {

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

	@TagAttribute
	private String moduleId = null;

	private WebContainer myContainer;

	@Override
	public void doTag() throws JspException, IOException {
		getMyContainer();
		if (myContainer != null) {
			StringBuffer buffer = new StringBuffer();
			if (getJspBody() != null) {
				if (!myContainer.getComponents().containsKey(uniqueKey())) {
					StringWriter out = new StringWriter();
					getJspBody().invoke(out);
					buffer.append(out.getBuffer());
				}
			}
			if (myContainer.isBodyEvaluated()) {
				throw new JspException("Componente "+uniqueKey()+" em container já avaliado e não nulo");
			}
			getJspContext().getOut().print(buffer);
			addToContainer();
		} else { //container = null
			if(!noJS){
				getJspContext().getOut().print( concat("<script type=\"text/javascript\">needResourceJS('",this.getKey(),"','"+this.getVersion(),"');</script>"));
			}
			if(!noCSS){
				getJspContext().getOut().print( concat("<script type=\"text/javascript\">needResourceCSS('"+this.getKey(),"','",this.getVersion(),"');</script>"));
			}
		}
	}

	private WebContainer getMyContainer() {
		if (myContainer == null) {
			myContainer = WebContainer.get();
		}
		return myContainer;
	}

	private void addToContainer() {
		getMyContainer().addComponent(uniqueKey(), this);
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

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	private String uniqueKey() {
		return this.key + ">" + this.version;
	}

	public void appendJSto(HttpServletRequest req, StringBuffer buffer) {
		buffer.append( concat("<script src=\"" , resPath(req) , "/" , key , "/" , version , "/" , key , ".js\" type=\"text/javascript\"></script>"));
	}

	public void appendCSSto(HttpServletRequest req, StringBuffer buffer) {
		buffer.append( concat("<link rel=\"stylesheet\" type=\"text/css\" href=\"" , resPath(req) , "/" , key , "/" , version , "/" , key , ".css\" media=\"screen\"/>"));
	}

	private String resPath(HttpServletRequest req){
		return (moduleId==null)?Paths.resource(req) : Paths.resource(req,moduleId);
	}

	@Override
	public String toString() {
		getMyContainer();
		return uniqueKey()+(myContainer!=null?" in "+this.getMyContainer():" without container");
	}
}