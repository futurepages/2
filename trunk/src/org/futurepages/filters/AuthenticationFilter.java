package org.futurepages.filters;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.futurepages.actions.DynAction;
import org.futurepages.actions.AjaxAction;
import org.futurepages.actions.LoginAction;
import org.futurepages.core.action.Action;
import org.futurepages.core.context.Context;
import org.futurepages.core.filter.Filter;
import org.futurepages.core.control.InvocationChain;
import org.futurepages.core.context.SessionContext;
import org.futurepages.core.admin.AuthenticationFree;
import org.futurepages.core.admin.RedirectAfterLogin;

/**
 * A filter to handle user authentcation.
 * You should use this filter to protect your actions from unauthorized access.
 * 
 * @author Sergio Oliveira
 */
public class AuthenticationFilter implements Filter {

    public static final String URL_KEY = "url";

    /**
     * Creates a new authentication filter.
     */
    public AuthenticationFilter() {
    }

    public String filter(InvocationChain chain) throws Exception {

        Action action = chain.getAction();

        String innerAction = chain.getInnerAction();

        Context session = action.getSession();

        boolean shouldBypass = false;


        if (action instanceof AuthenticationFree) {

            if(!(action instanceof LoginAction)){
                session.setAttribute(AuthenticationFilter.URL_KEY, null);
            }

            AuthenticationFree af = (AuthenticationFree) action;

            shouldBypass = af.bypassAuthentication(innerAction);

        }

        if (!shouldBypass) {

            Filter f = chain.getFilter(AuthenticationFreeMarkerFilter.class);

            if (f != null) {

                AuthenticationFreeMarkerFilter aff = (AuthenticationFreeMarkerFilter) f;

                shouldBypass = aff.bypassAuthentication(innerAction);

            }
        }

        if (!shouldBypass) {

            if (!LoginAction.isLogged(session)) {

                boolean shouldRedirect = false;

                if ((action instanceof RedirectAfterLogin) && !(action instanceof DynAction)) {

                    RedirectAfterLogin ral = (RedirectAfterLogin) action;

                    shouldRedirect = ral.shouldRedirect(innerAction);
                }

                //maker ??
                if (!shouldRedirect) {

                    Filter f = chain.getFilter(ShouldRedirectFilter.class);

                    if (f != null) {

                        ShouldRedirectFilter ramf = (ShouldRedirectFilter) f;

                        shouldRedirect = ramf.shouldRedirect(innerAction);
                    }
                }


                if (shouldRedirect) {

                    HttpServletRequest req = ((SessionContext) session).getRequest();
                    HttpSession ses = ((SessionContext) session).getSession();
                    setCallbackUrl(ses, req);

                }

                if (action instanceof AjaxAction) {

                    return AJAX_DENIED;
                }

                if (action instanceof DynAction) {

                    return DYN_LOGIN;
                }

                return LOGIN;
            }
        }
        return chain.invoke();
    }

    /**
     * Sets a callback url for a redirection after the login.
     * This method is called by the authentication tag to set a url for the redirection.
     * You should not call this method.
     *
     * @param session The HttpSession where to put the URL.
     * @param req The HttpServletRequest from where to get the URL.
     */
    public static void setCallbackUrl(HttpSession session, HttpServletRequest req) {
        StringBuffer url = req.getRequestURL();
        String query = req.getQueryString();
        if (query != null) {
            url.append('?');
            url.append(query);
        }
        session.setAttribute(URL_KEY, url.toString());
    }

    public void destroy() {
    }
}
		