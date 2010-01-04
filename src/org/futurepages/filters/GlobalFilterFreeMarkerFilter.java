package org.futurepages.filters;

import org.futurepages.core.filter.Filter;
import org.futurepages.core.control.InvocationChain;

public class GlobalFilterFreeMarkerFilter implements Filter {
   
   private final String[] innerActions;
   
   public GlobalFilterFreeMarkerFilter() {
      
      this.innerActions = null;
      
   }
   
   public GlobalFilterFreeMarkerFilter(String ... innerActions) {
      
      this.innerActions = innerActions;
   }
   
   public boolean isGlobalFilterFree(String innerAction) {
      
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