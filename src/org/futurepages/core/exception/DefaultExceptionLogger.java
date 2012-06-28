package org.futurepages.core.exception;

import java.util.Date;
import javax.servlet.ServletException;
import org.futurepages.core.action.AsynchronousManager;
import org.futurepages.core.action.Manipulable;
import org.futurepages.core.control.InvocationChain;
import org.futurepages.exceptions.FuturepagesServletException;
import org.futurepages.exceptions.ServletErrorException;
import org.futurepages.util.DateUtil;
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
        log(exceptionId , "  ("  , DateUtil.viewDateTime(new Date()) , ") >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
						 
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


		if(status500){
			Throwable ex;
			if(throwable.getCause() != null){
				ex = throwable.getCause();
			}else{
				ex = throwable;
			}
			if(ex instanceof ServletErrorException){
				throw (ServletErrorException) ex;
			}
			String protocolNumber = execute(ex,ExceptionLogType.SERVLET_500.name());
			throw new FuturepagesServletException(protocolNumber, actionType, ex);
		}else if(chain!=null){
			String protocolNumber = execute(throwable,actionType);
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

