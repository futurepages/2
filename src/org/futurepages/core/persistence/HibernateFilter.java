package org.futurepages.core.persistence;

import java.lang.reflect.Method;
import javax.servlet.ServletException;

import org.futurepages.annotations.NonTransactional;
import org.futurepages.annotations.Transactional;
import org.futurepages.core.action.Action;
import org.futurepages.core.consequence.Consequence;
import org.futurepages.core.control.InvocationChain;
import org.futurepages.core.filter.AfterConsequenceFilter;
import org.futurepages.filters.ExceptionFilter;

public class HibernateFilter implements AfterConsequenceFilter {

	public HibernateFilter() {
	}

	@Override
	public String filter(InvocationChain chain) throws Exception {

		boolean hasError = true;
		boolean isTransactional = isTransactional(chain);

		try {
			if (isTransactional) {
				Dao.beginTransaction();
			}
			String result = chain.invoke();
			hasError = false;
			return result;
			
		} catch (Throwable throwable) {
			return ExceptionFilter.treatedException(chain, throwable);
		} finally {

			if (Dao.isTransactionActive()) {
				if (hasError) {
					Dao.rollBackTransaction();
				} else {
					if (isTransactional) {
						Dao.commitTransaction();
					}
				}
			}
		}
	}

	@Override
	public void destroy() {
		HibernateManager.closeSessions();
	}

	@Override
	public void afterConsequence(Action action, Consequence c, boolean conseqExecuted, boolean actionExecuted, String result) {
		HibernateManager.closeSessions();
	}

	/**
	 * Define se a action é transacional ou não.
	 * @return
	 * false: se o método estiver anotado com {@link NonTransactional}. 
	 * <br>true: se a Classe ou o método estiverem anotados com {@link Transactional} o métoro retornará true.
	 * <br>false: se o método estiver anotado com {@link Transactional} e {@link NonTransactional} simultaneamente
	 */
	protected boolean isTransactional(InvocationChain chain) throws ServletException {
		boolean result = false;
		Method method = chain.getMethod();
		if(method == null){
			throw new ServletException("Inner action '"+chain.getInnerAction()+"' for action '"+chain.getActionName()+"' not found.");
		}
		boolean metodoNaoTransacional = method.isAnnotationPresent(NonTransactional.class);
		if(metodoNaoTransacional){
			result = false;
		}else{
			Class<?> klass = chain.getAction().getClass();
			boolean classeTransacional = klass.isAnnotationPresent(Transactional.class);
			boolean metodoTransacional = method.isAnnotationPresent(Transactional.class);
			result = metodoTransacional || classeTransacional;
		}
		
		return result;
	}
}
