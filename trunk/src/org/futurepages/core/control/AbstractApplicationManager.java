package org.futurepages.core.control;

import org.futurepages.core.consequence.Forward;
import org.futurepages.core.consequence.Chain;
import org.futurepages.core.consequence.Redirect;
import org.futurepages.core.consequence.Consequence;
import org.futurepages.core.context.Context;
import org.futurepages.core.filter.Filter;
import org.futurepages.consequences.StreamConsequence;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.futurepages.consequences.AjaxConsequence;
import org.futurepages.core.action.Manipulable;
import org.futurepages.core.ajax.AjaxRenderer;
import org.futurepages.core.consequence.ConsequenceProvider;
import org.futurepages.filters.InjectionFilter;
import org.futurepages.filters.OutjectionFilter;
import org.futurepages.core.list.ListData;
import org.futurepages.core.list.ListManager;

/**
 * The central abstract base manager which controls actions, filters, locales and data lists.
 * You can use this class to register actions and filters through the loadActions() method.
 * You can use this class to specify supported locales through the loadLocales() method.
 * You can use this class to manage the data list loading process.
 * You can use this class to initialize anything for your web application.
 *
 * @author Sergio Oliveira Jr.
 * @author Fernando Boaglio.
 * @author Leandro Santana Pereira.
 */
public abstract class AbstractApplicationManager  implements Manipulable{

	public static String EXTENSION = "fpg";

    private static String realPath;

	private Map<String, ActionConfig> actions = new HashMap<String, ActionConfig>();
    private Map<String, Map<String, ActionConfig>> innerActions = new HashMap<String, Map<String, ActionConfig>>();
	private List<Filter> globalFilters = new LinkedList<Filter>();
    private List<Filter> globalFiltersLast = new LinkedList<Filter>();
	private Map<String, Consequence> globalConsequences = new HashMap<String, Consequence>();

    static AbstractApplicationManager instance = null;

    private static String viewDir = null;

    private static ActionConfig defaultAction = null;

    private static Context appContext = null;

    private List<String> actionPackages = new LinkedList<String>();

    static void setApplication(Context appContext) {
       AbstractApplicationManager.appContext = appContext;
    }

    public static Context getApplication() {
       return appContext;
    }

    public static AbstractApplicationManager getInstance() {
        return instance;
    }

    public static void setRealPath(String realpath) {
        realPath = realpath;
    }

    private String findHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setDefaultAction(ActionConfig ac) {

       AbstractApplicationManager.defaultAction = ac;
    }

    public static ActionConfig getDefaultAction() {

       return defaultAction;
    }

    public Properties getProperties() {

    	if (realPath == null) throw new IllegalStateException("Realpath is not set for this application!");

    	String hostname = findHostName();

      File fileWithHostname = null;

    	if (hostname != null) {

    	   fileWithHostname = new File(realPath + File.separator + "WEB-INF" + File.separator + "appManager-" + hostname + ".properties");

      }

    	File file = new File(realPath + File.separator + "WEB-INF" + File.separator + "appManager.properties");

    	if (fileWithHostname != null && fileWithHostname.exists()) {

    		Properties props = new Properties();

    		try {

    			props.load(new FileInputStream(fileWithHostname));

    		} catch(Exception e) {

    			throw new RuntimeException(e);
    		}

    		return props;

    	} else if (file.exists()) {

    		Properties props = new Properties();

    		try {

    			props.load(new FileInputStream(file));

    		} catch(Exception e) {

    			throw new RuntimeException(e);
    		}

    		return props;

    	} else {

    		throw new RuntimeException("Cannot find appManager.properties or appManager-HOSTNAME.properties inside WEB-INF!");
    	}

    }

    public StreamConsequence stream(String contentType) {

    	return new StreamConsequence(contentType);
    }

    public void addActionPackage(String actionPackage) {

    	actionPackages.add(actionPackage);
    }

    public void removeActionPackage(String actionPackage) {

    	actionPackages.remove(actionPackage);
    }

    public static void setViewDir(String viewDir) {

    	AbstractApplicationManager.viewDir = viewDir;
    }

    public static String getViewDir() {

    	return viewDir;
    }

    /**
     * Returns this web application's real path.
     * For example: c:\program files\tomcat\webapps\myapplication
     *
     * @return The real path
     */
    public static String getRealPath() {
        return realPath;
    }

    /**
     * Default constructor
     */
    public AbstractApplicationManager(){
    	super();
    }

    /**
     * Reset this application manager. All configuration (actions, filters, etc) is discarded.
     */
    public void reset() {
        actions.clear();
        innerActions.clear();
        globalFilters.clear();
        globalFiltersLast.clear();
        globalConsequences.clear();
    }

    void service(Context appContext, HttpServletRequest req, HttpServletResponse res) {

    }

	/**
	 * Register an ActionConfig for the Mentawai controller.
     *
     * Note: Starting from version 1.2, this method is returning the action config it receives.
	 *
	 * @param ac The ActionConfig to register
     * @return The ActionConfig it receives to register
     * @throws IllegalStateException if you try to add an action config with no name (internal action config)
	 */
	public ActionConfig addActionConfig(ActionConfig ac) {
        if (ac.getName() == null) throw new IllegalStateException("Cannot add an action config without a name!");
        String innerAction = ac.getInnerAction();
        if (innerAction == null) {
		    actions.put(ac.getName(), ac);
        } else {
            Map<String, ActionConfig> map = innerActions.get(ac.getName());
            if (map == null) {
                map = new HashMap<String, ActionConfig>();
                innerActions.put(ac.getName(), map);
            }
            map.put(innerAction, ac);
        }
        return ac;
	}

	/**
	 * Remove an action config from this application manager.
	 *
	 * @param ac The action config to remove
	 * @return true if removed, false if not found
	 * @since 1.8
	 */
	public boolean removeActionConfig(ActionConfig ac) {

		String name = ac.getName();

		if (name == null) throw new IllegalStateException("Cannot remove an action config without a name!");

		String innerAction = ac.getInnerAction();

		if (innerAction == null) {

			return actions.remove(name) != null;

		} else {

			Map<String, ActionConfig> map = innerActions.get(name);

            if (map != null) {

            	return map.remove(innerAction) != null;
            }

            return false;
		}
	}

    /**
     * Shorter version of addActionConfig.
     *
     * @param ac
     * @since 1.2
     * @return The ActionConfig it receives
     */
    public ActionConfig add(ActionConfig ac) {
        return addActionConfig(ac);
    }

    /**
     * Override this method to do any initialization for your web application.
     *
     * @deprecated Use init(Context application) instead.
     */
    public void init() { }

    /**
     * Override this method to do any initialization for your web application.
     *
     * @param application The application context of your web application.
     * @since 1.1
     */
    public void init(Context application) {
        init(); // for deprecation...
    }

    /**
     * Called by the controller when the application is exiting.
     *
     * OBS: This is called by the Controller servlet's destroy method.
     *
     * @param application
     * @since 1.4
     */
    public void destroy(Context application) {


    }

    /**
     * Override this method to register your mentabeans.
     */
    public void loadBeans() { }

	/**
	 * Override this method to register actions and filters in this application manager.
	 */
	public void loadActions() { }

	/**
	 * Override this method to specify the supported locales for your application.
	 */
	public void loadLocales() {	}

	/**
	 * Override this method to control the data list loading process.
	 */
	public void loadLists() throws IOException {

	}

    /**
     * Override this method to define formatters that can be used by fpg:out tag
     */
    public void loadFormatters() {

    }

	/**
	 * Gets the ActionConfig with the given name or alias.
	 *
	 * @param name The name of the ActionConfig
	 * @return The ActionConfig associated with the given name
	 */
	public ActionConfig getActionConfig(String name) {

		ActionConfig ac = actions.get(name);

		if (ac == null) {

			ac = loadActionConfig(name);
		}

		return ac;
	}


	protected Map<String, ActionConfig> getActions() {
		return actions;
	}

	/**
	 * Gets the Inner ActionConfig with the given name and inner action.
	 *
	 * @param name The name of the ActionConfig
     * @param innerAction The inner action of the ActionConfig.
	 * @return The Inner ActionConfig associated with the given name and inner action.
	 */
	public ActionConfig getActionConfig(String name, String innerAction) {

		ActionConfig ac = null;

        Map<String, ActionConfig> map = innerActions.get(name);

        if (map != null) {

        	ac = map.get(innerAction);
        }

        if (ac == null) {

        	ac = loadActionConfig(name);
        }

        return ac;
	}

	private ActionConfig loadActionConfig(String name) {

		StringBuilder sb = new StringBuilder(32);

		Iterator<String> iter = actionPackages.iterator();

		while(iter.hasNext()) {

			String actionPackage = iter.next();

			sb.setLength(0);

			sb.append(actionPackage).append('.').append(name);

			// check if action is in the classpath...

			Class<? extends Object> actionClass = null;

			try {

				actionClass = Class.forName(sb.toString());

			} catch(Exception e) {

				continue;
			}

			ActionConfig ac = new ActionConfig(actionClass);

			addActionConfig(ac);

			return ac;
		}

		return null;
	}

	/**
	 * Register a filter for all actions in this application manager.
	 * The filters registered with this method will be executed <i>before</i>
     * the specific action filters.
     *
	 * @param filter The filter to register as a global filter.
	 */
	public void addGlobalFilter(Filter filter) {
		addGlobalFilter(filter, false);
	}

    /**
     * Shorter version of addGlobalFilter.
     *
     * @param filter
     * @since 1.2
     */
    public void filter(Filter filter) {
        addGlobalFilter(filter);
    }

	/**
	 * Register a list of filters for all actions in this application manager.
	 * The filters registered with this method will be executed <i>before</i>
     * the specific action filters.
     *
	 * @param filters A list of filters.
     * @since 1.1.1
	 */
	public void addGlobalFilter(List filters) {
		addGlobalFilter(filters, false);
	}

    /**
     * Shorter version of addGlobalFilter.
     *
     * @param filters
     * @since 1.2
     */
    public void filter(List filters) {
        addGlobalFilter(filters);
    }

	/**
	 * Register a filter for all actions in this application manager.
	 *
	 * @param filter The filter to register as a global filter.
     * @param last true if you want this filter to be executed <i>after</i> the specific action filters.
     * @since 1.1.1
	 */
	public void addGlobalFilter(Filter filter, boolean last) {
        if (last) {

            globalFiltersLast.add(filter);

        } else if (filter.getClass().equals(InjectionFilter.class)
                || filter.getClass().equals(OutjectionFilter.class)) {

        		// force those filters to be the last in the chain, because if they are global that what makes sense...
        		globalFiltersLast.add(filter);

        } else {

		    globalFilters.add(filter);
        }
	}

    /**
     * Shorter version of addGlobalFilter.
     *
     * @param filter
     * @param last
     * @since 1.2
     */
    public void filter(Filter filter, boolean last) {
        addGlobalFilter(filter, last);
    }

    /**
     * Shorter version of addFlobalFilter.
     *
     * @param filter
     * @since 1.3
     */
    public void filterLast(Filter filter) {
    	addGlobalFilter(filter, true);
    }

	/**
	 * Register a list of filters for all actions in this application manager.
	 *
	 * @param filters A list of filters.
     * @param last true if you want these filters to be executed <i>after</i> the specific action filters.
     * @since 1.1.1
	 */
	public void addGlobalFilter(List filters, boolean last) {
        Iterator iter = filters.iterator();
        while(iter.hasNext()) {
            Filter f = (Filter) iter.next();
            addGlobalFilter(f, last);
        }
	}

    /**
     * Shorter version of addGlobalFilter.
     *
     * @param filters
     * @param last
     * @since 1.2
     */
    public void filter(List filters, boolean last) {
        addGlobalFilter(filters, last);
    }

    /**
     * Shorter version of addGlobalFilter.
     *
     * @param filters
     * @since 1.3
     */
    public void filterLast(List filters) {
    	addGlobalFilter(filters, true);
    }

	/**
	 * Register a consequence for all actions in this application manager.
	 * A global consequence has precedence over action consequences.
	 *
	 * @param result The result for what a global consequence will be registered
	 * @param c The consequence to register as a global consequence
	 */
	public void addGlobalConsequence(String result, Consequence c) {
		globalConsequences.put(result, c);
	}

    /**
     * Shorter version of addGlobalConsequence.
     *
     * @param result
     * @param c
     * @since 1.2
     */
    public void on(String result, Consequence c) {
        addGlobalConsequence(result, c);
    }

    /**
     * Shorter verions of addGlobalConsequence that will assume a forward.
     *
     * @param result
     * @param jsp
     * @since 1.9
     */
    public void on(String result, String jsp) {

    	addGlobalConsequence(result, new Forward(jsp));

    }

	/**
	 * Gets the global filters registered in this application manager.
	 *
     * @param last true if you want the global filters registered to be executed <i>after</i> the specific action filters.
	 * @return A java.util.List with all the filters registered in this application manager.
     * @since 1.1.1
	 */
	public List<Filter> getGlobalFilters(boolean last) {
        if (last) return globalFiltersLast;
        return globalFilters;
	}

	/**
	 * Gets all the global filters registered in this application manager.
     * Note that it will sum up in a list the filters executed <i>before</i> and <i>after</i> the specific action filters.
	 *
	 * @return A java.util.List with all the filters registered in this application manager.
	 */
	public List<Filter> getGlobalFilters() {
        List<Filter> list = new LinkedList<Filter>();
        list.addAll(getGlobalFilters(false));
        list.addAll(getGlobalFilters(true));
        return list;
	}

	/**
	 * Gets a global consequence associated with the result.
	 *
	 * @param result The result for what to get a global consequence.
	 * @return A global consequence for the result.
	 */
	public Consequence getGlobalConsequence(String result) {
		return globalConsequences.get(result);
	}

    /*
     * This is useful for filter destroying in the Controller.
     */
    Set<Filter> getAllFilters() {
        Set<Filter> filters = new HashSet<Filter>();
        filters.addAll(globalFilters);
        filters.addAll(globalFiltersLast);

        Iterator<ActionConfig> iterAc = actions.values().iterator();
		while(iterAc.hasNext()) {
            ActionConfig ac = (ActionConfig) iterAc.next();
            filters.addAll(ac.getFilters());
        }

		Iterator<Map<String, ActionConfig>> iter = innerActions.values().iterator();

        while(iter.hasNext()) {

        	Map<String, ActionConfig> map = iter.next();

            Iterator iter2 = map.values().iterator();

            while(iter2.hasNext()) {

                ActionConfig ac = (ActionConfig) iter2.next();

                filters.addAll(ac.getFilters());

            }
        }

        return filters;
    }

    /**
     * Convenient method that provides a less verbose way to create a forward.
     *
     * This is shorter than new Forward("/foo.jsp").
     *
     * @param page
     * @return a new forward consequence
     * @since 1.2
     */
    public static Consequence fwd(String page) {

        return new Forward(page);

    }

    /**
     * Convenient method that provides a less verbose way to create a redirect.
     *
     * This is shorter than new Redirect("/foo.jsp").
     *
     * @param page
     * @return a new redirect consequence
     * @since 1.2
     */
    public static Consequence redir(String page) {

        return new Redirect(page);

    }

    /**
     * Convenient method that provides a less verbose way to create a redirect.
     *
     * This is shorter than new Redirect("/foo.jsp", true).
     *
     * @param page
     * @param flag
     * @return a new redirect consequence
     * @since 1.3
     */
    public static Consequence redir(String page, boolean flag) {

        return new Redirect(page, flag);

    }

    /**
     * Convenient method that provides a less verbose way to create a redirect.
     *
     * This is shorter than new Redirect().
     *
     * @return a new redirect consequence
     * @since 1.3
     */
    public static Consequence redir() {

        return new Redirect();

    }

    /**
     * Convenient method that provides a less verbose way to create a redirect.
     *
     * This is shorter than new Redirect().
     *
     * @param flag
     * @return a new redirect consequence
     * @since 1.4
     */
    public static Consequence redir(boolean flag) {

        return new Redirect(flag);

    }

    /**
     * Convenient method that provides a less verbose way to create a chain.
     *
     * This is shorter than new Chain("/foo.jsp").
     *
     * @param ac The action config to chain
     * @return a new chain consequence
     * @since 1.2
     */
    public static Consequence chain(ActionConfig ac) {

        return new Chain(ac);

    }

    public static Consequence ajax(AjaxRenderer renderer) {

       return new AjaxConsequence(renderer);
    }

    /**
     * Convenient method that provides a less verbose way to create a chain.
     *
     * @param ac
     * @param innerAction
     * @return a new chain consequence
     * @since 1.12
     */
    public static Consequence chain(ActionConfig ac, String innerAction) {

       return new Chain(ac, innerAction);
    }

    /**
     * Convenient method for setting a chain.
     *
     * @param klass
     * @return a new chain consequence
     * @since 1.11
     */
    public static Consequence chain(Class<? extends Object> klass) {

       return new Chain(new ActionConfig(klass));

    }

    /**
     * Convenient method for setting a chain.
     *
     * @param klass
     * @param innerAction
     * @return a new chain consequence
     * @since 1.11
     */
    public static Consequence chain(Class<? extends Object> klass, String innerAction) {

       return new Chain(new ActionConfig(klass, innerAction));
    }


    /**
     * Convenient method that provides a less verbose way to create a ClassActionConfig.
     *
     * Note: This will also add the action to this ApplicationManager, in other words,
     * no need to call add or addActionConfig !!!
     *
     * @param klass
     * @return a new ActionConfig
     * @since 1.3
     */
    public ActionConfig action(Class<? extends Object> klass) {

        return addActionConfig(new ActionConfig(klass));
    }

    /**
     * Convenient method that provides a less verbose way to create an action config.
     *
     * Note: This will also add the action to this ApplicationManager, in other words,
     * no need to call add or addActionConfig !!!
     *
     * @param name
     * @param klass
     * @return a new action config
     * @since 1.2
     */
    public ActionConfig action(String name, Class<? extends Object> klass) {

        return addActionConfig(new ActionConfig(name, klass));

    }

    /**
     * Convenient method that provides a less verbose way to create an action config.
     *
     * Note: This will also add the action to this ApplicationManager, in other words,
     * no need to call add or addActionConfig !!!
     *
     * @param name
     * @param klass
     * @param innerAction
     * @return a new action config
     * @since 1.2
     */
    public ActionConfig action(String name, Class<? extends Object> klass, String innerAction) {

        return addActionConfig(new ActionConfig(name, klass, innerAction));

    }

    /**
     * Convenient method that provides a less verbose way to create a ClassActionConfig.
     *
     * Note: This will also add the action to this ApplicationManager, in other words,
     * no need to call add or addActionConfig !!!
     *
     * @param klass
     * @param innerAction
     * @return a new ClassActionConfig
     * @since 1.3
     */
    public ActionConfig action(Class<? extends Object> klass, String innerAction) {

        return addActionConfig(new ActionConfig(klass, innerAction));

    }

    /**
     * Turn on/off the statsMode mode here.
     *
     * @param statsMode
     */
    public void setStatsMode(boolean statsMode) {

        Controller.statsMode = statsMode;

    }


    /**
     * Turn on/off the reload mode of application manager.
     * This can also be done in the web.xml file.
     *
     * @param reloadMode
     */
    public void setReloadMode(boolean reloadMode) {

        Controller.reloadAppManager = reloadMode;

    }

    /**
     * Turn on/off auto view discovery. Default is on!
     * This can also be done in the web.xml file.
     *
     * Auto view gives the controller the ability to generate
     * forward consequences automatically for the results it cannot
     * find a consequence, so that you don't need to define consequences
     * for your actions in the configuration.
     *
     * @param autoView
     */
    public void setAutoView(boolean autoView) {

        Controller.autoView = autoView;

    }

	/**
     * This method override the ApplicationManager attributes.
	 * @param The parent ApplicationManager
	 * @return
	 */
	public AbstractApplicationManager setParent(AbstractApplicationManager parent) {
    	this.actions = parent.actions;
    	this.globalConsequences = parent.globalConsequences;
    	this.globalFilters = parent.globalFilters;
    	this.globalFiltersLast = parent.globalFiltersLast;
    	this.innerActions = parent.innerActions;

    	return this;
	}

   /**
    * Sets the consequence provider that will be used by the controller.
    *
    * @param consequenceProvider
    * @since 1.11
    */
   public void setConsequenceProvider(ConsequenceProvider consequenceProvider) {

      Controller.setConsequenceProvider(consequenceProvider);
   }

   /**
    * Adds the list to this ListManager, so there is no need to use ListManager.addList
    *
    * @param listData
    * @since 1.11
    */
   public void addList(ListData listData) {

      ListManager.addList(listData);
   }
}