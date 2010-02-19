package org.futurepages.filters;

import org.futurepages.core.action.AbstractAction;
import org.futurepages.actions.AjaxAction;
import org.futurepages.core.action.Action;
import org.futurepages.core.filter.Filter;
import org.futurepages.core.control.InvocationChain;
import org.futurepages.core.admin.AuthenticationFree;
import org.futurepages.core.admin.DefaultRole;
import org.futurepages.core.admin.DefaultUser;

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
    private Class actionType;

    public PermissionFilter() {
    }

    public <T extends DefaultUser> PermissionFilter(Class<T> userType, String... roles) {
        this.roles = roles;
        this.userType = userType;
    }

    public <T extends DefaultUser> PermissionFilter(Class<T> userType) {
        this.userType = userType;
    }

    public <T extends DefaultUser> PermissionFilter(Class<T> userType, Class actionType) {
        this.userType = userType;
        this.actionType = actionType;
    }

    public PermissionFilter(String... roles) {
        this.roles = roles;
    }

	public PermissionFilter(DefaultRole... roles) {
		this.roles = new String[roles.length];
        for(int i = 0 ; i < roles.length  ; i++){
			this.roles[i] = roles[i].getRoleId();
		}
    }

    public String filter(InvocationChain chain) throws Exception {

        AbstractAction action = (AbstractAction) chain.getAction();

        if (action.loggedUser() != null) {

            if (AuthenticationFree.class.isAssignableFrom(action.getClass())) {
                if (((AuthenticationFree) action).bypassAuthentication(chain.getInnerAction())) {
                    return chain.invoke();
                }
            }

            if (actionType == null && userType != null) {
                if (!userType.isAssignableFrom(action.loggedUser().getClass())) {
                    return denied(action);
                }
            } else if (actionType != null) {
                if (actionType.isAssignableFrom(action.getClass())
                        && !userType.isAssignableFrom(action.loggedUser().getClass())) {
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