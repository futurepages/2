package org.futurepages.core.action;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.Cookie;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.futurepages.consequences.AjaxConsequence;
import org.futurepages.consequences.StringConsequence;
import org.futurepages.core.admin.Authentication;
import org.futurepages.core.admin.DefaultUser;
import org.futurepages.core.context.Context;
import org.futurepages.core.context.SessionContext;
import org.futurepages.core.control.InvocationChain;
import org.futurepages.core.i18n.LocaleManager;
import org.futurepages.core.input.Input;
import org.futurepages.core.output.MapOutput;
import org.futurepages.core.output.Output;
import org.futurepages.core.pagination.Pageable;
import org.futurepages.core.pagination.PaginationSlice;
import org.futurepages.core.pagination.Paginator;
import org.futurepages.core.path.Paths;
import org.futurepages.core.validation.Validator;
import org.futurepages.exceptions.ErrorException;
import org.futurepages.filters.HeadTitleFilter;
import org.futurepages.filters.ModuleIdFilter;
import org.futurepages.util.Is;
import org.futurepages.util.Security;
import org.futurepages.util.The;
import org.futurepages.util.html.HtmlMapChars;

/**
 * Action Base
 */

public abstract class AbstractAction implements Pageable, Action {

	protected static final String USER_KEY = "user";
	protected static final String LOCALE_KEY = LocaleManager.LOCALE_KEY;
	protected Input input;
	protected Output output;
	protected Context session;
	protected Context application;
	protected Context cookies;
	protected Context callback;
	protected Dispatcher dispatcher;
	protected Locale loc;
	private Map<String, String> messages;
	protected boolean listingDependencies;
	private Paginator paginator;

	private InvocationChain chain;
	
	public AbstractAction() {
		messages = new HashMap<String, String>();
		messages.put(SUCCESS, null);
		messages.put(INFO, null);
		messages.put(ERROR, null);
		messages.put(WARNING, null);
		listingDependencies = false;

		dispatcher = new Dispatcher();
	}

	public String putMessage(String key, String message) {
		output(key, HtmlMapChars.htmlValue(message));
		messages.put(key, message);
		return key;
	}

	public String getMessage(String key) {
		return messages.get(key);
	}

	public String execute() throws Exception {
		doListDependencies();
		return SUCCESS;
	}

	/**
	 * Valida perante três modos: breakOnFirst =  false | true.
	 * 
	 * @param breakOnFirst true se lança ErrorException na primeira falha, false para retornar todos os erros.
	 * @return retorna o validador daquele tipo passando o tipo de validação
	 */
	public <V extends Validator> V validate(Class<V> t, boolean breakOnFirst){
		V validator = Validator.validate(t, breakOnFirst);
		return validator;
	}

	/**
	 * Valida quebrando retornando ErrorException na primeira falha
	 * @param t Tipo do Validator
	 * @return o validador
	 */
	public <V extends Validator> V validate(Class<V> t){
		return Validator.validate(t, true);
	}

	protected Paginator getPaginator(){
		if(paginator == null){
			paginator = new Paginator(output, input);
		}
		return paginator;
	}

	protected Paginator getPaginator(int defaultPageSize){
		getPaginator().setDefaultPageSize(defaultPageSize);
		return paginator;
	}

	/**
	 * Paginação de elementos
	 *
	 * Depreciado por ser uma má pratica de programação (mistura controle e modelo)
	 *
	 * @deprecated Utilize setOutputPaginationSlice (verificar em site2 e scrummer o uso)
	 */
	public <T extends Serializable> List<T> paginateList(int pageSize, Class<T> entityClass, String where, String order) {
		return getPaginator().paginateList(pageSize, entityClass, where, order);
	}

	/**
	 * Atenção: Este método é legado do Futurepages 1 e aparentemente não está funcionando corretamente
	 * na contagem dos itens para paginação.
	 * 
	 * Utilize Dao.reportPage() or Dao.listReports()
	 * 
	 * @deprecated
	 */
	@Deprecated
	public List paginateReport(int pageSize, Class entityClass, String fields, String where, String group, String order, Class resultClass) {
		return getPaginator().paginateReport(pageSize, entityClass, resultClass,fields, where, group, order);
	}

	public <T extends Serializable> void setOutputPaginationSlice(String listKey, PaginationSlice<T> slice) {
		getPaginator().setOutputPaginationSlice(listKey, slice);
	}

	protected Object input(String key){
		return input.getValue(key);
	}

	@Override
	public void output(String key, Object obj){
		output.setValue(key, obj);
	}
	protected void outputOnly(String key, Object obj){
		clearOutput();
		output(key, obj);
	}

	protected void outputPrettyUrlParams(Object... params){
		output(PRETTY_URL_PARAMS, params);
	}


	protected void clearOutput(){
		output = new MapOutput();
	}

	protected String onlySuccess(String msg){
		clearOutput();
		return success(msg);
	}

	
	protected String onlyError(String msg){
		clearOutput();
		return error(msg);
	}

	protected String onlyError(Exception ex){
		clearOutput();
		return error(ex.getMessage());
	}

	@Override
	public String redir(String url) {
			return redir(url,false);
	}

	@Override
	public String redir(String url, boolean keepOutput) {
		if(AsynchronousManager.isAjaxAction(chain)){
			outputAjax(Paths.context(getRequest())+url);
			return AJAX_REDIR;
		} else {
			String howToRedir = REDIR_APPEND_OUTPUT;
			if(!keepOutput){
				clearOutput();
				howToRedir = REDIR;
			}
			output(REDIR_URL, url);
			return howToRedir;
		}

	}

	/** @return Pega o numero da página corrente em uso */
	protected int getPageNum() {
		return getPaginator().getPageNum();
	}

	/** @return Pega o tamanho do deslocamento dos elementos na página */
	protected int getOffsetPages(){
		return getPaginator().getPagesOffset();
	}

	protected int getPageSize(int defaultPageSize) {
		return getPaginator().getPageSize(defaultPageSize);
	}

	protected int getPageSize(int defaultPageSize, int maxPageSize) {
		return getPaginator().getPageSize(defaultPageSize, maxPageSize);
	}

	/**
	 * Pega o objeto que se encontra no input com uma determinada chave String passada
	 * por parâmetro e coloca-o no output com a mesma chave.
	 * 
	 * @param key nome da chave do input que irá pro output.
	 */
	protected void fwdValue(String key) {
		output(key, input.getValue(key));
	}

	public void setSessionTime(int minutes) {
		((SessionContext) session).getSession().setMaxInactiveInterval(minutes * 60);
	}

	public void headTitle(String headTitle) {
		output(HEAD_TITLE, headTitle);
	}
	
    public void headTitleAppend(String headTitle) {
		String previousValue = (String)output.getValue(HEAD_TITLE);
		if(HeadTitleFilter.isPretty()){
			if(!Is.empty(previousValue)){
				output(HEAD_TITLE, The.concat(headTitle , HeadTitleFilter.SEPARATOR ,previousValue ));
			}else{
				headTitle(headTitle);
			}
		}else{
			if(!Is.empty(previousValue)){
				output(HEAD_TITLE, The.concat(previousValue , HeadTitleFilter.SEPARATOR , headTitle));
			}else{
				headTitle(headTitle);
			}
		}
    }

	public void setModuleId(String moduleId){
		ModuleIdFilter.setModuleId(this, moduleId);
	}

	protected void outputAjax(Object object) {
		output(AjaxConsequence.KEY, object);
	}

	protected void outputString(String responseValue) {
		output(StringConsequence.KEY, responseValue);
	}

	public void doListDependencies() {
		listingDependencies = true;
		listDependencies();
		listingDependencies = false;
	}

	protected void listDependencies() {
	}
	
	@Override
	public String accessDenied(){
		return Authentication.accessDenied(this.getChain());
	}

	public String accessDenied(String accessMessage){
		output("accessMessage", accessMessage);
		return accessDenied();
	}

	@Override
	public boolean hasSuccess() {
		return (messages.get(Action.SUCCESS) != null) || (getRequest().getParameter(Action.SUCCESS)!=null);
	}

	@Override
	public String getSuccess() {
		return messages.get(SUCCESS);
	}

	@Override
	public boolean hasError() {
		return messages.get(ERROR) != null || (getRequest().getParameter(Action.ERROR)!=null);
	}

	@Override
	public String getError() {
		return messages.get(ERROR);
	}

	@Override
	public HttpServletRequest getRequest() {
		return ((SessionContext) session).getRequest();
	}

	@Override
	public HttpServletResponse getResponse() {
		return ((SessionContext) session).getResponse();
	}

	@Override
	public HttpSession getHttpSession() {
		return ((SessionContext) session).getSession();
	}

	@Override
	public String getIpsFromClient() {
		return getIpsFromRequest(this.getRequest());
	}

	public static String getIpsFromRequest(HttpServletRequest req){
		String ipClientReal = req.getHeader("x-forwarded-for");
		String ipResult;
		if (ipClientReal == null) {
			ipResult = req.getRemoteAddr();
		} else {
			ipResult = ipClientReal;
		}
		return ipResult;
	}

	/**
	 * Verifica se possui usuário logado.
	 * @return true se está logado.
	 */
	@Override
	public boolean isLogged() {
		return isLogged(session);
	}

	/**
	 * Retorna o usuário logado da sessão. Por baixo dos panos é session.getAttribute(USER_KEY)
	 * USER_KEY por padrão é "user"
	 *
	 * @return O usuário logado.
	 */
	@Override
	public DefaultUser loggedUser() {
		return loggedUser(session);
	}

	@Override
	public String success() {
		return success("");
	}

	@Override
	public String success(String msg) {
		return this.putMessage(SUCCESS, msg);
	}

    public String error() {
        return error("");
    }

    protected String error(String msg) {
        return this.putMessage(ERROR, msg);
    }

	public String warning(){
		return warning(false, "");
	}

	public String warning(String msg){
		return warning(false, msg);
	}

	public String info() {
		return putMessage(INFO, "");
	}

	public String info(String infoMsg) {
		return putMessage(INFO, infoMsg);
	}

	protected String error(ErrorException ex) {
        return this.putError(true, ex);
    }

	protected ErrorException errorEx(String message) {
        return new ErrorException(message);
    }

	protected ErrorException errorEx(Exception ex) {
        return new ErrorException(ex.getMessage());
    }

    public String putError(boolean listDependencies, ErrorException errorException) {
        this.putMessage(ERROR, errorException.getMessage());

		if(AsynchronousManager.isAjaxAction(chain)){
			outputAjax(errorException.getValidationMap());
			return AJAX_ERROR;
		}else{
			output("errorList", errorException.getValidationMap());
		}

		if (listDependencies && !listingDependencies) {
			this.doListDependencies();
		}
	    //@dynActions em crudActions listam dependência... para não listarem, tem que anotar o método com @NotListDependencies
		return ERROR;
	}

	private String warning(boolean listDependencies, String warningMsg) {
		this.putMessage(WARNING, warningMsg);
		if (listDependencies) {
			this.doListDependencies();
		}
		return WARNING;
	}

	public String ajaxError(Exception ex) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(ERROR, ex.getMessage());
		outputAjax(map);
		return AJAX_ERROR;
	}

	public String ajaxError(String errorMsg) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(ERROR, errorMsg);
		outputAjax(map);
		return AJAX_ERROR;
	}

	public String ajaxSuccess(String successMsg) {
		outputAjax(successMsg);
		return AJAX_SUCCESS;
	}

	@Override
	public boolean hasNoCache(){
		return The.bool((Boolean)session.getAttribute("noCache"));
	}

	protected void setNoCache(){
		session.setAttribute("noCache", true);
	}

	@Override
	public boolean isGet() {
		String method = getRequest().getMethod();
		boolean isPost = method != null && method.equalsIgnoreCase("get");
		return isPost;
	}

	@Override
	public boolean isPost() {
		String method = getRequest().getMethod();
		boolean isPost = method != null && method.equalsIgnoreCase("post");
		return isPost;
	}

	@Override
	public void setInput(Input input) {
		this.input = input;
	}

	@Override
	public void setOutput(Output output) {
		this.output = output;
	}

	@Override
	public void setSession(Context context) {
		this.session = context;
	}

	@Override
	public void setApplication(Context context) {
		this.application = context;
	}

	@Override
	public void setCookies(Context context) {
		this.cookies = context;
	}

	@Override
	public void setLocale(Locale loc) {
		this.loc = loc;
	}

	@Override
	public Input getInput() {
		return input;
	}

	@Override
	public Output getOutput() {
		return output;
	}

	@Override
	public Context getCallback(){
		return callback;
	}

	@Override
	public void setCallback(Context callback){
		this.callback = callback;
	}

	@Override
	public Context getSession() {
		return session;
	}

	@Override
	public Context getApplication() {
		return application;
	}

	@Override
	public Context getCookies() {
		return cookies;
	}

	@Override
	public Cookie getCookie(String key) {
        Cookie[] cookiesArray = this.getRequest().getCookies();
        if (cookiesArray != null) {
            for (int i = 0; i < cookiesArray.length; i++) {
                if (cookiesArray[i].getName().equals(key)) {
                    return cookiesArray[i];
                }
            }
        }
        return null;
	}

	@Override
	public Locale getLocale() {
		return loc;
	}

	@Override
	public Map<String, String> getMessages() {
		return messages;
	}

	@Override
	public void setMessages(Map<String, String> messages) {
		this.messages = messages;
	}

	public Locale getUserLocale() {
		return getUserLocale(session);
	}

	public static Locale getUserLocale(HttpSession session) {
		return (Locale) session.getAttribute(LOCALE_KEY);
	}

	public static Locale getUserLocale(Context session) {
		return (Locale) session.getAttribute(LOCALE_KEY);
	}

	public static boolean isLogged(Context session) {
		return session.hasAttribute(USER_KEY);
	}

	public static boolean isLogged(HttpServletRequest req) {
		return req.getSession().getAttribute(USER_KEY)!=null ;
	}

	public static DefaultUser loggedUser(HttpServletRequest req) {
		return (DefaultUser) req.getSession().getAttribute(USER_KEY);
	}

	public static DefaultUser loggedUser(Context session) {
		return (DefaultUser) session.getAttribute(USER_KEY);
	}

	public static DefaultUser loggedUser(HttpSession session) {
		return (DefaultUser) session.getAttribute(USER_KEY);
	}

	public String getContextPath() {
		return input.getProperty("contextPath");
	}

	public InvocationChain getChain(){
		return this.chain;
	}

	@Override
	public void setChain(InvocationChain chain) {
		if(this.chain==null){
			this.chain = chain;
		}
	}

	protected class Dispatcher {

		private String hash;

		Map<String, String> messages;
		Output output;

		public Dispatcher(){
		}

		public void load(){
			load(true);
		}

		public void load(boolean loadAndRemove){
			load("dispatcherHash",loadAndRemove);
		}

		public void load(String hashKey){
			load(hashKey,true);
		}


		public void load(String hashKey, boolean loadAndRemove){
			String sessionKey = input.getStringValue(hashKey);
			Dispatcher dispatcher = (Dispatcher) session.getAttribute("dispatcher_"+sessionKey);
			if(dispatcher!=null){
				this.output = dispatcher.output;
				this.messages = dispatcher.messages;
				if(loadAndRemove){
					session.removeAttribute("dispatcher_"+sessionKey);
				}
				dispatcher = null;
				
				setOutput(this.output);
				setMessages(this.messages);
			}else{
				throw new DispatcherNotPresentException();
			}
		}

		public String hash(){
			if(hash == null){
				hash = The.concat(Security.md5(getChain().getActionName()).substring(0,6),
								",",String.valueOf(Thread.currentThread().getId()),
								",",String.valueOf(System.currentTimeMillis())
					);
				this.output = getOutput();
				this.messages = getMessages();
				session.setAttribute("dispatcher_"+hash, this);
			}
			return hash;
		}
	}

	protected class DispatcherNotPresentException extends RuntimeException {}

}