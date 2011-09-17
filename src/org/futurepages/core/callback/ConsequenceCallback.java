package org.futurepages.core.callback;

import org.futurepages.core.context.Context;

public abstract class ConsequenceCallback implements Runnable {

	protected String actionReturn;
	protected Context actionData;

	public void setActionData(Context actionData){
		this.actionData = actionData;
	}

	public void setActionReturn(String actionReturn){
		this.actionReturn = actionReturn;
	}

}