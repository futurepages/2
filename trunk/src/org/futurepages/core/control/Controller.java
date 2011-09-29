package org.futurepages.core.control;

import org.futurepages.core.consequence.Consequence;
import org.futurepages.core.context.CookieContext;
import org.futurepages.core.context.ApplicationContext;
import org.futurepages.core.context.SessionContext;
import org.futurepages.core.filter.AfterConsequenceFilter;
import org.futurepages.core.filter.GlobalFilterFree;
import org.futurepages.core.filter.Filter;
import org.futurepages.core.action.Action;
import org.futurepages.core.config.Params;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.futurepages.actions.DynAction;
import org.futurepages.core.callback.ConsequenceCallback;

import org.futurepages.core.consequence.ConsequenceProvider;
import org.futurepages.core.consequence.DefaultConsequenceProvider;
import org.futurepages.core.context.MapContext;
import org.futurepages.core.exception.DefaultExceptionLogger;
import org.futurepages.filters.GlobalFilterFreeFilter;
import org.futurepages.core.formatter.FormatterManager;
import org.futurepages.core.input.RequestInput;
import org.futurepages.core.output.ResponseOutput;
import org.futurepages.core.i18n.LocaleManager;
import org.futurepages.core.input.PrettyGlobalURLRequestInput;
import org.futurepages.core.input.PrettyURLRequestInput;
import org.futurepages.exceptions.FilterException;
import org.futurepages.filters.ConsequenceCallbackFilter;
import org.futurepages.tags.core.webcomponent.ImportComponentRes;

/**
 * The central controller. The actions are intercepted and
 * executed by this controller. The controller is also responsable for creating
 * and starting the ApplicationManager.
 *
 * @author Sergio Oliveira
 * @author Rubem Azenha (rubem.azenha@gmail.com)
 * @author Leandro Santana Pereira
 * @author Danilo Batista
 */
public class Controller extends HttpServlet {

	private char innerActionSeparator = '.';
	private final String EXTENSION = AbstractApplicationManager.EXTENSION;
	private Set<String> moduleIDs;
	private String startPage = null;
	private AbstractApplicationManager appManager = null;
	private static String appMgrClassname = null;
	private static ServletContext application = null;
	protected static ApplicationContext appContext = null;
	private static ConsequenceProvider defaultConsequenceProvider = new DefaultConsequenceProvider();
	private boolean withPrettyURL;
	private static Controller INSTANCE;
	private ThreadLocal<InvocationChain> chainTL = new ThreadLocal<InvocationChain>();

	public static Controller getInstance() {
		return INSTANCE;
	}

	/**
	 * Initialize the Controller, creating and starting the ApplicationManager.
	 */
	@Override
	public void init(ServletConfig conf) throws ServletException {
		super.init(conf);
		try {

			withPrettyURL = Params.get("PRETTY_URL").equals("true");
			startPage = Params.get("START_PAGE_NAME");

			if (withPrettyURL) {
				innerActionSeparator = '-';
			}
			INSTANCE = this;

			this.configureServlet(conf);
			initApplicationManager();
		} catch (Exception ex) {
			DefaultExceptionLogger.getInstance().execute(ex, null, true);
		}

	}

	/**
	 * Creates the AplicationManager and starts it.
	 *
	 * @throws ServletException
	 */
	private void initApplicationManager() throws ServletException {

		Class<? extends Object> klass = null;

		try {
			klass = Class.forName(appMgrClassname);
		} catch (ClassNotFoundException e) {
			throw new ServletException("Could not find application manager: " + appMgrClassname, e);
		}

		try {
			appManager = (AbstractApplicationManager) klass.newInstance();

			AbstractApplicationManager.setApplication(appContext);

			moduleIDs = appManager.moduleIds();

			appManager.init(appContext);
			appManager.loadLocales();
			appManager.loadActions();
			appManager.registerChains();

			// Load some pre-defined formatters here.
			FormatterManager.init();

			appManager.loadFormatters();

		} catch (Exception e) {
			throw new ServletException(
					"Exception while loading application manager " + appMgrClassname + ": " + e.getMessage(), e);
		}
	}

	/**
	 * Destroy all filters defined in the ApplicationManager, call the destroy()
	 * method of ApplicationManager, then call super.destroy() to destroy this
	 * servlet (the Controller).
	 */
	@Override
	public void destroy() {
		if (appManager != null) {
			Set<Filter> filters = appManager.getAllFilters();
			Iterator<Filter> iter = filters.iterator();
			while (iter.hasNext()) {
				Filter f = iter.next();
				f.destroy();
			}

			// call destroy from appmanager...
			appManager.destroy(appContext);
		}
		chainTL.remove();

		super.destroy();
		LocaleManager.stopLocaleScan();
	}

	/**
	 * Returns the ServletContext of your web application.
	 *
	 * @return The ServletContext of your web application.
	 */
	public ServletContext getApplication() {
		return application;
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		try {
			doService(req, res);
		} catch (Exception ex) {
			DefaultExceptionLogger.getInstance().execute(ex, chainTL.get(), true);
		} finally {
			chainTL.remove();
		}
	}

	protected void doService(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		if (appManager == null) {
			throw new ServletException("The Application manager is not loaded");
		}
		res.setCharacterEncoding(Params.get("PAGE_ENCODING"));

		appManager.service(appContext, req, res);

		String prettyActionUri = (withPrettyURL ? getActionPlusInnerAction(req) : null);
		String actionName = getActionName(req, prettyActionUri);
		String innerAction = getInnerActionName(req, prettyActionUri);

		ActionConfig ac = null;

		if (innerAction != null) {
			ac = appManager.getActionConfig(actionName, innerAction);
		}

		if (ac == null) {
			ac = appManager.getActionConfig(actionName);
		}

		if (ac == null) {
			if (AbstractApplicationManager.getDefaultAction() != null) {
				ac = AbstractApplicationManager.getDefaultAction();
			} else {
				throw new ServletException("Could not find the action for actionName: " + actionName + (innerAction != null ? "." + innerAction : ""));
			}
		}

		Action action = ac.getAction(); // create an action instance here...

		if (action == null) {
			throw new ServletException("Could not get an action instance: " + ac);
		}

		prepareAction(action, ac.isGlobal(), req, res);

		List<Object> filters = new LinkedList<Object>();
		Consequence c = null;
		boolean conseqExecuted = false;
		boolean actionExecuted = false;
		StringBuilder returnedResult = new StringBuilder(32);

		try {
			c = invokeAction(ac, action, innerAction, filters, returnedResult);
			actionExecuted = true;
			c.execute(action, req, res);
			conseqExecuted = true;
		} catch (Exception e) {
			Throwable cause = getRootCause(e);
			throw new ServletException("Exception while invoking action " + actionName + ": " + e.getMessage() + " / " + e.getClass().getName() + " / " + cause.getMessage() + " / " + cause.getClass().getName(), cause);
		} finally {
			if (action instanceof DynAction) {
				ImportComponentRes.destroyAsyncResources();
			}
			/*
			 * Here we check all filters that were executed together with the
			 * action. If they are AfterConsequenceFilters, we need to call the
			 * afterConsequence method.
			 */
			Iterator<Object> iter = filters.iterator();
			ArrayList<ConsequenceCallbackFilter> callbackFilters = null;
			String returnedFromAction = returnedResult.toString().length() > 0 ? returnedResult.toString() : null;
			while (iter.hasNext()) {
				Filter f = (Filter) iter.next();
				if (f instanceof AfterConsequenceFilter) {
					AfterConsequenceFilter acf = (AfterConsequenceFilter) f;
					try {
						acf.afterConsequence(action, c, conseqExecuted, actionExecuted, returnedFromAction);
					} catch (Exception e) {
						throw new ServletException("Exception while executing the AfterConsequence filters: " + e.getMessage(), e);
					}
					if(f instanceof ConsequenceCallbackFilter){
						if(callbackFilters == null){
							callbackFilters = new ArrayList<ConsequenceCallbackFilter>();
						}
						callbackFilters.add((ConsequenceCallbackFilter)f);
					}
				}
			}
			if(actionExecuted && conseqExecuted && callbackFilters!=null){
				for(ConsequenceCallbackFilter f : callbackFilters){
					try {
						ConsequenceCallback cc = f.getCallbackClass().newInstance();
						cc.setActionData(action.getCallback());
						cc.setActionReturn(returnedResult.toString());
						cc.setCaller(ac.getName()+"-"+innerAction);
						Thread thread = new Thread(cc);
						thread.start();
					} catch (Exception ex) {
						throw new ServletException("Exception while invoking Consequence Callbacks. "+ex.getMessage());
					}
				}
			}
		}
	}

	private Throwable getRootCause(Throwable t) {
		Throwable curr = t;
		while (curr.getCause() != null) {
			curr = curr.getCause();
		}
		return curr;
	}

	/**
	 * Invoke an action and return the consequence generated by this invocation.
	 * This method also return all filters that were executed together with the
	 * action inside the filters list parameter.
	 *
	 * @param ac  The ActionConfig which contains the consequences for this action.
	 * @param action The action to invoke.
	 * @param innerAction The inner action to execute or null to execute the regular action (execute() method).
	 * @param filters The filters that were applied to the action. (You should pass an empty list here!)
	 * @return A consequence generated by this invocation.
	 * @throws ActionException if there was an exception executing the action.
	 * @throws FilterException if there was an exception executing a filter for the action.
	 */
	public Consequence invokeAction(ActionConfig ac, Action action, String innerAction, List<Object> filters, StringBuilder returnedResult) throws Exception {

		InvocationChain chain = createInvocationChain(ac, action, innerAction);
		if (chainTL.get() == null) { //no caso do ChainConsequence, o chain mantido é o da action original.
			chainTL.set(chain); //setado para gerenciar o log das exceptions
		}

		// copy all filters executed together with that action to the filters parameter...

		if (filters == null || !filters.isEmpty()) {

			throw new IllegalArgumentException(
					"filters parameter should be non-null and a zero-sized list!");
		}
		Iterator<Filter> iter = chain.getFilters().iterator();

		while (iter.hasNext()) {
			filters.add(iter.next());
		}

		// execute chain!
		String result = chain.invoke();
		returnedResult.append(result);

		// If there is an inner action, try to get a consequence for the inner
		// action
		Consequence c = null;
		if (innerAction != null) {
			c = ac.getConsequence(result, innerAction);
		}

		// If not found, try to get a consequene specific for that action
		if (c == null) {
			c = ac.getConsequence(result);
		}

		// If not found, try to get a global consequence
		if (c == null) {
			c = appManager.getGlobalConsequence(result);
		}

		// use the default consequence provider...
		if (c == null) {
			c = defaultConsequenceProvider.getConsequence(ac.getName(), ac.getActionClass(), result, innerAction);
		}

		if (c == null) {
			throw new ServletException("Action has no consequence for result: " + ac.getName() + " - " + result);
		}

//		System.out.println("<#"+Thread.currentThread().getId()+"#>"+ ac.getName() + "(" + ac.getActionClass().getName() + ")" + ((innerAction!=null)?"."+innerAction:"")+"["+result.toUpperCase()+"] -> "+(c!=null?c.toString():" NULL")); //for DEBUG-MODE

		return c;
	}

	private boolean hasGlobalFilterFreeMarkerFilter(List<Filter> filters, String innerAction) {
		Iterator<Filter> iter = filters.iterator();
		while (iter.hasNext()) {
			Filter f = iter.next();
			if (GlobalFilterFreeFilter.class.isAssignableFrom(f.getClass())) {
				GlobalFilterFreeFilter gffmf = (GlobalFilterFreeFilter) f;
				return gffmf.isGlobalFilterFree(innerAction);
			}
		}
		return false;
	}

	private InvocationChain createInvocationChain(ActionConfig ac, Action action, String innerAction) {

		InvocationChain chain = new InvocationChain(ac.getName(), action);
		Object actionImpl = action;

		// first place the "firstFilters" for the action...

		List<Filter> firstFilters = ac.getFirstFilters(innerAction);
		if (firstFilters != null) {
			chain.addFilters(firstFilters);
		}

		// place the global filters that are NOT LAST...
		boolean isGlobalFilterFree = false;
		if (actionImpl instanceof GlobalFilterFree) {
			GlobalFilterFree gff = (GlobalFilterFree) actionImpl;
			isGlobalFilterFree = gff.isGlobalFilterFree(innerAction);
		}

		if (!isGlobalFilterFree) {
			List<Filter> globals = appManager.getGlobalFilters(false);
			if (globals != null) {
				chain.addFilters(globals);
			}
		}

		// place the action specific filters...
		List<Filter> filters = ac.getFilters(innerAction);
		if (filters != null) {
			isGlobalFilterFree = hasGlobalFilterFreeMarkerFilter(filters, innerAction);
			if (isGlobalFilterFree) {
				// remove previously added global filters...
				chain.clearFilters();
			}
			chain.addFilters(filters);
		}

		// place the global filters that are LAST

		if (!isGlobalFilterFree) {
			List<Filter> globals = appManager.getGlobalFilters(true);
			if (globals != null) {
				chain.addFilters(globals);
			}
		}

		if (innerAction != null) {
			chain.setInnerAction(innerAction);
		}
		return chain;
	}

	//Only for prettyURLs
	private String getActionPlusInnerAction(HttpServletRequest req) {

		String context = req.getContextPath();

		String uri = req.getRequestURI().toString();

		if (context.length() > 0 && uri.indexOf(context) == 0) {
			uri = uri.substring(context.length()); // remove the context from the uri, if present
		}

		if (uri.startsWith("/") && uri.length() > 1) {
			uri = uri.substring(1); // cut the first '/'
		}

		if (uri.endsWith("/") && uri.length() > 1) {
			uri = uri.substring(0, uri.length() - 1);  // cut the last '/'
		}

		String[] s = uri.split("/");

		if (isModule(s[0])) {
			if (s.length == 1) {
				if (s[0].equals(startPage)) {
					return s[0];
				} else if (!s[0].equals(EXTENSION)) {
					return s[0] + "/" + startPage;
				}
			} else if (s.length >= 2) {
				//para prever URLs dos módulos
				if (!s[0].equals(EXTENSION)) {
					return s[0] + "/" + s[1];
				}
				return s[1];
			}
		} else {
			return s[0];
		}

		return null;
	}

	protected String getActionName(HttpServletRequest req, String s) {
		if (!withPrettyURL) {
			return getObsoleteActionName(req);
		}

		// separate the inner action from action...
		int index = s.indexOf(innerActionSeparator);
		if (index > 0) {
			return s.substring(0, index);
		}
		return s;
	}

	protected String getInnerActionName(HttpServletRequest req, String s) {
		if (!withPrettyURL) { //s == null
			return getObsoleteInnerActionName(req);
		}
		// separate the inner action from action...
		int index = s.indexOf(innerActionSeparator);
		if (index > 0 && index + 1 < s.length()) {
			return s.substring(index + 1);
		}
		return null;
	}

	protected void prepareAction(Action action, boolean global, HttpServletRequest req, HttpServletResponse res) {

		if (withPrettyURL) {
			if (!global) {
				action.setInput(new PrettyURLRequestInput(req));
			} else {
				action.setInput(new PrettyGlobalURLRequestInput(req));
			}
		} else {
			action.setInput(new RequestInput(req));
		}
		action.setOutput(new ResponseOutput(res));
		action.setSession(new SessionContext(req, res));
		action.setApplication(appContext);
		action.setCookies(new CookieContext(req, res));
		action.setLocale(LocaleManager.getLocale(req));
		action.setCallback(new MapContext());
	}

	protected String getObsoleteActionName(HttpServletRequest req) {
		String uri = getURI(req);
		// If there is an Inner Action, cut it off from the action name
		int index = uri.lastIndexOf(innerActionSeparator);
		if (index > 0 && (uri.length() - index) >= 2) {
			uri = uri.substring(0, index);
		}
		return uri;
	}

	protected String getObsoleteInnerActionName(HttpServletRequest req) {
		String uri = getURI(req);
		String innerAction = null;
		int index = uri.lastIndexOf(".");
		if (index > 0 && (uri.length() - index) >= 2) {
			innerAction = uri.substring(index + 1, uri.length());
		}
		return innerAction;
	}

	/**
	 * Returns the URI from this request. URI = URI - context - extension. This
	 * method is used by getActionName and getInnerActionName. You may call this
	 * method in your own controller subclass. Ex: /myapp/UserAction.add.fpg
	 * will return UserAction.add
	 */
	protected String getURI(HttpServletRequest req) {
		String context = req.getContextPath();
		String uri = req.getRequestURI().toString();
		// remove the context from the uri, if present
		if (context.length() > 0 && uri.indexOf(context) == 0) {
			uri = uri.substring(context.length());
		}

		if (uri.equals("/")) {
			return Params.get("START_PAGE_NAME");
		}

		// cut the extension...
		int index = uri.lastIndexOf(".");

		if (index > 0) {
			uri = uri.substring(0, index);
		}

		// cut the first '/'
		if (uri.startsWith("/") && uri.length() > 1) {
			uri = uri.substring(1, uri.length());
		}
		return uri;
	}

	public boolean isWithPrettyURL() {
		return withPrettyURL;
	}

	public char getInnerActionSeparator() {
		return innerActionSeparator;
	}

	private void configureServlet(ServletConfig conf) {
		application = conf.getServletContext();
		appContext = new ApplicationContext(application);
		AbstractApplicationManager.setRealPath(application.getRealPath(""));

		// gets the AplicationManager class
		appMgrClassname = conf.getInitParameter("applicationManager");

		if (appMgrClassname == null || appMgrClassname.trim().equals("")) {
			appMgrClassname = "ApplicationManager"; // default without package...
		}
	}

	public boolean isModule(String pattern) {
		return moduleIDs.contains(pattern);
	}
}
