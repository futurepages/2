package org.futurepages.core.callback;

import org.futurepages.core.context.Context;
import org.futurepages.core.exception.DefaultExceptionLogger;
import org.futurepages.core.persistence.Dao;
import org.futurepages.core.persistence.HibernateManager;
import org.futurepages.util.StringUtils;

public abstract class ConsequenceCallback implements Runnable {

	protected String actionReturn;
	protected Context actionData;
	protected String caller;

	public void setActionData(Context actionData){
		this.actionData = actionData;
	}

	public void setActionReturn(String actionReturn){
		this.actionReturn = actionReturn;
	}
	public void setCaller(String caller){
		this.caller = caller;
	}

	protected void handleException(Exception exCause){
		String message = StringUtils.concat("ConsequenceCallback for "+this.caller,"[",this.actionReturn,"] for data "+this.actionData.toString()+" has crashed.");
		ConsequenceCallbackException ex = new ConsequenceCallbackException(message,exCause);
		DefaultExceptionLogger.getInstance().execute(ex);
	}

	private class ConsequenceCallbackException extends Exception {

		public ConsequenceCallbackException(String message,Exception exCause) {
			super(message, exCause);
		}
	}

	@Override
	public final void run() {
		try {
			Thread.sleep(10000); //para dar o tempo necess�rio para que a a��o seja efetivada no banco de dados.
			doRun();
		} catch (Exception ex) {
			handleException(ex);
			if (Dao.isTransactionActive()) {
				Dao.rollBackTransaction();
			}
		} finally {
			Dao.close();
			HibernateManager.closeSessions();
		}
	}
	
	public abstract void doRun() throws Exception;
}

