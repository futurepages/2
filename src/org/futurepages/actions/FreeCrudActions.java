package org.futurepages.actions;

/**
 * Actions com suporte a CRUD que n�o requer autentica��o por parte do usu�rio.
 */
public abstract class FreeCrudActions extends CrudActions {

    @Override
    public boolean bypassAuthentication(String innerAction) {
        return true;
    }
}