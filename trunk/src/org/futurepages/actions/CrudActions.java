package org.futurepages.actions;

import org.futurepages.core.action.AbstractAction;
import org.futurepages.core.admin.AuthenticationFree;
import org.futurepages.core.admin.RedirectAfterLogin;

/**
 * Action especializada em CRUD de um bean.
 * @author leandro
 */
public abstract class CrudActions extends AbstractAction implements RedirectAfterLogin, AuthenticationFree {

    @Override
    public String execute() {
        String type = input.getStringValue(TYPE);
        if (type == null) {
            type = SHOW;
        }
        if (type.equals(EXPLORE)) {
            listObjects();
        } else { //SHOW, CREATE, UPDATE, DELETE, ETC.
            if (type.equals(CREATE) || type.equals(UPDATE)) {
                doListDependencies();
            }
            if (!type.equals(CREATE)) { //UPDATE, DELETE, SHOW, ETC.
                restoreObject();
            }
            //not crud
            if (!type.equals(CREATE)
                    && !type.equals(SHOW)
                    && !type.equals(UPDATE)
                    && !type.equals(DELETE)) {

                fwdValue(TYPE);

            }
        }
        return type;
    }

	@Override
    public void doListDependencies() {
		super.doListDependencies();
        fwdValue(TYPE);
        listDependencies();
    }

    protected void listObjects() {
    }

    protected void restoreObject() {
    }

    /**
    *  A exibição do bean é desprotegido.
    *  @param innerAction
    */
    public boolean bypassAuthentication(String innerAction) {
        if (innerAction == null) {
            if (input.getStringValue(TYPE) == null) {
                return true;
            } else if (input.getStringValue(TYPE).equals(SHOW)) {
                return true;
            }
        }
        return false;
    }

    
    protected boolean bypassCrud(String innerAction, String crudType) {

        // cond1Pass: Action?type=crudType
        boolean cond1Pass = innerAction == null && input.getStringValue(TYPE).equals(crudType);
        // cond2Pass: Action-innerAction
        boolean cond2Pass = innerAction != null && innerAction.equals(crudType);

        return cond1Pass || cond2Pass;
    }

    /**
     * Os innerActions não redirecionam, somente o execute()
     */
    public boolean shouldRedirect(String inner) {
        if (inner == null || inner.equals("execute")) {
            return true;
        }
        return false;
    }
}
