package org.futurepages.tags.core.webcomponent;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashSet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import org.futurepages.annotations.Tag;
import org.futurepages.annotations.TagAttribute;
import org.futurepages.core.config.Params;
import org.futurepages.core.path.Paths;
import org.futurepages.core.tags.build.ContentTypeEnum;
import static org.futurepages.util.StringUtils.concat;

@Tag(bodyContent = ContentTypeEnum.SCRIPTLESS)
public final class ImportComponentRes extends SimpleTagSupport {

	public static final ThreadLocal<HashSet> asyncResources = new ThreadLocal();

	@TagAttribute(required = true)
	private String key;

	@TagAttribute(required = true)
	private String version;

	@TagAttribute
	private boolean noCSS = false;

	@TagAttribute
	private boolean noJS = false;

	@TagAttribute
	private boolean jsInHead = false;

	@TagAttribute
	private String moduleId = null;

	@TagAttribute
	private boolean pseudo = false;

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
		} else { //if primeira vez dentro da ajax request - container = null (ajax request)
			 if(asyncResources.get()==null){
				asyncResources.set(new HashSet());
			 }
			 if(!asyncResources.get().contains(this.uniqueKey())){ //primeira vez do componente dentro do contexto request
				HttpServletRequest req = (HttpServletRequest) ((PageContext) getJspContext()).getRequest();
				if(!noJS && !pseudo){
					getJspContext().getOut().print(importJS(req));
				}
				if(!noCSS && !pseudo){
					getJspContext().getOut().print(importCSS(req));
				}
				StringWriter evalResult = new StringWriter(); //escreve o body da tag
				if(getJspBody()!=null){
					getJspBody().invoke(evalResult);
					getJspContext().getOut().print(evalResult.getBuffer());
				}
				asyncResources.get().add(this.uniqueKey()); //adiciona para não executar mais para este componente
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

	public boolean isPseudo() {
		return this.pseudo;
	}

	public void setPseudo(boolean pseudo) {
		this.pseudo = pseudo;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	private String uniqueKey() {
		return this.key + ">" + this.version;
	}

	public void appendJSto(HttpServletRequest req, StringBuffer buffer) {
		buffer.append(importJS(req));
	}

	private String importJS(HttpServletRequest req){
		return  concat("<script src=\"" , resPath(req) , "/" , key , "/" , version , "/" , key , ".js"+Params.get("RELEASE_QUERY")+"\" type=\"text/javascript\"></script>");
	}

	public void appendCSSto(HttpServletRequest req, StringBuffer buffer) {
		buffer.append(importCSS(req));
	}

	private String importCSS(HttpServletRequest req){
		return  concat("<link rel=\"stylesheet\" type=\"text/css\" href=\"" , resPath(req) , "/" , key , "/" , version , "/" , key , ".css"+Params.get("RELEASE_QUERY")+"\" media=\"all\"/>");
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