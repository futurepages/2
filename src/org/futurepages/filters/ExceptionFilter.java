package org.futurepages.filters;

import org.futurepages.core.action.Action;
import org.futurepages.core.exception.DefaultExceptionLogger;
import org.futurepages.core.exception.ExceptionLogger;
import org.futurepages.annotations.NotListDependencies;
import java.util.Date;
import org.futurepages.actions.AjaxAction;
import org.futurepages.actions.DynAction;
import org.futurepages.core.action.AbstractAction;
import org.futurepages.core.control.InvocationChain;
import org.futurepages.core.filter.Filter;
import org.futurepages.exceptions.ErrorException;

public class ExceptionFilter  implements Filter {

	private ExceptionLogger exLogger;

    public ExceptionFilter() {
        exLogger = DefaultExceptionLogger.getInstance();
    }

    public ExceptionFilter(ExceptionLogger exLogger) {
        this.exLogger = exLogger;
    }

    public String filter(InvocationChain chain) throws Exception {
		try {
            return chain.invoke();
        } catch (Throwable throwable) {
			return treatedException(chain.getAction(), exLogger, throwable);
        }
    }

    public void destroy() {
    }


	/**
	 * 
	 * @return DYN_EXCEPTION ou EXCEPTION para exceptions esperadas, ERROR para exceptions esperadas.
	 */
	public static String treatedException(Action action, ExceptionLogger exLogger, Throwable throwable) {
			 //Exceptions, causadas por erros inesperados
            if (!(throwable.getCause() instanceof ErrorException)) {
                long exceptionNumber = (new Date()).getTime();
                if (exLogger != null) {
                    exLogger.execute(throwable, exceptionNumber);
                }
                if(throwable.getCause()!=null){
                    action.getOutput().setValue("exceptionName", throwable.getCause().getClass().getName());
                    action.getOutput().setValue("exceptionNumber", exceptionNumber);
                    action.getOutput().setValue("exceptionMessage", throwable.getCause().getMessage());
                }
                if(action instanceof DynAction || action instanceof AjaxAction){
                    return DYN_EXCEPTION;
                }
                return EXCEPTION;

			//Erros causados por Exceptions Esperadas (ErrorExceptions)
            } else {
                ErrorException errorException = (ErrorException) throwable.getCause();
                boolean listDependencies = true;
                if (throwable.getCause().getClass().isAnnotationPresent(NotListDependencies.class)) {
                    listDependencies = false;
                }
                return ((AbstractAction) action).putError(listDependencies, errorException);
            }
	}
}