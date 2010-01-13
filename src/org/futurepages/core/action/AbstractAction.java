package org.futurepages.core.action;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.futurepages.consequences.AjaxConsequence;
import org.futurepages.core.admin.DefaultUser;
import org.futurepages.util.HtmlMapChars;
import org.futurepages.core.context.Context;
import org.futurepages.core.control.Controller;
import org.futurepages.core.input.Input;
import org.futurepages.core.output.Output;
import org.futurepages.core.context.SessionContext;
import org.futurepages.core.i18n.LocaleManager;
import org.futurepages.core.pagination.Pageable;
import org.futurepages.core.pagination.PaginationSlice;
import org.futurepages.core.persistence.Dao;
import org.futurepages.core.validation.Validator;
import org.futurepages.exceptions.ErrorException;
import org.futurepages.filters.HeadTitleFilter;
import org.futurepages.util.Is;

/**
 * Action Base
 */
public abstract class AbstractAction implements Pageable, StickyAction {

    protected static final String USER_KEY = "user";
    protected static final String LOCALE_KEY = LocaleManager.LOCALE_KEY;
    protected Input input;
    protected Output output;
    protected Context session;
    protected Context application;
    protected Context cookies;
    protected Locale loc;
    private Map<String, String> messages;

    public AbstractAction() {
        messages = new HashMap<String, String>();
        messages.put(SUCCESS, null);
        messages.put(INFO, null);
        messages.put(ERROR, null);
        messages.put(WARNING, null);
    }

    public String putMessage(String key, String message) {
        output.setValue(key, HtmlMapChars.htmlValue(message));
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
     * Valida perante três modos: breakOnFirst = null | false | true.
     * 
     * @param breakOnFirst true se lança ErrorException na primeira falha, false se retorna no fim, e null e não retorna ErrorException
     * @return retorna o validador daquele tipo passando o tipo de validação
     */
    public <T extends Validator> T validate(Class<T> t, Boolean breakOnFirst){
        return Validator.validate(t, breakOnFirst);
    }

    /**
     * Valida quebrando retornando ErrorException na primeira falha
     * @param t Tipo do Validator
     * @return o validador
     */
    public <T extends Validator> T validate(Class<T> t){
        return Validator.validate(t, true);
    }

    /**
     * Paginação de elementos
     *
     * Depreciado por ser uma má pratica de programação (mistura controle e modelo)
     *
     * @deprecated Utilize setOutputPaginationSlice (verificar em site2 e scrummer o uso)
     */
    public <T extends Serializable> List<T> paginateList(int pageSize, Class<T> entityClass, String where, String order) {
        List<T> list = Dao.listPage(getPageNum(), pageSize, " FROM " + entityClass.getName() + " WHERE " + where + " ORDER BY " + order);
        long totalSize = Dao.numRows(entityClass, where);
        double total = totalSize;
        int totalPages = (int) Math.ceil(total / pageSize);
        setOutputPaginationValues(pageSize, totalSize, totalPages, getPageNum());
        return list;
    }

    public <T extends Serializable> void setOutputPaginationSlice(String listKey, PaginationSlice<T> slice) {
        setOutputPaginationValues(slice.getPageSize(), slice.getTotalSize(), slice.getTotalPages(), slice.getPageNumber());
        output.setValue(listKey, slice.getList());
    }

    protected void setOutputPaginationValues(int pageSize, long totalSize, int totalPages, int pageNum) {
        output.setValue(_TOTAL_SIZE, (int) totalSize);
        output.setValue(_TOTAL_PAGES, totalPages);
        output.setValue(_PAGE_NUM, pageNum);
        output.setValue(_PAGE_SIZE, pageSize);
    }

    /**
     * @return Pega o numero da página corrente em uso
     */
    protected int getPageNum() {
        int pageNum = 1;
        if (input.getValue(_PAGE_NUM) != null) {
            pageNum = input.getIntValue(_PAGE_NUM);
        }
        return pageNum;
    }

    protected int getPageSize(int defaultPageSize) {
        try {
            if (!Is.selected(input.getIntValue(_PAGE_SIZE))) {
                throw new Exception();
            }
            return input.getIntValue(_PAGE_SIZE);
        } catch (Exception ex) {
            return defaultPageSize;
        }

    }

    /**
     * Pega o objeto que se encontra no input com uma determinada chave String passada
     * por parâmetro e coloca-o no output com a mesma chave.
     * 
     * @param key nome da chave do input que irá pro output.
     */
    protected void fwdValue(String key) {
        output.setValue(key, input.getValue(key));
    }

    public void setSessionTime(int minutes) {
        ((SessionContext) session).getSession().setMaxInactiveInterval(minutes * 60);
    }

    protected void headTitle(String headTitle) {
        output.setValue(HEAD_TITLE, headTitle);
    }

    protected void headTitleAppend(String headTitle) {
        output.setValue(HEAD_TITLE, output.getValue(HEAD_TITLE) + HeadTitleFilter.SEPARATOR + headTitle);
    }

    protected void outputAjax(Map map) {
        output.setValue(AjaxConsequence.KEY, map);
    }

    public void doListDependencies() {
        listDependencies();
    }

    protected void listDependencies() {
    }

    public boolean hasSuccess() {
        return messages.get(Action.SUCCESS) != null;
    }

    public boolean hasError() {
        return messages.get(ERROR) != null;
    }

    public String getError() {
        return messages.get(ERROR);
    }

    public HttpServletRequest getRequest() {
        return ((SessionContext) session).getRequest();
    }

    public HttpServletResponse getResponse() {
        return ((SessionContext) session).getResponse();
    }

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
    public boolean isLogged() {
        return isLogged(session);
    }

    /**
     * Retorna o usuário logado da sessão. Por baixo dos panos é session.getAttribute(USER_KEY)
     * USER_KEY por padrão é "user"
     *
     * @return O usuário logado.
     */
    public DefaultUser loggedUser() {
        return loggedUser(session);
    }

    protected String success() {
        return success("");
    }

    protected String success(String msg) {
        return this.putMessage(SUCCESS, msg);
    }

    public void showAsError(Exception exception) {
            throw new ErrorException(exception);
    }

    public String error(boolean listDependencies, ErrorException errorException) {
        this.putMessage(ERROR, errorException.getMessage());
        output.setValue("errorList", errorException.getValidationMap());
        if (listDependencies) {
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

    public void setInput(Input input) {
        this.input = input;
    }

    public void setOutput(Output output) {
        this.output = output;
    }

    public void setSession(Context context) {
        this.session = context;
    }

    public void setApplication(Context context) {
        this.application = context;
    }

    public void setCookies(Context context) {
        this.cookies = context;
    }

    public void setLocale(Locale loc) {
        this.loc = loc;
    }

    public Input getInput() {
        return input;
    }

    public Output getOutput() {
        return output;
    }

    public Context getSession() {
        return session;
    }

    public Context getApplication() {
        return application;
    }

    public Context getCookies() {
        return cookies;
    }

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

    public void adhere() {

        Controller.adhere(this, this.getClass());
    }

    public void disjoin() {

        Controller.disjoin(this, this.getClass());
    }

    public void onRemoved() {
        // subclasses can override this to trap this callback for sticky actions...
    }

    public String getContextPath() {
        return input.getProperty("contextPath");
    }
}