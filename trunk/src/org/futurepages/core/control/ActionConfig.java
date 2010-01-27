package org.futurepages.core.control;

import org.futurepages.core.consequence.Forward;
import org.futurepages.core.consequence.Consequence;
import org.futurepages.core.filter.Filter;
import org.futurepages.core.action.Action;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.futurepages.consequences.AjaxConsequence;
import org.futurepages.core.ajax.AjaxRenderer;

/**
 * An ActionConfig links together an action implementation, an action name or alias, action results and action consequences.
 * It makes it possible for an action implementation to be re-used in different situations with different names and consequences.
 * 
 * @author Sergio Oliveira
 */
public class ActionConfig {
	
	protected Class<? extends Object> actionClass;
	private String name = null;
	private Map<String, Consequence> consequences = new HashMap<String, Consequence>();
    private Map<String, Map<String, Consequence>> innerConsequences = new HashMap<String, Map<String, Consequence>>();
    private List<Object[]> filters = new LinkedList<Object[]>();
    private List<Object[]> firstFilters = new LinkedList<Object[]>();
    private String innerAction = null;
    
	/**
	 * Creates an ActionConfig for the given action implementation.
     * This action config will use the name of the action to derive its name.
     * This can and should also be used with chain consequence!
     * 
     * org.myapp.blablabla.MyAction
     * 
     * The name will be:
     * 
     * /MyAction
     * 
	 * @param klass The action implementation to use
	 */
	public ActionConfig(Class<? extends Object> klass) {
		
		this.actionClass = klass;
        this.name = getName(klass);
	}
    

	/**
	 * Creates an ActionConfig with the given name for the given action implementation.
	 * 
	 * @param name The name or alias of this ActionConfig
	 * @param klass The action implementation to use
	 */
	public ActionConfig(String name, Class<? extends Object> klass) {
      
      if (name.indexOf(".") > 0) {
         
         // use is probably defining action and inner action together...
         
         StringTokenizer st = new StringTokenizer(name, ".");
         
         if (st.countTokens() != 2) {
            
            throw new IllegalArgumentException("Bad action name: " + name);
         }
         
         this.name = cutSlash(st.nextToken());
         this.innerAction = st.nextToken();
         
      } else {
         
         this.name = cutSlash(name);
      }
		
		this.actionClass = klass;
      
	}
    
	/**
	 * Creates an ActionConfig for the given action implementation.
     * This action config will use the name of the action to derive its name.
     * This can and should also be used with chain consequence!
	 *
     * Notice that this action config is specific to an inner action.
     * 
     * org.myapp.blablabla.MyAction
     * 
     * The name will be:
     * 
     * /MyAction
	 * 
	 * @param name The name or alias of this ActionConfig
	 * @param klass The action implementation to use
     * @param innerAction The inner action to use
	 */
	public ActionConfig(String name, Class<? extends Object> klass, String innerAction) {
		
		this.actionClass = klass;
		this.name = cutSlash(name);
        this.innerAction = innerAction;
	}
	
	/**
	 * Creates an ActionConfig with the given name for the given inner action implementation.
     * Notice that this action config is specific to an inner action.
	 * 
	 * @param klass The action implementation to use
     * @param innerAction The inner action to use
	 */
	public ActionConfig(Class<? extends Object> klass, String innerAction) {
		
		this.actionClass = klass;
		this.name = getName(klass);
        this.innerAction = innerAction;
	}
	
	/**
	 * Adds a consequence for the given result.
	 * An action must have a consequence for each of its possible results.
	 * 
	 * @param result A possible result of this ActionConfig
	 * @param c The consequence for this result
     * @return this action config for method chaining. Ex: addConsequence().addConsequence();
	 */
	public ActionConfig addConsequence(String result, Consequence c) {
		consequences.put(result, c);
        return this;
	}
    
    /**
     * Shorter version of addConsequence.
     * 
     * @param result
     * @param c
     * @return this action config
     * @since 1.2
     */
    public ActionConfig on(String result, Consequence c) {
       return addConsequence(result, c); 
    }
    
    /**
     * Shorter verions of addConsequence that will assume a forward.
     * 
     * @param result
     * @param jsp
     * @return  this action config
     * @since 1.9
     */
    public ActionConfig on(String result, String jsp) {
    	
    	return addConsequence(result, new Forward(jsp));
    }
    
    private String cutSlash(String name) {
        if (name.startsWith("/") && name.length() > 1) {
            return name.substring(1, name.length());
        }
        return name;
    }
    
	/**
	 * Adds a consequence for the given result of the given inner action.
	 * An inner action can have a consequence for each of its possible results.
     * If you don't define consequences for an inner action, 
     * the consequences of the main action (execute() method) is used instead.
	 * 
	 * @param result A possible result of this ActionConfig
     * @param innerAction The inner action that can return this result.
	 * @param c The consequence for this result
     * @return this action config for method chaining Ex: addConsequence().addConsequence();
     * @throws IllegalStateException If this method is called for a action config specific to an inner action
	 */
	public ActionConfig addConsequence(String result, String innerAction, Consequence c) {
        if (this.innerAction != null) throw new IllegalStateException("Calling addConsequence(result,innerAction,c) is illegal for inner action configs!");
        Map<String, Consequence> map = innerConsequences.get(innerAction);
        if (map == null) {
            map = new HashMap<String, Consequence>();
            innerConsequences.put(innerAction, map);
        }
        map.put(result, c);
        return this;
	}
    
    /**
     * Shorter version of addConsequence.
     * @param result
     * @param innerAction
     * @param c
     * @return this action config
     * @since 1.2
     */
    public ActionConfig on(String result, String innerAction, Consequence c) {
        return addConsequence(result, innerAction, c);
    }
    
    /**
     * Adds a filter for the action.
     *
     * @param filter The filter to add for this action.
     * @return this action config for method chaining Ex: addConsequence().addFilter();
     */
    public ActionConfig addFilter(Filter filter) {
        return addFilter(filter, (String) null);
    }
    
    /**
     * Shorter version of addFilter.
     * 
     * @param filter
     * @return this action config
     * @since 1.2
     */
    public ActionConfig filter(Filter filter) {
        return addFilter(filter);
    }
    
    /**
     * Adds a filter for this inner action.
     *
     * @param filter The filter to add for this inner action.
     * @param innerAction the inner action
     * @return this action config for method chaining Ex: addConsequence().addFilter();
     * @since 1.1.1
     */
    public ActionConfig addFilter(Filter filter, String innerAction) {
        Object [] array = new Object[2];
        array[0] = innerAction;
        array[1] = filter;
        filters.add(array);
        return this;
    }
    
    /**
     * Add a list of filters that will be executed before the global filters.
     * 
     * @param filters
     * @return this
     * @since 1.9
     */
    public ActionConfig filterFirst(List filters) {
    	
    	return addFilterFirst(filters);
    }
    
    /**
     * Add a list of filters that will be executed before the global filters.
     * 
     * @param filters
     * @param innerAction
     * @return this
     * @since 1.9
     */
    public ActionConfig filterFirst(List filters, String innerAction) {
    	
    	return addFilterFirst(filters, innerAction);
    	
    }
    
    /**
     * Add a list of filters that will be executed before the global filters.
     * 
     * @param filter
     * @return this
     * @since 1.9
     */
    public ActionConfig filterFirst(Filter filter) {
    	
    	return addFilterFirst(filter);
    }
    
    /**
     * Add a list of filters that will be executed before the global filters.
     * 
     * @param filter
     * @param innerAction
     * @return this
     * @since 1.9
     */
    public ActionConfig filterFirst(Filter filter, String innerAction) {
    	
    	return addFilterFirst(filter, innerAction);
    }
    
    /**
     * Add a list of filters that will be executed before the global filters.
     * 
     * @param filters
     * @return this
     * @since 1.9
     */
    public ActionConfig addFilterFirst(List filters) {
    	
    	return addFilterFirst(filters, null);
    }
    
    /**
     * Adds a list of filters that will be executed before the global filters.
     * 
     * @param filters
     * @param innerAction
     * @return this
     * @since 1.9
     */
    public ActionConfig addFilterFirst(List filters, String innerAction) {
        Iterator iter = filters.iterator();
        while(iter.hasNext()) {
            Filter f = (Filter) iter.next();
            addFilterFirst(f, innerAction);
        }
        return this;
    }
    
    /**
     * Adds a filter that will be executed before the global filters.
     * 
     * @param filter
     * @return this
     * @since 1.9
     */
    public ActionConfig addFilterFirst(Filter filter) {
    	
    	return addFilterFirst(filter, null);
    }
    
    /**
     * Adds a filter that will be executed before the global filters.
     * 
     * @param filter
     * @param innerAction
     * @return this
     * @since 1.9
     */
    public ActionConfig addFilterFirst(Filter filter, String innerAction) {
    	Object [] array = new Object[2];
    	array[0] = innerAction;
    	array[1] = filter;
    	
    	firstFilters.add(array);
    	
    	return this;
    }
    
    private static String getName(Class klass) {
        
        String[] tokens = klass.getName().split("\\.");
        
        return tokens[tokens.length - 1];
        
    }
    
    /**
     * Shorter version of addFilter.
     * 
     * @param filter
     * @param innerAction
     * @return this action config
     * @since 1.2
     */
    public ActionConfig filter(Filter filter, String innerAction) {
        return addFilter(filter, innerAction);
    }
    
    /**
     * Adds a list of filter for the action.
     *
     * @param filters A list of filters.
     * @return this action config for method chaining Ex: addConsequence().addFilter();
     */
    public ActionConfig addFilter(List filters) {
        return addFilter(filters, null);
    }
    
    /**
     * Shorter version of addFilter.
     * 
     * @param filters
     * @return this action config
     * @since 1.2
     */
    public ActionConfig filter(List filters) {
        return addFilter(filters);
    }
    
    /**
     * Shorter version of a AjaxConsequence success.
     * 
     * @param renderer
     * @return this
     * @since 1.10.1
     */
    public ActionConfig ajaxSuccess(AjaxRenderer renderer){
    
    	return addConsequence(Action.SUCCESS, new AjaxConsequence(AjaxConsequence.KEY , renderer));
    }

    /**
     * Shorter version of a AjaxConsequence error.
     * 
     * @param renderer
     * @return this
     * @since 1.10.1
     */
    public ActionConfig ajaxError(AjaxRenderer renderer){
    
    	return addConsequence(Action.ERROR, new AjaxConsequence(AjaxConsequence.KEY, renderer));
    }    
    
 
 
    
    /**
     * Adds a list of filter for the inner action.
     *
     * @param filters A list of filters
     * @param innerAction the inner action
     * @return this action config for method chaining Ex: addConsequence().addFilter();
     * @since 1.1.1
     */
    public ActionConfig addFilter(List filters, String innerAction) {
        Iterator iter = filters.iterator();
        while(iter.hasNext()) {
            Filter f = (Filter) iter.next();
            addFilter(f, innerAction);
        }
        return this;
    }

    /**
     * Adds a filter to a list of inner actions.
     * 
     * @param filter The filter to add
     * @param args The list of inner actions
     * @return this
     * @since 1.11
     */
    public ActionConfig addFilter(Filter filter, String ... args) {
    	
    	if (args != null) {
    		
    		for(int i=0;i<args.length;i++) {
    			
    			addFilter(filter, args[i]);
    		}
    	}
    	
    	return this;
    }
    
    /**
     * Shorter version.
     * 
     * @param filter
     * @param args
     * @return this action config
     * @since 1.11
     */
    public ActionConfig filter(Filter filter, String ... innerActions) {
    	
    	return addFilter(filter, innerActions);
    }
    
    /**
     * Shorter version of addFilter.
     * 
     * @param filters
     * @param innerAction
     * @return this action config
     * @since 1.2
     */
    public ActionConfig filter(List filters, String innerAction) {
        return addFilter(filters, innerAction);
    }
    
    /**
     * Returns the filters for this action.
     *
     * @return The filters for this action.
     */
    public List<Filter> getFilters() {
        List<Filter> list = new ArrayList<Filter>(filters.size() + firstFilters.size());
        
        Iterator<Object[]> iter = firstFilters.iterator();
        while(iter.hasNext()) {
            Object [] array = iter.next();
            list.add((Filter) array[1]);
        }
        
        iter = filters.iterator();
        while(iter.hasNext()) {
            Object [] array = iter.next();
            list.add((Filter) array[1]);
        }
        return list;
    }
    
    /**
     * Returns the filters for this inner action.
     *
     * @param innerAction the inner action.
     * @return The filters for this action.
     * @since 1.1.1
     */
    public List<Filter> getFilters(String innerAction) {
        List<Filter> list = new ArrayList<Filter>(filters.size());
        Iterator<Object[]> iter = filters.iterator();
        while(iter.hasNext()) {
            Object [] array = iter.next();
            if (array[0] == null || array[0].equals(innerAction)) {
                list.add((Filter) array[1]);
            }
        }
        return list;
    }    
    
    /**
     * Returns the filters for this inner action, that will be executed before the global filters.
     *
     * @param innerAction the inner action.
     * @return The filters for this action.
     * @since 1.9
     */
    public List<Filter> getFirstFilters(String innerAction) {
        List<Filter> list = new ArrayList<Filter>(firstFilters.size());
        Iterator<Object[]> iter = firstFilters.iterator();
        while(iter.hasNext()) {
            Object [] array = iter.next();
            if (array[0] == null || array[0].equals(innerAction)) {
                list.add((Filter) array[1]);
            }
        }
        return list;
    }    
	
	/**
	 * Gets the name or alias of this ActionConfig.
	 * 
	 * @return The name or alias of this ActionConfig.
	 */
	public String getName() { return name; }
	
    /**
     * Gets the inner action that this action config represents.
     *
     * @return The inner action name that his action represents.
     */
    public String getInnerAction() {
        return innerAction;
    }
    
    void setInnerAction(String innerAction) {
        this.innerAction = innerAction;
    }
	
	/**
	 * Gets the consequence for the given result.
	 * 
	 * @param result The result for what to get the consequence
	 * @return The consequence associated with the result.
	 */
	public Consequence getConsequence(String result) {
		return consequences.get(result);
	}
    
	/**
	 * Gets the consequence for the given result of the given inner action.
	 * 
	 * @param result The result for what to get the consequence
     * @param innerAction The innerAction from where to get the consequence.
	 * @return The consequence associated with the result and the inner action.
	 */
	public Consequence getConsequence(String result, String innerAction) {
        Map map = innerConsequences.get(innerAction);
        if (map != null) {
            return (Consequence) map.get(result);
        }
		return null;
	}

	/**
     * Returns an action instance to be used with this request.
     * Mentawai creates a new action instance for each request.
     * You can extend ActionConfig and override this class to integrate Mentawai 
     * with other IoC containers, that may want to create the action themselves.
     * 
     * @return The action instance to use for the request.
     */
    public Action getAction() {
		try {
			return (Action) actionClass.newInstance();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
    }
    
    /**
     * Returns the action class for this action config.
     * 
     * @return The action class for this action config.
     * @since 1.2.1
     */
    public Class<? extends Object> getActionClass() {
    	
    	// must be action here, because constructor is checking...
    	
        return actionClass;
    }
    
    /**
     * Returns the name of this ActionConfig.
     * Ex: /HelloWorld, /customers/add, etc.
     *
     * @return The name of this action.
     */
	@Override
    public String toString() {
        return name;
    }
}