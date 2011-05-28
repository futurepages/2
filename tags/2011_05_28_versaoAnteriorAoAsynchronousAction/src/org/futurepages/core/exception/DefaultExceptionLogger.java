package org.futurepages.core.exception;

public class DefaultExceptionLogger implements ExceptionLogger{

    private static final DefaultExceptionLogger INSTANCE = new DefaultExceptionLogger();

    public static DefaultExceptionLogger getInstance() {
        return INSTANCE;
    }

    private boolean showTrace = true;

    public DefaultExceptionLogger(){
    }

    public DefaultExceptionLogger(boolean showTrace){
        this.showTrace = showTrace;
    }

	public void execute(Throwable throwable) {
		execute(throwable, System.currentTimeMillis());
	}

    public void execute(Throwable throwable, long numeroProtocolo){
        handleException(throwable,numeroProtocolo, showTrace);
    }

	protected void handleException(Throwable throwable,long numeroProtocolo, boolean trace) {

		Throwable root = throwable.getCause();
        String message = "";
        if(throwable.getCause()!=null){
            message = throwable.getCause().getMessage();
        }

		if (root == null) root = throwable;

        System.out.println("EXCEPTION "+numeroProtocolo+" ------------------------->");
        System.out.println(message);
      	if (trace) {
            root.printStackTrace();
        }
        System.out.println(numeroProtocolo+" <--------------------------------------");
	}
}