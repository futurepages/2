package org.futurepages.core.path;

import org.futurepages.core.config.Params;
import javax.servlet.http.HttpServletRequest;
import org.futurepages.util.StringUtils;

/**
 * Retorna os endere�os (URL) absolutos da aplica��o.
 * 
 * @author leandro
 */
public class Paths {
    
    /**
     * @param req Requisi��o
     * @param module id do m�dulo
     * @return a url completa do m�dulo
     */
    public static String module(HttpServletRequest req,String module) {
        return context(req)+"/"+Params.MODULES_PATH+"/"+((module!=null)?module:"");
    }

    public static String moduleAction(HttpServletRequest req, String moduleId) {
        if(Params.get("PRETTY_URL").equals("true")){
            return context(req)+"/"+moduleId;
        }
        return module(req,moduleId);
    }

    /**
     * @param req Requisi��o
     * @return a url completa da pasta de recursos da aplica��o
     */
    public static String resource(HttpServletRequest req) {
        return StringUtils.concat(context(req),"/",Params.get("RESOURCE_PATH"));
    }

	/**
     * @param req Requisi��o
     * @return a url completa da pasta de recursos da aplica��o
     */
    public static String resource(HttpServletRequest req, String module) {
        return StringUtils.concat(module(req,module),"/",Params.get("RESOURCE_PATH"));
    }

    /**
     * @param req Requisi��o
     * @return a url completa da pasta de temas da aplica��o
     */
    public static String theme(HttpServletRequest req){
        return template(req)+"/"+Params.get("THEMES_DIR_NAME")+"/"+Params.get("THEME");
    }

    /**
     * @param req Requisi��o
     * @return a url completa da pasta de arquivos do template da aplica��o
     */
    public static String template(HttpServletRequest req){
        return context(req)+"/"+Params.TEMPLATE_PATH;
    }

    /**
     *
     * @param req Requisi��o
     * @return o endere�o do contexto da aplica��o, exemplo: /application
     */
    public static String context(HttpServletRequest req){
        return req.getContextPath();
    }

    /**
     *
     * @param req Requisi��o
     * @return a url completa do host server da aplica��o
     */
    public static String host(HttpServletRequest req){
        return StringUtils.concat(req.getScheme(),"://",req.getServerName(),(req.getServerPort()!=80 && req.getServerPort()!= 443 ? ":"+req.getServerPort() : "" ));
    }

	public static String template(HttpServletRequest req, String module) {
		return (module==null? Paths.template(req) : Paths.module(req, module)+"/"+Params.TEMPLATE_PATH);
	}
}