package org.futurepages.core.control;

import org.futurepages.core.consequence.Consequence;
import org.futurepages.core.context.CookieContext;
import org.futurepages.core.context.ApplicationContext;
import org.futurepages.core.context.Context;
import org.futurepages.core.context.SessionContext;
import org.futurepages.core.filter.AfterConsequenceFilter;
import org.futurepages.core.filter.GlobalFilterFree;
import org.futurepages.core.filter.Filter;
import org.futurepages.core.action.StickyAction;
import org.futurepages.core.action.AbstractAction;
import org.futurepages.exceptions.ActionException;
import org.futurepages.core.action.Action;
import org.futurepages.core.config.Params;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.futurepages.core.consequence.ConsequenceProvider;
import org.futurepages.core.consequence.DefaultConsequenceProvider;
import org.futurepages.filters.GlobalFilterFreeFilter;
import org.futurepages.core.formatter.FormatterManager;
import org.futurepages.core.input.RequestInput;
import org.futurepages.core.output.ResponseOutput;
import org.futurepages.core.i18n.LocaleManager;
import org.futurepages.core.list.ListManager;
/**
 * The Mentawai central controller. Mentawai actions are intercepted and
 * executed by this controller. The controller is also responsable for creating
 * and starting the ApplicationManager.
 *
 * @author Sergio Oliveira
 * @author Rubem Azenha (rubem.azenha@gmail.com)
 */
public class Controller extends HttpServlet {

    private static final char SEP = File.separatorChar;
    private static AbstractApplicationManager appManager = null;
    private static String appMgrClassname = null;
    private static ServletContext application = null;
    private static ServletConfig config = null;
    protected static ApplicationContext appContext = null;
    private static ConsequenceProvider consequenceProvider = null;
    private static ConsequenceProvider defaultConsequenceProvider = new DefaultConsequenceProvider();
    private static File appManagerFile = null;
    private static long lastModified = 0;
    static boolean reloadAppManager = false;
    static boolean autoView = true;
    static boolean statsMode = false;
    private static final String STICKY_KEY = "_stickyActions";

    /**
     * Initialize the Controller, creating and starting the ApplicationManager.
     *
     * @param conf
     *            the ServletConfig.
     */
	@Override
    public void init(ServletConfig conf) throws ServletException {

        super.init(conf);

        config = conf;

        application = conf.getServletContext();

        appContext = new ApplicationContext(application);

        AbstractApplicationManager.setRealPath(application.getRealPath(""));

        // verifies if the "reload mode" is on
        String reload = config.getInitParameter("reloadAppManager");

        if (reload != null && reload.equalsIgnoreCase("true")) {
            reloadAppManager = true;
        }

        // verifies if the "auto view" is on (default is on!)
        String auto = config.getInitParameter("autoView");

        if (auto != null && auto.equalsIgnoreCase("true")) {
            autoView = true;
        }

        // verifies if the "stats mode" is on
        // default is off -> Security issue!
        String stats = config.getInitParameter("stats");

        if (stats != null && stats.equalsIgnoreCase("true")) {
            statsMode = true;
        }

        // gets the AplicationManager class
        appMgrClassname = config.getInitParameter("applicationManager");

        if (appMgrClassname == null || appMgrClassname.trim().equals("")) {

            appMgrClassname = "ApplicationManager"; // default without package...

        }
        initApplicationManager();
    }

    public static void setConsequenceProvider(ConsequenceProvider provider) {

        Controller.consequenceProvider = provider;
    }

    private static boolean isAppMgrModified() {

        // not thread-safe on purpouse...

        if (appManagerFile == null) {

            StringBuffer sb = new StringBuffer(AbstractApplicationManager.getRealPath());
            sb.append(SEP).append("WEB-INF").append(SEP).append("classes").append(SEP);
            sb.append(appMgrClassname.replace('.', SEP)).append(".class");

            appManagerFile = new File(sb.toString());

        }

        if (!appManagerFile.exists()) {
            return true; // cannot find file... reload on every time !
        }

        if (appManagerFile.lastModified() != lastModified) {

            lastModified = appManagerFile.lastModified();

            return true;

        }

        return false;
    }

    /**
     * Creates the AplicationManager and starts it.
     *
     * @throws ServletException
     */
    private static void initApplicationManager() throws ServletException {

        Class<? extends Object> klass = null;

        try {
            if (reloadAppManager) {
                // for automatic class reloading !!!
                AMClassLoader loader = new AMClassLoader();
                klass = Class.forName(appMgrClassname, true, loader);
            } else {
                klass = Class.forName(appMgrClassname);
            }
        } catch (ClassNotFoundException e) {
            throw new ServletException("Could not find application manager: " + appMgrClassname, e);
        }

        try {
            appManager = (AbstractApplicationManager) klass.newInstance();

            AbstractApplicationManager.instance = appManager;

            AbstractApplicationManager.setApplication(appContext);

            appManager.init(appContext);

            appManager.loadLocales();

            appManager.loadActions();

            appManager.loadBeans();

            // Try to automatic load the lists, if the user has created them
            // inside "/lists" directory (default dir).
            ListManager.init();

            // Then load any user-defined list, because there are other ways to
            // load lists besides the above.
            appManager.loadLists();

            // Load some pre-defined formatters here.
            FormatterManager.init();

            appManager.loadFormatters();

        } catch (IOException e) {
            throw new ServletException(
                    "Exception while loading lists in application manager: " + e.getMessage(), e);
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

        super.destroy();

        LocaleManager.stopLocaleScan();
    }

    /**
     * Returns the ServletContext of your web application.
     *
     * @return The ServletContext of your web application.
     */
    public static ServletContext getApplication() {
        return application;
    }

    /**
     * Returns the URI from this request. URI = URI - context - extension. This
     * method is used by getActionName and getInnerActionName. You may call this
     * method in your own controller subclass. Ex: /myapp/UserAction.add.fpg
     * will return UserAction.add
     *
     * @param req
     * @return The URI
     */
    protected String getURI(HttpServletRequest req) {

        String context = req.getContextPath();

        String uri = req.getRequestURI().toString();

        // remove the context from the uri, if present

        if (context.length() > 0 && uri.indexOf(context) == 0) {

            uri = uri.substring(context.length());

        }

        if(uri.equals("/")){
            return Params.get("START_PAGE_NAME");
        }

        // cut the extension... (.fpg or whatever was defined in web.xml)
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

    /**
     * From the http request, get the action name. You may override this if you
     * want to extract the action name through some other way.
     *
     * @param req
     *            The http request
     * @return The action name
     */
    protected String getActionName(HttpServletRequest req) {

        String uri = getURI(req);
        // If there is an Inner Action, cut it off from the action name

        int index = uri.lastIndexOf(".");

        if (index > 0 && (uri.length() - index) >= 2) {

            uri = uri.substring(0, index);

        }

        return uri;
    }

    /**
     * The action name may include an Inner Action. For example: for
     * bookmanager.add.fpg the action name is "bookmanager" and the inneraction
     * name is "add". If you want to extract the inner action through some other
     * way you can override this method in your own controller.
     *
     * @param req
     * @return The inner action name or null if there is no inneraction.
     */
    protected String getInnerActionName(HttpServletRequest req) {

        String uri = getURI(req);

        String innerAction = null;

        int index = uri.lastIndexOf(".");

        if (index > 0 && (uri.length() - index) >= 2) {

            innerAction = uri.substring(index + 1, uri.length());

        }

        return innerAction;
    }

    /**
     * Subclasses of this controller may override this method to have a chance
     * to prepare the action before it is executed. This method creates and
     * injects in the action all contexts, input, output and locale.
     *
     * @param action
     *            The action to prepare for execution
     * @param req
     *            The http request (input will need that)
     * @param res
     *            The http response (output will need that)
     * @since 1.2
     */
    protected void prepareAction(Action action, HttpServletRequest req, HttpServletResponse res) {
        action.setInput(new RequestInput(req));
        action.setOutput(new ResponseOutput(res));
        action.setSession(new SessionContext(req, res));
        action.setApplication(appContext);
        action.setCookies(new CookieContext(req, res));
        action.setLocale(LocaleManager.getLocale(req));
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        if (appManager == null) {
            throw new ServletException("The Application manager is not loaded");
        }

        /*
         * This is very useful during development time, however it should not be
         * used in production, because it serializes all requests. Basicaly if
         * you forget to turn this off in production your site will suck!
         */
        if (reloadAppManager) {
			System.out.println("CUIDADO: reloadAppManager do Controller ligado.");
            synchronized (this) {

                if (isAppMgrModified()) {

                    initApplicationManager();
                }
            }
        }

        /*
         * This is useful for ScriptApplicationManager that needs to check on
         * every request if the script file has changed in disk.
         */
        appManager.service(appContext, req, res);

        String actionName = getActionName(req);

        /**
         * This is for ApplicationManager stats! =)
         */
        if (statsMode && actionName.equalsIgnoreCase(ApplicationManagerViewer.STATS_PAGE_NAME)) {

            ApplicationManagerViewer applicationManagerViewer = new ApplicationManagerViewer();

            applicationManagerViewer.buildApplicationManagerStats(res);

            return;

        }

        String innerAction = getInnerActionName(req);

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

        Action action = null;

        // sticky logic! Re-use instance if action is sticky (similar to
        // continuations in other frameworks, but simpler!)

        Class<? extends Object> actionClass = ac.getActionClass();

        if (StickyAction.class.isAssignableFrom(actionClass)) {

            HttpSession session = req.getSession(true);

            StickyActionMap map = (StickyActionMap) session.getAttribute(STICKY_KEY);

            if (map != null) {

                action = map.get(actionClass);
            }
        }

        if (action == null) {

            action = ac.getAction(); // create an action instance here...

        }

        if (action == null) {

            throw new ServletException("Could not get an action instace: " + ac);

        }

        prepareAction(action, req, res);

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

            e.printStackTrace();

            Throwable cause = getRootCause(e);

            throw new ServletException("Exception while invoking action " + actionName + ": " + e.getMessage() + " / " + e.getClass().getName() + " / " + cause.getMessage() + " / " + cause.getClass().getName(), cause);

        } finally {

            /*
             * Here we check all filters that were executed together with the
             * action. If they are AfterConsequenceFilters, we need to call the
             * afterConsequence method.
             */

            Iterator<Object> iter = filters.iterator();

            while (iter.hasNext()) {

                Filter f = (Filter) iter.next();

                if (f instanceof AfterConsequenceFilter) {

                    AfterConsequenceFilter acf = (AfterConsequenceFilter) f;

                    try {

                        String s = returnedResult.toString();

                        acf.afterConsequence(action, c, conseqExecuted, actionExecuted, s.length() > 0 ? s : null);

                    } catch (Exception e) {

                        throw new ServletException(
                                "Exception while executing the AfterConsequence filters: " + e.getMessage(), e);
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
     * @param ac
     *            The ActionConfig which contains the consequences for this
     *            action.
     * @param action
     *            The action to invoke.
     * @param innerAction
     *            The inner action to execute or null to execute the regular
     *            action (execute() method).
     * @param filters
     *            The filters that were applied to the action. (You should pass
     *            an empty list here!)
     * @return A consequence generated by this invocation.
     * @throws ActionException
     *             if there was an exception executing the action.
     * @throws FilterException
     *             if there was an exception executing a filter for the action.
     */
    public static Consequence invokeAction(ActionConfig ac, Action action,
            String innerAction, List<Object> filters, StringBuilder returnedResult) throws Exception {

        InvocationChain chain = createInvocationChain(ac, action, innerAction);

        // copy all filters executed together with that action to the filters
        // parameter...

        if (filters == null || filters.size() != 0) {

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

        // If not fount, try to get a consequene specific for that action

        if (c == null) {

            c = ac.getConsequence(result);

        }

//      imprime pra deubug: action e consequência
//		System.out.println("<#"+Thread.currentThread().getId()+"#>"+  ac.getName()+ ((innerAction!=null)?"."+innerAction:"")+"["+result.toUpperCase()+"] -> "+(c!=null?c.toString():" NULL"));
        // If not found, try to get a global consequence

        if (c == null) {
            c = appManager.getGlobalConsequence(result);
        }

        if (consequenceProvider != null) {

            // new consequenceProvider for Controller...

            if (c == null) {

                c = consequenceProvider.getConsequence(ac.getName(), ac.getActionClass(), result, innerAction);

                // add the consequence dynamically...

                if (c != null) {

                    if (innerAction != null && ac.getInnerAction() == null) {

                        ac.addConsequence(result, innerAction, c);

                    } else {

                        ac.addConsequence(result, c);
                    }
                }
            }

        } else {

            // use the default consequence provider...

            if (c == null && autoView) {

                //c = ac.getAutoConsequence(result, innerAction); // moved to OldAutoViewConsequenceProvider

                c = defaultConsequenceProvider.getConsequence(ac.getName(), ac.getActionClass(), result, innerAction);
            }

        }

        if (c == null) {

            throw new ActionException("Action has no consequence for result: " + ac.getName() + " - " + result);
        }

        return c;
    }

    private static boolean hasGlobalFilterFreeMarkerFilter(List<Filter> filters, String innerAction) {

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

    private static InvocationChain createInvocationChain(ActionConfig ac,
            Action action, String innerAction) {

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

    public static void adhere(StickyAction action, Class<? extends AbstractAction> actionClass) {

        Context session = action.getSession();

        StickyActionMap map = (StickyActionMap) session.getAttribute(STICKY_KEY);

        if (map == null) {

            map = new StickyActionMap(new HashMap<Object, StickyAction>());

            session.setAttribute(STICKY_KEY, map);
        }

        map.put(actionClass, action);

    }

    public static void disjoin(StickyAction action, Class<? extends AbstractAction> actionClass) {

        Context session = action.getSession();

        StickyActionMap map = (StickyActionMap) session.getAttribute(STICKY_KEY);

        if (map != null) {

            map.remove(actionClass);
        }
    }

    private static class StickyActionMap implements HttpSessionBindingListener {

        private final Map<Object, StickyAction> map;

        public StickyActionMap(Map<Object, StickyAction> map) {

            this.map = map;

        }

        public void put(Object key, StickyAction value) {

            map.put(key, value);
        }

        public StickyAction get(Object key) {

            return map.get(key);
        }

        public StickyAction remove(Object key) {

            return map.remove(key);
        }

        public void valueBound(HttpSessionBindingEvent evt) {
        }

        public void valueUnbound(HttpSessionBindingEvent evt) {

            Iterator<StickyAction> iter = map.values().iterator();

            while (iter.hasNext()) {

                StickyAction sticky = iter.next();

                sticky.onRemoved();

            }
        }
    }

    public static void setAppManager(AbstractApplicationManager applicationManager) {
        synchronized (applicationManager) {
            appManager = applicationManager;

        }
    }
}