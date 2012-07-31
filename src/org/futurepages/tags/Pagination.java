package org.futurepages.tags;

import javax.servlet.jsp.JspException;

import org.futurepages.annotations.Tag;
import org.futurepages.annotations.TagAttribute;
import org.futurepages.core.pagination.Pageable;
import org.futurepages.core.path.Paths;
import org.futurepages.core.config.Params;
import org.futurepages.core.tags.PrintTag;
import org.futurepages.core.tags.build.ContentTypeEnum;
import static org.futurepages.util.StringUtils.*;

@Tag(bodyContent = ContentTypeEnum.JSP)
public class Pagination extends PrintTag implements Pageable {

	@TagAttribute(required = true)
	private String url = null;

	private String cachedUrl;

	@TagAttribute
	private String params = "";

	@TagAttribute
	private int maxShowing = 20;

	@TagAttribute
	private String descriptor = "";

	@TagAttribute
	private boolean withoutNumbers = false; //withNumbers is default

	@TagAttribute
	private boolean justTop = false;

	@TagAttribute
	private String target = "";

	@TagAttribute
	private String cssClass = "pagination";

	@TagAttribute
	private String nextLabel = "&raquo;";

	@TagAttribute
	private String previousLabel = "&laquo;";

	@TagAttribute
	private boolean useImages = false;

	private static final String NEXT_PAGE = "nextpage";
	private static final String PREVIOUS_PAGE = "previouspage";
	private static final String IMAGE_FORMAT = "gif";
	//N�o � atributo da tag
	private String symbol = "?";
	private Boolean isPrettyUrl = new Boolean(Params.get("PRETTY_URL"));

	@Override
	public String getStringToPrint() throws JspException {
		Integer totalPages = (Integer) action.getOutput().getValue(_TOTAL_PAGES);
		//Se possui mais de uma p�gina.
		if (totalPages != null) {
			String pagesLinks = allPaginationLinks(totalPages);
			if (totalPages > 1) {
				if (justTop) {
					return concat(descriptor, pagesLinks, getBodyContent().getString());
				} else {
					return concat(descriptor, pagesLinks, getBodyContent().getString(), descriptor, pagesLinks);
				}
			}
			//Se n�o possui mais de uma p�gina.
		} else if (justTop) {
			return descriptor + getBodyContent().getString();
		}
		return concat(descriptor, getBodyContent().getString(), descriptor);
	}

	public void setNextLabel(String nextLabel) {
		this.nextLabel = nextLabel;
	}

	public void setPreviousLabel(String previousLabel) {
		this.previousLabel = previousLabel;
	}

	public void setWithoutNumbers(boolean withoutNumbers) {
		this.withoutNumbers = withoutNumbers;
	}

	public String getUrl() {
		if (cachedUrl == null) {
			if (url.startsWith("/")) {
				this.cachedUrl = Paths.context(req) + url;
			} else {
				this.cachedUrl = this.url;
			}
		}
		return this.cachedUrl;
	}

	public void setUrl(String url) {
		this.cachedUrl = null;
		this.url = url;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	public void setTarget(String target) {
		this.target = "target= \"" + target + "\"";
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

	public void setDescriptor(String descriptor) {
		this.descriptor = descriptor;
	}

	// M�TODOS PRIVADOS //////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////
	private String pageNumLinks(int pageNum, int totalPages) {
		StringBuffer sb = new StringBuffer();

		if (!withoutNumbers) {
			//Resultado com at� 20 p�ginas.
			if (totalPages <= maxShowing) {
				for (int i = 1; i <= totalPages; i++) {
					if (pageNum != i) {
						sb.append(pageLink(i));
					} else {
						sb.append("<span class=\"current\">" + pageNum + "</span>");
					}
				}
			} else {
				//Resultado com mais de 20 p�ginas.
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
		} else {
			boolean prevOK = false;
			boolean nextOK = false;
			for (int i = 1; i <= totalPages; i++) {
				if (pageNum < i && !prevOK) {
					sb.append("<span class=\"disabled\"> ... </span>");
					prevOK = true;
				} else if(pageNum > i && !nextOK){
					sb.append("<span class=\"disabled\"> ... </span>");
					nextOK = true;
				} else if(pageNum == i){
					sb.append("<span class=\"current\">" + pageNum + "</span>");
				}
			}
		}
		return sb.toString();
	}

	/**
	 * Verifica se a p�gina � a primeira para habilitar/desabilitar o bot�o (<)
	 */
	private String previousPageLink(int pageNum) {
		//enabled
		if (pageNum > 1) {
			if (isPrettyUrl) {
				return concat("<a href=\"", getUrl(), params, symbol, _PAGE_NUM, "=", (pageNum - 1), "\" ", target, ">", previousButton(true), "</a>");
			} else {
				return concat("<a href=\"", getUrl(), symbol, _PAGE_NUM, "=", (pageNum - 1), params, "\" ", target, ">", previousButton(true), "</a>");
			}
		} //disabled
		else {
			return "<span class=\"disabled\">" + previousButton(false) + "</span>";
		}
	}

	/**
	 * Verifica se � a �ltima p�gina para habilitar/desabilitar o bot�o (>)
	 */
	private String nextPageLink(int pageNum, int totalPages) {
		//disabled
		if (pageNum == totalPages) {
			return "<span class=\"disabled\">" + nextButton(false) + "</span>";
		} //enabled
		else {
			if (isPrettyUrl) {
				return concat("<a href=\"", getUrl(), params, symbol, _PAGE_NUM, "=", (pageNum + 1), "\" ", target, ">", nextButton(true), "</a>");
			} else {
				return concat("<a href=\"", getUrl(), symbol, _PAGE_NUM, "=", (pageNum + 1), params, "\" ", target, ">", nextButton(true), "</a>");
			}
		}
	}

	private String allPaginationLinks(Integer totalPages) {
		Integer pageNum = (Integer) action.getOutput().getValue(_PAGE_NUM);
		StringBuffer sb = new StringBuffer();
		if (!isPrettyUrl) {
			String completeURL = getUrl() + params;
			if (completeURL.contains("?")) {
				symbol = "&";
			}
		} else {
			if (params.contains("?")) {
				symbol = "&";
			}
		}

		sb.append(previousPageLink(pageNum));

		sb.append(pageNumLinks(pageNum, totalPages));

		sb.append(nextPageLink(pageNum, totalPages));

		return "<div class=\"" + cssClass + "\">" + sb.toString() + "</div>";
	}

	private String pageLink(int pageNum) {
		if (isPrettyUrl) {
			return concat("<a href=\"", getUrl(), params, symbol, _PAGE_NUM, "=", pageNum, "\" ", target, ">", pageNum, "</a>");
		} else {
			return concat("<a href=\"", getUrl(), symbol, _PAGE_NUM, "=", pageNum, params, "\" ", target, ">", pageNum, "</a>");
		}
	}

	private String previousButton(boolean enabled) {
		if (!useImages) {
			return previousLabel;
		} else {
			return adjImgButton(PREVIOUS_PAGE, enabled);
		}
	}

	private String nextButton(boolean enabled) {
		if (!useImages) {
			return nextLabel;
		} else {
			return adjImgButton(NEXT_PAGE, enabled);
		}
	}

	private String adjImgButton(String type, boolean enabled) {
		String enabledResult = enabled ? "" : "_disable";
		return concat("<img src=\"", Paths.theme(action.getRequest()), "/res/", cssClass, "/", type + enabledResult, ".", IMAGE_FORMAT, "\"/>");
	}
}
