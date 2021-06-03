
package org.futurepages.filters;

import org.futurepages.core.action.Action;
import org.futurepages.core.config.Params;
import org.futurepages.core.control.InvocationChain;
import org.futurepages.core.filter.Filter;
import org.futurepages.util.Is;

import javax.servlet.http.HttpServletRequest;

/**
 * Filtro responsável pela modificação do domínio da url para um domínio padronizado. Isto é útil para
 * SEO. Os indexadores de busca não penalizarão o sistema por conta de urls redundantes e ainda ajudará
 * na comunicação do sistema com outros através de callback urls padronizadas.
 *
 * @author leandro
 */
public class AutoRedirectDomainFilter implements Filter {

	private String mainDomain;

	public AutoRedirectDomainFilter(String baseURL) {
		this.mainDomain = baseURL;
		if(!Is.validURL("http://"+mainDomain)){
			throw new RuntimeException("Erro ao inicializar o filtro AutoRedirectDomainFilter. Especifique um domínio válido em app-params.xml[param=AUTO_REDIRECT_DOMAIN]");
		}
	}

	@Override
	public String filter(InvocationChain chain) throws Exception {
		HttpServletRequest req = chain.getAction().getRequest();
		String hostRequested = req.getHeader("Host");
		if(!hostRequested.equals(mainDomain)){
			chain.getAction().getOutput().setValue(Action.REDIR_URL, changeDomain(chain.getAction().getRequest()));
			return REDIR;
		}
		return chain.invoke();
	}

	private String changeDomain(HttpServletRequest request){
		StringBuilder newUrl = new StringBuilder();
		newUrl.append(Params.get("DEFAULT_SCHEME")).append("://").append(this.mainDomain);
		//nao descomentar, erroneamente estava encaminhando a porta para a nova url, acontece que a porta deve vim no parametro da url nova.
		//newUrl.append(request.getLocalPort()!=80 && request.getLocalPort()!=443 ? ":"+request.getLocalPort() : "" );
		if(!request.getRequestURI().equals("/"+Params.get("START_PAGE_NAME"))) {
			newUrl.append(request.getRequestURI());
		}else{
			newUrl.append("/");
		}
		if(request.getQueryString()!=null){
			newUrl.append("?").append(request.getQueryString());
		}
		return newUrl.toString();
	}

	@Override
	public void destroy() {	}

}