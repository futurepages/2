package org.futurepages.core.template;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Interface que permite um maior controle sobre os pages. Cada page pode ter o
 * seu PageController, os quais tem acesso ao page, request, response e application. O
 * TemplateManager pode ser obtido tambem, bastando chamar o metodo estatico getTemplateManager
 * de TemplateServlet.
 * Em caso de regexp, o atributo path do page sera o valor da URL, e nao a expressao regular
 * na qual a URL se enquadrou.
 *
 * @author Davi Luan Carneiro
 */
public interface PageController {

	/**
	 * Realiza alguma tarefa ANTES da renderizacao do page
	 */
	public void processPage(Page page, HttpServletRequest request,
			HttpServletResponse response, ServletContext application);

}
