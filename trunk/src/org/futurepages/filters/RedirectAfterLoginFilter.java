package org.futurepages.filters;

import org.futurepages.core.action.Action;
import org.futurepages.core.action.AbstractAction;
import org.futurepages.core.context.Context;
import org.futurepages.core.filter.Filter;
import org.futurepages.core.control.InvocationChain;
import org.futurepages.core.output.Output;
import org.futurepages.core.consequence.Redirect;

/**
 * A filter that implements the redirect after login mechanism.
 * Apply this filter to your Login action if you want it to perform a redict 
 * to the first page the user tried to access.
 *
 * @author Sergio Oliveira
 */
public class RedirectAfterLoginFilter implements Filter {
	
    /**
     * Creates a RedirectAfterLoginFilter.
     */
	public RedirectAfterLoginFilter() { }
	
	public String filter(InvocationChain chain) throws Exception {
		Action action = chain.getAction();
		Context session = action.getSession();
		String callback = (String) session.getAttribute(AuthenticationFilter.URL_KEY);
		String result = chain.invoke();
		if (callback != null && !result.equals(AbstractAction.ERROR)) {
			Output output = action.getOutput();
			output.setValue(Redirect.REDIRURL_PARAM, callback);
			return REDIR;
		}
		return result;
	}
    
    public void destroy() { }
}
		