package org.futurepages.actions;

/**
 * Action que necessita de autentica��o para ser acessada.
 * @author leandro
 */
public abstract class ProtectedCrudActions extends CrudActions {


    @Override
    public boolean bypassAuthentication(String inner){
        return false;
    }

}