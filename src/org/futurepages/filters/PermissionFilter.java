package org.futurepages.filters;

import org.futurepages.core.action.AbstractAction;
import org.futurepages.actions.AjaxAction;
import org.futurepages.core.action.Action;
import org.futurepages.core.filter.Filter;
import org.futurepages.core.control.InvocationChain;
import org.futurepages.core.admin.AuthenticationFree;

/**
 * Usado individualmente por action, para proteger uma action
 * de acordo com a role e/ou o tipo do usuário logado.
 * 
 * Verifica se o suário logado é do tipo e/ou se tem o role passados como parâmetros.
 * 
 * @author leandro
 */
public class PermissionFilter implements Filter {

    private String[] roles;
    private Class userType;
    /*TODO VERIFICAR ISSO*/

    public PermissionFilter() {
    }

    public PermissionFilter(Class userType, String... roles) {
        this.roles = roles;
        this.userType = userType;
    }

    public PermissionFilter(Class userType) {
        this.userType = userType;
    }

    public PermissionFilter(String... roles) {
        this.roles = roles;
    }

    public String filter(InvocationChain chain) throws Exception {

        AbstractAction action = (AbstractAction) chain.getAction();

        if (action.loggedUser() != null) {

            if(AuthenticationFree.class.isAssignableFrom(action.getClass())){
                if(((AuthenticationFree)action).bypassAuthentication(chain.getInnerAction())){
                    return chain.invoke();
                }
            }

            if (userType != null) {
                if (!userType.isAssignableFrom(action.loggedUser().getClass()) ) {
                    return denied(action);
                }
            }

            if (roles != null) {
                for (String roleId : roles) {
                    if (!action.loggedUser().hasRole(roleId)) {
                        return denied(action);
                    }
                }
            }

        }

        return chain.invoke();
    }

    private String denied(Action action) {
        if (action instanceof AjaxAction) {
            return AJAX_DENIED;
        }

        return ACCESS_DENIED;
    }

    public void destroy() {
    }
}