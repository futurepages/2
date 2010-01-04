package org.futurepages.core.template;

import org.futurepages.exceptions.TemplateException;
import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.futurepages.core.control.AbstractApplicationManager;

/**
 * Controller responsavel pelo suporte a convencoes. Trabalha com valores
 * defaults, porem pode ser extendido e personalizado.
 * Ele funciona assim: suponha que haja uma requisicao <b>modules/news/list.page</b>.
 * O easytemplates vai pegar o template base, vai setar os valores para os
 * blocos comuns, como "topo", "menu", "rodape", etc. e setara no bloco de nome
 * "body" (veja o metodo getPrincipalBlock) o valor <b>modules/news/list.jsp</b>
 *
 * @author Davi Luan Carneiro
 */
public class ConventionController implements PageController {

	public void processPage(Page page, HttpServletRequest request,
			HttpServletResponse response, ServletContext application) {
		
		
		String view = getRoot() + page.getPath() + getExtension();
		
		String viewDir = AbstractApplicationManager.getViewDir();
		
		if (viewDir != null && viewDir.length() > 0 && !view.startsWith(viewDir)) {
			
			if (viewDir.endsWith("/") && view.startsWith("/")) {
				
				viewDir = viewDir.substring(0, viewDir.length() - 1);
				
			} else if (!viewDir.endsWith("/") && !view.startsWith("/")) {
				
				viewDir = viewDir + "/";
			}
			
			view = viewDir + view;
		} 
		
		File file = new File(application.getRealPath("/") + view);
		if (!file.exists()) {
			try {
				request.getRequestDispatcher(view).forward(request, response);
				return;
			} catch (Exception e) {
				throw new TemplateException(e);
			}
		}
		page.setBlock(getPrincipalBlock(), new Page(view));
	}

	/**
	 * Pode ser estendido e redefinido. Por exemplo, se voce quiser que todas as
	 * paginas fiquem dentro de WEB-INF/views, eh so redefinir este metodo.
	 *
	 * @return Diretorio raiz das paginas
	 */
	public String getRoot() {
		return "";
	}

	/**
	 * Por default, vale "body". Ou seja, ele vai considerar que este eh o bloco
	 * principal, o corpo, o conteudo da pagina. Pode ser redefinido.
	 *
	 * @return Nome do bloco que contera o corpo da pagina
	 */
	public String getPrincipalBlock() {
		return "body";
	}

	/**
	 * Por default retorna .jsp. Este metodo existe para o caso de ser criado suporte
	 * a Freemarker ou Velocity. Assim, a extensao deveria ser diferente.
	 *
	 * @return Extensao da pagina
	 */
	public String getExtension() {
		return ".jsp";
	}
}