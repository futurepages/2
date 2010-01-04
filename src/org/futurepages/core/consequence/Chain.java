package org.futurepages.core.consequence;

import org.futurepages.core.filter.AfterConsequenceFilter;
import org.futurepages.core.filter.Filter;
import org.futurepages.exceptions.ConsequenceException;
import org.futurepages.core.action.Action;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.futurepages.core.control.ActionConfig;
import org.futurepages.core.control.Controller;
import org.futurepages.core.input.Input;
import org.futurepages.core.input.RequestInput;

/**
 * An action chaining consequence.
 * 
 * @author Sergio Oliveira
 */
public class Chain implements Consequence {
	
	private ActionConfig ac;
   
   private String innerAction = null;
	
	/**
	 * Creates a chain consequence for the given ActionConfig.
	 * 
	 * @param ac The ActionConfig to chain.
	 */
	public Chain(ActionConfig ac) {
		this.ac = ac;
	}
   
   /**
    * Creates a chain consequence for the given ActionConfig
    * @param ac
    * @param innerAction
    * @since 1.12
    */
   public Chain(ActionConfig ac, String innerAction) {
      
      this(ac);
      
      this.innerAction = innerAction;
   }
    
    public void execute(Action a, HttpServletRequest req, HttpServletResponse res) throws ConsequenceException {
		Action action = ac.getAction();
        if (action == null) {
            throw new ConsequenceException("Could not load action for chain: " + ac);
        }
        
        /*
         * Because of the new InputWrapper filters, do not re-use the input but copy its values !
         */
        
        Input input = new RequestInput(req);
        
        Input old = a.getInput();
        
        Iterator<String> iterOld = old.keys();
        
        while(iterOld.hasNext()) {
            
            String key = (String) iterOld.next();
            
            input.setValue(key, old.getValue(key));
            
        }
        
        action.setInput(input);
        action.setOutput(a.getOutput());
        action.setSession(a.getSession());
        action.setApplication(a.getApplication());
        action.setLocale(a.getLocale());
        action.setCookies(a.getCookies());
        
        Consequence c = null;
        
        List<Object> filters = new LinkedList<Object>();
        
        boolean conseqExecuted = false;
        
        boolean actionExecuted = false;
        
        StringBuilder returnedResult = new StringBuilder(32);
        
        try {
           
           String innerAction;
           
           if (this.innerAction != null) {
              
              innerAction = this.innerAction;
              
           } else {
              
              innerAction = ac.getInnerAction();
           }
        	
            c = Controller.invokeAction(ac, action, innerAction, filters, returnedResult);
            
            actionExecuted = true;
        
            c.execute(action, req, res);
            
            conseqExecuted = true;
            
        } catch(ConsequenceException e) {
            throw e;
        } catch(Exception e) {
            throw new ConsequenceException(e);
        } finally {
            
            Iterator<Object> iter = filters.iterator();
            
            while(iter.hasNext()) {
                
                Filter f = (Filter) iter.next();
                
                if (f instanceof AfterConsequenceFilter) {
                    
                    AfterConsequenceFilter acf = (AfterConsequenceFilter) f;
                    
                    try {
                    	
                    	String s = returnedResult.toString();
                        
                        acf.afterConsequence(action, c, conseqExecuted, actionExecuted, s.length() > 0 ? s : null);
                        
                    } catch(Exception e) {
                        
                        e.printStackTrace();
                    }
                    
                }
                
            }
        }
    }
    
	@Override
    public String toString() {
        
        StringBuffer sb = new StringBuffer(128);
        
        sb.append("Chain to ").append(ac);
        
        if (innerAction != null) sb.append(" (innerAction = ").append(innerAction).append(")");
        
        return sb.toString();
    }
}

	
	