package org.futurepages.core.action;

/**
 * An interface describing a sticky action, in other words,
 * an action that can adhere to the session and persist its state.
 * 
 * This is similar to what other frameworks call <i>continuations</i>, but
 * much simpler.
 * 
 * @author Sergio Oliveira
 */
public interface StickyAction extends Action {
    
	/**
	 * Adhere to the session, so the instance of this action will persist
	 * until disjoin is called.
	 */
    public void adhere();
    
    /**
     * Remove this action from session and discard its instance losing
     * all state (instance variables) associated with it.
     */
    public void disjoin();

    /**
     * This method will be called if the session has expired or if it has
     * been invalidated and there are sticky actions still sticked to the session.
     * 
     * This is very useful for clean up and will be called if your action calls
     * adhere and <i>forget</i> to call disjoin, leaving the actiion instance
     * forever in the session.
     */
    public void onRemoved();
    
}