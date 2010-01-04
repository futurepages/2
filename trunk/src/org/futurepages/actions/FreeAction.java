package org.futurepages.actions;

import org.futurepages.core.action.AbstractAction;
import org.futurepages.core.admin.AuthenticationFree;

/**
 * Action que n�o requer autentica��o por parte do usu�rio.
 */
public abstract class FreeAction extends AbstractAction implements AuthenticationFree{

    public boolean bypassAuthentication(String innerAction) {
        return true;
    }
}