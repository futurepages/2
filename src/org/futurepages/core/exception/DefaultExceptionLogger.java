package org.futurepages.core.exception;

import javax.servlet.ServletException;
import org.futurepages.core.action.AsynchronousManager;
import org.futurepages.core.action.Manipulable;
import org.futurepages.core.control.InvocationChain;
import org.futurepages.exceptions.FuturepagesServletException;
import org.futurepages.exceptions.ServletErrorException;
import org.futurepages.util.StringUtils;


public class DefaultExceptionLogger implements ExceptionLogger, Manipulable{

    private static final DefaultExceptionLogger INSTANCE = new DefaultExceptionLogger();

    public static DefaultExceptionLogger getInstance() {
        return INSTANCE;
    }

	private DefaultExceptionLogger() {}

	public void execute(Throwable throwable) {
		execute(throwable, ExceptionLogType.SILENT_EXCEPTION.name());
	}

	public String execute(Throwable throwable, String errorType) {
		String numeroProtocolo = System.currentTimeMillis()+"-"+Thread.currentThread().getId();

		String exceptionId =  StringUtils.concat("[",errorType.toUpperCase(),"] ",numeroProtocolo);
        log(exceptionId ," >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
						 
		throwable.printStackTrace();

		log("\n"+exceptionId+" <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n");
		return numeroProtocolo;
	}

	public String execute(Throwable throwable, InvocationChain chain, boolean status500) throws ServletException {
		
		String actionType = null;
		if(chain!=null){
			if(AsynchronousManager.isAsynchronousAction(chain)){
				actionType = DYN_EXCEPTION;
			}else{
				actionType = EXCEPTION;
			}
		}

		String protocolNumber = execute(throwable,(status500?ExceptionLogType.SERVLET_500.name():actionType));

		if(status500){
			if(throwable instanceof ServletErrorException){
				throw (ServletErrorException) throwable;
			}
			throw new FuturepagesServletException(protocolNumber, actionType, throwable);
		}else if(chain!=null){
			chain.getAction().getOutput().setValue(EXCEPTION, new Exception(protocolNumber,throwable));
		}
		
		return actionType;
	}

	private void log(String... strs){
		System.out.println(StringUtils.concat(strs));
	}
	
	private enum ExceptionLogType {
		EXCEPTION, SERVLET_500, DYN_EXCEPTION, SILENT_EXCEPTION;
	}
}

