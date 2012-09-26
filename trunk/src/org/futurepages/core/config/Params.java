package org.futurepages.core.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Element;
import org.jdom.JDOMException;

import org.futurepages.exceptions.BadFormedConfigFileException;
import org.futurepages.exceptions.ConfigFileNotFoundException;
import org.futurepages.util.FileUtil;
import org.futurepages.util.XmlUtil;

/**
 * Parâmetros da aplicação Futurepages
 * 
 * @author leandro
 */
public class Params {

	private static HashMap<String, String> paramsMap = new HashMap<String, String>();

    public static final  String PARAMS_FILE_NAME       = "app-params.xml";
	public static final  String MODULES_PATH           = "modules";
	public static final  String MODULES_PACK           = "modules";
	public static final  String CONFIGURATION_DIR_NAME = "conf";
    public static final  String BEANS_PACK_NAME        = "beans";
	public static final  String TEMPLATE_PATH          = "template";
	public static final  String BASE_HIBERNATE_PROPERTIES_FILE = "hibernate.properties";

	public static String get(String name) {
		return paramsMap.get(name);
	}

	/**
	 * Inicializa os parâmetros padrões da aplicação,
	 * seta os especificados no arquivo app-params.xml
	 * e compõe os demais parâmetros dependentes de outros.
	 * 
	 * @param applicationRealPath - endereço real da aplicação
	 * @param contextName - nome do contexto
	 * @throws java.lang.Exception
	 */
	public static void initialize(String applicationRealPath, String contextName){
		defaultParams(applicationRealPath, contextName);
		parseXML();
		compositeParams();
	}

	public static Map<String, String> getParamsMap(){
		return  paramsMap;
	}

	/**
	 * Parâmetros Padrões
	 */
	private static void defaultParams(String applicationRealPath, String contextName) {
		paramsMap.put("CONTEXT_NAME", contextName);
		paramsMap.put("WEB_REAL_PATH", applicationRealPath);
		paramsMap.put("WEBINF_PATH", get("WEB_REAL_PATH") + "WEB-INF");
		paramsMap.put("CLASSES_PATH", get("WEBINF_PATH") + "/classes/");

		//só quando for dar suporte a mais de um banco de dados
		paramsMap.put("CONNECT_EXTERNAL_MODULES", "false");
		paramsMap.put("DATABASE_DIR_NAME", "database");
		paramsMap.put("DEPLOY_MODE" , "none");
		paramsMap.put("DEV_MODE" , "off");
		paramsMap.put("DYN_EXCEPTION_FILE_PATH", "/exceptions/dyn/exception.jsp");
    	paramsMap.put("EMAIL_ACTIVE", "false");
		paramsMap.put("EMAIL_DEFAULT_PORT", "25");
		paramsMap.put("EMAIL_SSL_CONNECTION", "false");
		paramsMap.put("EXCEPTION_FILE_PATH",     "/exceptions/exception.jsp");
		paramsMap.put("GENERATE_TAGLIB",  "true");
		paramsMap.put("GLOBAL_HEAD_TITLE","");
		paramsMap.put("INIT_ACTION", "init.Index");
		paramsMap.put("INIT_MANAGER_CLASS", "org.futurepages.core.InitManager");
		paramsMap.put("INSTALL_MODE", "off");
		paramsMap.put("PAGE_ENCODING", "ISO-8859-1");
		paramsMap.put("PRETTY_URL", "false");
		paramsMap.put("MIGRATION_CLASSPATH", "");
		paramsMap.put("MINIFY_RESOURCE_MODE", "none"); //none, css, js, both
		paramsMap.put("QUARTZ_MODE", "off");
		paramsMap.put("RELEASE", "");
		paramsMap.put("SCHEMA_GENERATION_TYPE", "none");
		paramsMap.put("START_PAGE_NAME", "Index");
        paramsMap.put("THEME", "default");
        paramsMap.put("THEMES_DIR_NAME", "themes");
        paramsMap.put("USE_MODULE_DEPENDENCY", "false");
	}


	private static String pathParamsFile = null;
	/**
	 * Lê os parâmetros do arquivo xml
	 */
	private static void parseXML() {
		pathParamsFile = paramsMap.get("CLASSES_PATH")+CONFIGURATION_DIR_NAME+"/"+PARAMS_FILE_NAME;
		Element appParams;
		try {
			appParams = XmlUtil.getRootElement(pathParamsFile);
			List<Element> elements = appParams.getChildren();
			for (Element e : elements) {
				String name = e.getAttributeValue("name");
				String value = e.getAttributeValue("value");
				paramsMap.put(name, value);
			}
		} catch (IOException e) {
			throw new ConfigFileNotFoundException("Arquivo de parâmetros da aplicação não encontrado: "+pathParamsFile);
		} catch (JDOMException e) {
			throw new BadFormedConfigFileException("Arquivo de parâmetros da aplicação mal formado: "+pathParamsFile);
		}

	}

	/**
	 * Constroi os Parâmetros Compostos 
	 */
	private static void compositeParams() {
		paramsMap.put("CLASSES_REAL_PATH", get("WEBINF_PATH") + "/classes");
		paramsMap.put("MODULES_CLASSES_REAL_PATH", get("CLASSES_REAL_PATH") + "/" + Params.MODULES_PATH);
		paramsMap.put("MODULES_WEB_REAL_PATH", get("WEB_REAL_PATH") + "/" + Params.MODULES_PATH);

		if (get("START_INDEX") == null) {
            paramsMap.put("START_INDEX", get("START_PAGE_NAME") + ".fpg");
        }

		if (get("RESOURCE_PATH") == null) {
			paramsMap.put("RESOURCE_PATH", TEMPLATE_PATH + "/resource");
        }

		if (get("START_CONSEQUENCE") == null) {
			paramsMap.put("START_CONSEQUENCE", "init/" + Params.get("START_PAGE_NAME") + ".page");
		}
		if (!get("RELEASE").equals("")) {
			paramsMap.put("RELEASE_QUERY", "?release=" + Params.get("RELEASE"));
		} else {
			paramsMap.put("RELEASE_QUERY", "");
		}
	}

	public static boolean devMode(){
		return Params.get("DEV_MODE").equals("on");
	}

	private static String regexParam(String key){
		return "<param\\s+[^/]*name\\s*=\\s*\""+key+"\"[^/]*/>";
	}

	/**
	 * Remove os parâmetros de geração de banco e de compactação de recursos para que
	 * não sejam executados sempre que a aplicação for restartada.
	 * @throws Exception
	 */
	public static void removeFileAutomations() throws Exception {
		HashMap<String, String> map = new HashMap();
		map.put(regexParam("MINIFY_RESOURCE_MODE"),"");
		map.put(regexParam("SCHEMA_GENERATION_TYPE"),"");
		map.put(regexParam("INSTALL_MODE"),"");
		map.put(regexParam("MIGRATION_CLASSPATH"),"");
		FileUtil.putStrings(map, pathParamsFile, pathParamsFile, true);
	}
}