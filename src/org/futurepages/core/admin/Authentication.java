package org.futurepages.core.admin;

import org.futurepages.actions.AjaxAction;
import org.futurepages.actions.DynAction;
import org.futurepages.core.action.Action;
import org.futurepages.core.action.Manipulable;

public class Authentication implements Manipulable {

    public static String accessDenied(Action action) {
        if (action instanceof AjaxAction) {
            return AJAX_DENIED;
        }
        if (action instanceof DynAction) {
            return DYN_DENIED;
        }

        return ACCESS_DENIED;
    }

}
