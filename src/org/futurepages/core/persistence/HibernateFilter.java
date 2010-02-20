package org.futurepages.core.persistence;

import org.futurepages.annotations.Transactional;
import org.futurepages.core.action.Action;
import org.futurepages.core.filter.AfterConsequenceFilter;
import org.futurepages.core.consequence.Consequence;
import org.futurepages.core.control.InvocationChain;
import org.futurepages.filters.ExceptionFilter;

public class HibernateFilter implements AfterConsequenceFilter {

	public HibernateFilter() {
	}

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

			return ExceptionFilter.treatedException(chain.getAction(), null, throwable);

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

	public void destroy() {
		Dao.close();
	}

	public void afterConsequence(Action action, Consequence c, boolean conseqExecuted, boolean actionExecuted, String result) {
		Dao.close();
	}

	private boolean isTransactional(InvocationChain chain) {
		return chain.getInvokedMethod().isAnnotationPresent(Transactional.class);
	}
}
