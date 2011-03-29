package org.futurepages.tags.core.template;

import javax.servlet.jsp.JspException;

import org.futurepages.annotations.Tag;
import org.futurepages.annotations.TagAttribute;
import org.futurepages.core.tags.PrintTag;
import org.futurepages.core.tags.build.ContentTypeEnum;
import org.futurepages.core.template.Page;
import org.futurepages.core.template.JspTemplateServlet;

/**
 * @author Leandro
 */
@Tag(bodyContent = ContentTypeEnum.EMPTY)
public class Block extends PrintTag {

	@TagAttribute(required = true, name="id")
	private String idX; //@TODO - Excluir ap�s contemplar heran�a de atributos
	
	@Override
	public String getStringToPrint() throws JspException {
		Page page = (Page) req.getAttribute(JspTemplateServlet.PAGE_ATTR);
		String view = null;
		if(page.isWithRule() && id.equals("body")) {
				view = ((String)req.getAttribute(JspTemplateServlet.CURRENT_PATH)) + ".jsp";
		} else {
			view = page.getBlock(id).getView();
		}

		try {
			pageContext.include("/" + view, false);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new JspException(ex);
		}
		return "";
	}
}