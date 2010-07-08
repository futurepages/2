package org.futurepages.core.action;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.futurepages.actions.AjaxAction;
import org.futurepages.consequences.AjaxConsequence;
import org.futurepages.core.admin.DefaultUser;
import org.futurepages.core.context.Context;
import org.futurepages.core.context.SessionContext;
import org.futurepages.core.i18n.LocaleManager;
import org.futurepages.core.input.Input;
import org.futurepages.core.output.MapOutput;
import org.futurepages.core.output.Output;
import org.futurepages.core.pagination.Pageable;
import org.futurepages.core.pagination.PaginationSlice;
import org.futurepages.core.pagination.Paginator;
import org.futurepages.core.validation.Validator;
import org.futurepages.exceptions.ErrorException;
import org.futurepages.filters.HeadTitleFilter;
import org.futurepages.util.StringUtils;
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
	protected Locale loc;
	private Map<String, String> messages;
	protected boolean listingDependencies;
	private Paginator paginator;
	
	
	public AbstractAction() {
		messages = new HashMap<String, String>();
		messages.put(SUCCESS, null);
		messages.put(INFO, null);
		messages.put(ERROR, null);
		messages.put(WARNING, null);
		listingDependencies = false;
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

	private Paginator getPaginator(){
		if(paginator == null){
			paginator = new Paginator(output, input);
		}
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

	public <T extends Serializable> void setOutputPaginationSlice(String listKey, PaginationSlice<T> slice) {
		getPaginator().setOutputPaginationSlice(listKey, slice);
	}

	protected void setOutputPaginationValues(int pageSize, long totalSize, int totalPages, int pageNum) {
		setOutputPaginationValues(pageSize, totalSize, totalPages, pageNum);
	}

	protected Object input(String key){
		return input.getValue(key);
	}
	
	protected void output(String key, Object obj){
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
		putMessage(SUCCESS, msg);
		return SUCCESS;
	}

	protected String redir(String url) {
		return redir(url,false);
	}

	protected String redir(String url, boolean preservOutput) {
		if(!preservOutput){
			clearOutput();
		}
		output(REDIR_URL, url);
		return REDIR;
	}

	/** @return Pega o numero da página corrente em uso */
	protected int getPageNum() {
		return getPaginator().getPageNum();
	}

	protected int getPageSize(int defaultPageSize) {
		return getPaginator().getPageSize(defaultPageSize);
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

	protected void headTitle(String headTitle) {
		output(HEAD_TITLE, headTitle);
	}

    public void headTitleAppend(String headTitle) {
        output(HEAD_TITLE, StringUtils.concat((String)output.getValue(HEAD_TITLE) , HeadTitleFilter.SEPARATOR , headTitle));
    }

	public void headTitleAppendToRoot(String headTitle) {
        output(HEAD_TITLE, StringUtils.concat(HeadTitleFilter.SEPARATOR , headTitle));
    }

	protected void outputAjax(Map map) {
		output(AjaxConsequence.KEY, map);
	}

	public void doListDependencies() {
		listingDependencies = true;
		listDependencies();
		listingDependencies = false;
	}

	protected void listDependencies() {
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

	public HttpServletRequest getRequest() {
		return ((SessionContext) session).getRequest();
	}

	public HttpServletResponse getResponse() {
		return ((SessionContext) session).getResponse();
	}

	@Override
	public HttpSession getHttpSession() {
		return ((SessionContext) session).getSession();
	}

	public String getIpsFromClient() {
		String ipClientReal = getRequest().getHeader("x-forwarded-for");
		String ipResult;
		if (ipClientReal == null) {
			ipResult = getRequest().getRemoteAddr();
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

	protected String success() {
		return success("");
	}

	protected String success(String msg) {
		return this.putMessage(SUCCESS, msg);
	}

    protected String error() {
        return error("");
    }

    protected String error(String msg) {
        return this.putMessage(ERROR, msg);
    }

	protected String error(ErrorException ex) {
        return this.putError(true, ex);
    }

    public String putError(boolean listDependencies, ErrorException errorException) {
        this.putMessage(ERROR, errorException.getMessage());

		if(this instanceof AjaxAction){
			outputAjax(errorException.getValidationMap());
		}else{
			output("errorList", errorException.getValidationMap());
		}

		if (listDependencies && !listingDependencies) {
			this.doListDependencies();
		}
		return ERROR;
	}

	public String warning(boolean listDependencies, String warningMsg) {
		this.putMessage(WARNING, warningMsg);
		if (listDependencies) {
			this.doListDependencies();
		}
		return WARNING;
	}

	public String info(AbstractAction action, String infoMsg) {
		return action.putMessage(INFO, infoMsg);
	}

	public boolean isPost() {
		String method = input.getProperty("method");
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
	public Locale getLocale() {
		return loc;
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

	public static DefaultUser loggedUser(Context session) {
		return (DefaultUser) session.getAttribute(USER_KEY);
	}

	public static DefaultUser loggedUser(HttpSession session) {
		return (DefaultUser) session.getAttribute(USER_KEY);
	}

	public String getContextPath() {
		return input.getProperty("contextPath");
	}
}
