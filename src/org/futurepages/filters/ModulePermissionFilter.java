package org.futurepages.filters;

import org.futurepages.actions.AjaxAction;
import org.futurepages.core.action.AbstractAction;
import org.futurepages.core.action.Action;
import org.futurepages.core.admin.AllModulesFree;
import org.futurepages.core.admin.AuthenticationFree;
import org.futurepages.core.control.AbstractModuleManager;
import org.futurepages.core.control.InvocationChain;
import org.futurepages.core.filter.Filter;

/**
 * Use este filtro de forma global. Ele verificar� em todas actions se trata-se
 * de uma ProtectedAction, se for, verificar� em qual pacote se encontra, da�
 * verificar� se o usu�rio logado possui o m�dulo da Action, s� deixar� executar
 * qualquer coisa da action se ele possuir o m�dulo ou for de um tipo de usu�rio
 * do mesmo m�dulo.
 * 
 * @author leandro
 */
public class ModulePermissionFilter implements Filter {

    public ModulePermissionFilter() {
    }

	@Override
    public String filter(InvocationChain chain) throws Exception {

		if (chain.getAction() instanceof AbstractAction) {

            AbstractAction action = (AbstractAction) chain.getAction();

            boolean shouldByPass = false;
            if(action instanceof AuthenticationFree){
                AuthenticationFree ac = (AuthenticationFree) action;
                shouldByPass = ac.bypassAuthentication(chain.getInnerAction());
            }
			if(!shouldByPass){
				if(!(action instanceof AllModulesFree)) {
					String moduleId = AbstractModuleManager.moduleId(action.getClass());
					if (!action.loggedUser().hasModule(moduleId)) {
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

	@Override
    public void destroy() {
    }
}