package org.futurepages.core.callback;

import org.futurepages.core.action.Action;

public interface Callback extends Runnable {

	public Action getAction();
	public void setAction(Action action);

	public void run();
}
