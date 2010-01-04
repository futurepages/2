package org.futurepages.filters;

import org.futurepages.core.filter.Filter;
import org.futurepages.core.control.InvocationChain;

public class RedirectAfterLoginMarkerFilter implements Filter {
   
   private final String[] innerActions;
   
   public RedirectAfterLoginMarkerFilter() {
      
      this.innerActions = null;
      
   }
   
   public RedirectAfterLoginMarkerFilter(String ... innerActions) {
      
      this.innerActions = innerActions;
   }
   
   public boolean shouldRedirect(String innerAction) {
      
      if (innerActions == null) return true;
      
      if (innerAction == null) return false; // inner actions are specified...
      
      for(String s : innerActions) {
         
         if (s.equals(innerAction)) return true;
      }
      
      return false;
      
   }
   
   public String filter(InvocationChain chain) throws Exception {
      
      return chain.invoke();
   }
   
   public void destroy() { 
      
   }
   
}