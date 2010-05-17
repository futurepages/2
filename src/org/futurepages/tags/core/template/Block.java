package org.futurepages.tags.core.template;

import javax.servlet.jsp.JspException;

import org.futurepages.annotations.Tag;
import org.futurepages.annotations.TagAttribute;
import org.futurepages.core.tags.PrintTag;
import org.futurepages.core.tags.build.ContentTypeEnum;
import org.futurepages.core.template.Page;
import org.futurepages.core.template.TemplateServlet;

/**
 * @author Leandro
 */
@Tag(bodyContent = ContentTypeEnum.EMPTY)
public class Block extends PrintTag {

	@TagAttribute(required = true, name="id")
	private String idX; //@TODO - Excluir após contemplar herança de atributos
	
	@Override
	public String getStringToPrint() throws JspException {
		Page page = (Page) req.getAttribute(TemplateServlet.PAGE_ATTR);
		Page block = page.getBlock(id);
		String view = "";
		if (block == null) {
			throw new JspException("Block " + id + " doesn't exists");
		}
		req.setAttribute(TemplateServlet.PAGE_ATTR, block);
		view = block.getView();
		try {
			TemplateServlet.executeListener(block, req, res, application);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new JspException(ex);
		}

		String oldView = (String) req.getAttribute("_view");

		String _view = "/" + view;
		req.setAttribute("_view", _view);
		try {
			pageContext.include(_view, false); //o mesmo que <jsp:include page="${_view}" flush="false"/>
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new JspException(ex);
		}
		req.setAttribute("_view", oldView);
		req.setAttribute(TemplateServlet.PAGE_ATTR, page);
		return "";
	}
}