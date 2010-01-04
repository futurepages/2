package org.futurepages.tags;

import javax.servlet.jsp.JspException;

import org.futurepages.core.pagination.Pageable;
import org.futurepages.core.path.Paths;
import org.futurepages.core.config.Params;
import org.futurepages.core.tags.PrintTag;


public class Pagination extends PrintTag implements Pageable{
    
    private String url = null;
    private String params  = "";
    private int maxShowing  = 20;
    private boolean justTop = false;
    private String target  = "";
    private String cssClass  = "pagination";
	private String nextLabel      = "&raquo;";
	private String previousLabel  = "&laquo;";
	private boolean useImages     = false;

	private static final String NEXT_PAGE         = "nextpage";
	private static final String PREVIOUS_PAGE     = "previouspage";
	private static final String IMAGE_FORMAT	  = "gif";

    //Não é atributo da tag
    private String symbol = "?";
    private Boolean isPrettyUrl = new Boolean(Params.get("PRETTY_URL"));
    
    public String getStringToPrint() throws JspException {
        Integer totalPages = (Integer) action.getOutput().getValue(_TOTAL_PAGES);
        //Se possui mais de uma página.
        if(totalPages!=null){
			String pagesLinks = allPaginationLinks(totalPages);
			if(totalPages>1){
				if(justTop){
					return pagesLinks+getBodyContent().getString();
				}
				else{
					return pagesLinks+getBodyContent().getString()+pagesLinks;
				}
			}
		}
        //Se não possui mais de uma página.
        return getBodyContent().getString();
    }

	public void setNextLabel(String nextLabel) {
		this.nextLabel = nextLabel;
	}

	public void setPreviousLabel(String previousLabel) {
		this.previousLabel = previousLabel;
	}
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public void setParams(String params) {
        this.params = params;
    }    

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    public void setTarget(String target) {
        this.target = "target= \""+target+"\"";
    }

    public void setJustTop(boolean justTop) {
        this.justTop = justTop;
    }

	public void setMaxShowing(int maxShowing) {
		this.maxShowing = maxShowing;
	}

	public void setUseImages(boolean useImages) {
		this.useImages = useImages;
	}



	// MÉTODOS PRIVADOS //////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////


    private String pageNumLinks(int pageNum, int totalPages) {
        StringBuffer sb = new StringBuffer();

        //Resultado com até 20 páginas.
        if (totalPages <= maxShowing) {
            for (int i = 1; i <= totalPages; i++) {
                if (pageNum != i) {
                    sb.append(pageLink(i));
                } else {
                    sb.append("<span class=\"current\">" + pageNum + "</span>");
                }
            }
        } else {
            //Resultado com mais de 20 páginas.
            if (pageNum > 6) {
                for (int i = 1; i <= 3; i++) {
                    sb.append(pageLink(i));
                }
                sb.append(" ... ");
                for (int i = pageNum - 3; i <= pageNum - 1; i++) {
                    sb.append(pageLink(i));
                }
            } else {
                for (int i = 1; i < pageNum; i++) {
                    sb.append(pageLink(i));
                }
            }

            sb.append("<strong class=\"current\">" + pageNum + "</strong>");

            if (pageNum < totalPages - 6) {
                for (int i = pageNum + 1; i <= pageNum + 3; i++) {
                    sb.append(pageLink(i));
                }
                sb.append(" ... ");
                for (int i = totalPages - 2; i <= totalPages; i++) {
                    sb.append(pageLink(i));
                }
            } else {
                for (int i = pageNum + 1; i <= totalPages; i++) {
                    sb.append(pageLink(i));
                }
            }
        }
        return sb.toString();
    }

    /**
     * Verifica se a página é a primeira para habilitar/desabilitar o botão (<)
     */
    private String previousPageLink(int pageNum) {
		//enabled
        if (pageNum > 1) {
            if(isPrettyUrl){
                return "<a href=\"" + url + params + symbol + _PAGE_NUM + "=" + (pageNum - 1) +  "\" " + target + ">" + previousButton(true)  + "</a>";
            }
            else{
                return "<a href=\"" + url + symbol + _PAGE_NUM + "=" + (pageNum - 1) + params + "\" " + target + ">" + previousButton(true) + "</a>";
            }
        }
		//disabled
        else {
            return "<span class=\"disabled\">"+previousButton(false)+"</span>";
        }
    }

    /**
     * Verifica se é a última página para habilitar/desabilitar o botão (>)
     */
    private String nextPageLink(int pageNum, int totalPages) {
        //disabled
		if (pageNum == totalPages) {
            return "<span class=\"disabled\">"+nextButton(false)+"</span>";
        }
		//enabled
        else {
            if(isPrettyUrl){
                return "<a href=\"" + url + params + symbol + _PAGE_NUM + "=" + (pageNum + 1) + "\" " + target + ">" + nextButton(true) + "</a>";
            }
            else{
                return "<a href=\"" + url + symbol + _PAGE_NUM + "=" + (pageNum + 1) + params + "\" " + target + ">" + nextButton(true) + "</a>";
            }
        }
    }

    private String allPaginationLinks(Integer totalPages) {
        Integer pageNum = (Integer) action.getOutput().getValue(_PAGE_NUM);
        StringBuffer sb = new StringBuffer();
        if (!isPrettyUrl) {
            String completeURL = url + params;
            if(completeURL.contains("?")){
                symbol = "&";
            }
        }
        else {
          if(params.contains("?")){
            symbol = "&";
          }
        }

        sb.append(previousPageLink(pageNum));

        sb.append(pageNumLinks(pageNum,totalPages));

        sb.append(nextPageLink(pageNum,totalPages));

        return "<div class=\""+cssClass+"\">" + sb.toString() + "</div>";
    }

	private String pageLink(int pageNum) {
		if(isPrettyUrl){
			return "<a href=\"" + url + params + symbol + _PAGE_NUM + "=" + pageNum +"\" " + target + ">" + pageNum + "</a>";
		}
        else{
            return "<a href=\"" + url + symbol + _PAGE_NUM + "=" + pageNum + params + "\" " + target + ">" + pageNum + "</a>";
		}
	}

	private String previousButton(boolean enabled) {
		if(!useImages){
			return previousLabel;
		}
		else{
			return adjImgButton(PREVIOUS_PAGE, enabled);
		}
	}

	private String nextButton(boolean enabled) {
		if(!useImages){
			return nextLabel;
		}
		else{
			return adjImgButton(NEXT_PAGE, enabled);
		}
	}

	private String adjImgButton(String type, boolean enabled) {
		String enabledResult = enabled ? "" : "_disable";
		return "<img src=\"" + Paths.theme(req) + "/res/"+cssClass+"/"+type+enabledResult+"."+IMAGE_FORMAT+"\"/>";
	}
}