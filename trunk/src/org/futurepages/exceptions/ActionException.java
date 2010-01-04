package org.futurepages.exceptions;

/**
 * An exception that can happen when an action is executed.
 * 
 * @author Sergio Oliveira
 */
public class ActionException extends Exception {
	
	protected final Throwable rootCause;
	
	public ActionException() {
		super();
		
		this.rootCause = null;
		
	}
	
	public ActionException(Throwable e) {
		super(getMsg(e), e);
		Throwable root = getRootCause(e);
		this.setStackTrace(root.getStackTrace());
		
		this.rootCause = root == this ? null : root;
	}
	
	private static String getMsg(Throwable t) {
		
		Throwable root = getRootCause(t);
		
		String msg = root.getMessage();
		
		if (msg == null || msg.length() == 0) {
			
			msg = t.getMessage();
			
			if (msg == null || msg.length() == 0) return root.getClass().getName();
		}
		
		return msg;
	}
	
	public ActionException(String msg) {
		super(msg);
		
		this.rootCause = null;
	}
	
	public ActionException(String msg, Throwable e) {
		
		super(msg, e);
		Throwable root = getRootCause(e);
		this.setStackTrace(root.getStackTrace());
		
		this.rootCause = root == this ? null : root;
	}
	
	private static Throwable getRootCause(Throwable t) {
		
		Throwable root = t.getCause();
		
		if (root == null) return t;
		
		while(root.getCause() != null) {
			
			root = root.getCause();
		}
		
		return root;
		
	}	
	
	@Override
	public Throwable getCause() {
		
		return rootCause;
	}
}
