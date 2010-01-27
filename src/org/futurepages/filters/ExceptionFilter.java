package org.futurepages.filters;

import org.futurepages.core.exception.DefaultExceptionLogger;
import org.futurepages.core.exception.ExceptionLoggerImpl;
import org.futurepages.annotations.NotListDependencies;
import org.futurepages.core.persistence.Dao;
import java.util.Date;
import org.futurepages.actions.AjaxAction;
import org.futurepages.actions.DynAction;
import org.futurepages.core.action.AbstractAction;
import org.futurepages.core.filter.Filter;
import org.futurepages.core.control.InvocationChain;
import org.futurepages.exceptions.ErrorException;

public class ExceptionFilter implements Filter {

	private ExceptionLoggerImpl exLogger;

    public ExceptionFilter() {
        exLogger = DefaultExceptionLogger.getInstance();
    }

    public ExceptionFilter(ExceptionLoggerImpl exLogger) {
        this.exLogger = exLogger;
    }

    public String filter(InvocationChain chain) throws Exception {
        try {
            return chain.invoke();
        } catch (Throwable throwable) {

			 AbstractAction action = (AbstractAction) chain.getAction();

			 //Exceptions, causadas por erros inesperados
            if (!(throwable.getCause() instanceof ErrorException)) {
                long exceptionNumber = (new Date()).getTime();
                if (exLogger != null) {
                    exLogger.execute(throwable,exceptionNumber);
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
                if (Dao.isTransactionActive()) {
                    Dao.rollBackTransaction();
                }
                boolean listDependencies = true;
                if (throwable.getCause().getClass().isAnnotationPresent(NotListDependencies.class)) {
                    listDependencies = false;
                }
                return action.putError(listDependencies, errorException);
            }
        }
    }

    public void destroy() {
    }
}