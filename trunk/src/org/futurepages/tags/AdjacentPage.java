package org.futurepages.tags;

import javax.servlet.jsp.JspException;

import org.futurepages.annotations.Tag;
import org.futurepages.annotations.TagAttribute;
import org.futurepages.core.pagination.Pageable;
import org.futurepages.core.tags.PrintTag;
import org.futurepages.core.tags.build.ContentTypeEnum;
import org.futurepages.util.Is;
import org.futurepages.util.StringUtils;

/**
 * Tag para exibici��o de links tipo 
 * Anteriores | Pr�ximos
 *
 * Faz se necess�rio o valor {@link Pageable}_HAS_PREVIOUS_PAGE e {@link Pageable}_HAS_NEXT_PAGE; 
 * 	Quando nulos ou n�o informados, os respectivos links n�o s�o constru�dos
 * 
 * @author Danilo
 */
@Tag(bodyContent = ContentTypeEnum.JSP)
public class AdjacentPage extends PrintTag implements Pageable {

	@TagAttribute(required = true)
    private String type;
    
    @TagAttribute(required = true)
	private String href;//N�o pode ser vazio!
	
    @TagAttribute(rtexprvalue = false)
    private String title;
	
	@TagAttribute(required = false)
    private String clss;
	
	@Override
	public String getStringToPrint() throws JspException {
		StringBuilder html = new StringBuilder();
		if(hasAdjacentUrl()){
			Boolean hasNextPage = (Boolean) action.getOutput().getValue(inputOf(type));
			if(hasNextPage!=null && hasNextPage){
				html.append("<a href=\"").append(href).append("\" ").append(title);

				if (!Is.empty(clss)) {
					html.append(" class=\"").append(clss).append("\"");
				}

				html.append(">").append(getBodyContent().getString()).append("</a> ");
			}else{
				html.append("<strong>"+getBodyContent().getString()+" </strong>");
			}
		}
		return html.toString();
	}

	private boolean hasAdjacentUrl() {
		return StringUtils.isNotEmpty(href);
	}

    private String inputOf(String type) {
        if(type.equalsIgnoreCase("next")) return _HAS_NEXT_PAGE;
        if(type.equalsIgnoreCase("previous")) return _HAS_PREVIOUS_PAGE;
        return null;
    }

    public void setTitle(String title) {
        this.title = "title=\""+title+"\"";
    }

    public void setHref(String href) {
        this.href = href;
    }

    public void setType(String type) {
        this.type = type;
    }
	
	public void setClss(String clss) {
		this.clss = clss;
	}
}