package org.futurepages.core.template;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Heranca de TemplateServlet para trabalhar com JSP.
 * 
 * @author Davi Luan Carneiro
 */
public class JspTemplateServlet extends TemplateServlet {
	
	/**
	 * Seta o page na request. Faz isso atraves do metodo request.setAttribute
	 */
	protected void putPageInResponse(Page page, HttpServletRequest request, HttpServletResponse response, ServletContext application) throws Exception {
		request.setAttribute(TemplateServlet.PAGE_ATTR, page);		
	}

	/**
	 * Exibe a pagina no brownser do usuario, usando um dispatcher
	 */
	protected void showPage(Page page, HttpServletRequest request, HttpServletResponse response, ServletContext application) throws Exception {
		application.getRequestDispatcher("/" + page.getView()).forward(request, response);
	}
}
