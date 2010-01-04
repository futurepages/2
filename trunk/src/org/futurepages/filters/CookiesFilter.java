package org.futurepages.filters;

import org.futurepages.core.action.Action;
import org.futurepages.core.context.Context;
import org.futurepages.core.filter.Filter;
import org.futurepages.core.input.InputWrapper;
import org.futurepages.core.control.InvocationChain;

/**
 * This filter will place the cookie context in the action input.
 * 
 * Note that the CookieContext class is also a Map<String, Object> so
 * it can be easily injected in a pojo action without coupling with the 
 * framework API.
 * 
 * @author Sergio Oliveira Jr.
 */
public class CookiesFilter extends InputWrapper implements Filter {
   
   private final String name;
   
   private ThreadLocal<Action> action = new ThreadLocal<Action>();
   
   public CookiesFilter(String name) {
      
      this.name = name;
      
   }
   
   public String filter(InvocationChain chain) throws Exception {
	   
	   Action action = chain.getAction();
	
	   super.setInput(action.getInput());
	
	   action.setInput(this);
	   
	   this.action.set(action);
	   
	   return chain.invoke();
   }
   
	@Override
	public Object getValue(String name) {
		
		if (name.equals(this.name)) {
			
			Object value = super.getValue(name);
			
			if (value != null) return value;
			
			Action action = this.action.get();
			
			if (action == null) throw new IllegalStateException("Action cannot be null here!");
			
			Context cookies = action.getCookies();
			
			setValue(name, cookies);
			
			return cookies;
			
		} else {
			
			return super.getValue(name);
		}
	}
   
   public void destroy() { 
      
   }
   
}