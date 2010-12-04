package org.futurepages.filters;

import java.util.Date;
import javax.servlet.ServletException;

import org.futurepages.annotations.NotListDependencies;
import org.futurepages.core.action.AbstractAction;
import org.futurepages.core.action.Action;
import org.futurepages.core.action.AsynchronousAction;
import org.futurepages.core.control.InvocationChain;
import org.futurepages.core.exception.DefaultExceptionLogger;
import org.futurepages.core.exception.ExceptionLogger;
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

	@Override
	public String filter(InvocationChain chain) throws Exception {
		try {
			return chain.invoke();
		} catch (Throwable throwable) {
			return treatedException(chain.getAction(), exLogger, throwable);
		}
	}

	@Override
	public void destroy() {
	}


	/**
	 * 
	 * @return DYN_EXCEPTION ou EXCEPTION para exceptions esperadas, ERROR para exceptions esperadas.
	 */
	public static String treatedException(Action action, ExceptionLogger exLogger, Throwable excecao) throws ServletException {
		Throwable cause = excecao;
		if(excecao.getCause() != null){
			cause = excecao.getCause();
		}
		boolean isErrorException = (cause instanceof ErrorException);
		if(cause instanceof ServletException ){
			throw ((ServletException)cause);  //Erro 500 (??) @TODO Verificar a viabilidade. Colocado por conta do uploadfy
		}
		if(isErrorException){
			//Erros causados por Exceptions Esperadas (ErrorExceptions)
			ErrorException errorException = (ErrorException) cause;
			boolean listDependencies = true;
			if (cause.getClass().isAnnotationPresent(NotListDependencies.class)) {
				listDependencies = false;
			}
			return ((AbstractAction) action).putError(listDependencies, errorException);

		}else{
			//Exceptions, causadas por erros inesperados

			long exceptionNumber = (new Date()).getTime();
			if (exLogger != null) {
				exLogger.execute(cause, exceptionNumber);
			} else {
				DefaultExceptionLogger.getInstance().execute(cause, exceptionNumber);
			}

			if(cause!=null){
				action.getOutput().setValue("exceptionName", cause.getClass().getName());
				action.getOutput().setValue("exceptionNumber", exceptionNumber);
				action.getOutput().setValue("exceptionMessage", cause.getMessage());
			}
			if(action instanceof AsynchronousAction){
				return DYN_EXCEPTION;
			}
			return EXCEPTION;

		}
	}
}