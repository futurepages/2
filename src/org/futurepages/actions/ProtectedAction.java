package org.futurepages.actions;

import org.futurepages.core.action.AbstractAction;
import org.futurepages.core.admin.RedirectAfterLogin;

/**
 * Action que necessita de autentica��o para ser acessada.
 *
 * Quando o RedirectFilter est� ligado, por padr�o o m�todo execute redireciona
 * e as innerActions n�o. Isso at� que se sobrescreva o m�todo shouldRedirect
 * dizendo quais inner ser�o redirecionadas.
 * 
 * @author leandro
 */
public abstract class ProtectedAction extends AbstractAction implements RedirectAfterLogin{

    public boolean shouldRedirect(String inner) {
        if(inner == null || inner.equals("execute")){
            return true;
        }
        return false;
    }
}
