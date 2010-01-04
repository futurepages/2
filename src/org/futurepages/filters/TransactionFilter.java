package org.futurepages.filters;

import java.util.HashSet;
import java.util.Set;

import org.futurepages.core.action.Action;
import org.futurepages.core.filter.Filter;
import org.futurepages.exceptions.FilterException;
import org.futurepages.core.input.Input;
import org.futurepages.core.control.InvocationChain;
import org.futurepages.core.persistence.transaction.Transaction;

/**
 * <p>
 * Mentawai filter for transaction managment. This filters starts a transaction
 * before the action's execution and commits or rollbacks the transaction after
 * the action's execution, depending on the the action's execution result.
 * </p>
 * <p>
 * The default value for commiting the transaction is SUCCESS but this can be
 * configurated, passing a list of results that allow the commit of the transactionon.
 * </p>
 * <p>
 * Also, if a exception is throwed by the action's execution, the transaction
 * is rollbacked.
 * </p>
 * <p>
 * The transaction filter requires that in the moment that the action's
 * executed is filtered the action's input have an object of a
 * org.mentawai.transaction.Transation implementation class. This object may
 * be injected by the IoCFilter.
 * See <a href="http://www.mentaframework.org/transfilter.jsp">the site documentation</a>
 * for details.
 * </p>
 * <p>
 * The default expected key of the transaction is "transaction", but that can
 * be changed.
 * </p>
 *
 * @author Rubem Azenha (rubem.azenha@gmail.com)
 */
public class TransactionFilter implements Filter {

	private final static String NAME = "TransactionFilter";
	private final static String TRANSACTION_KEY = "transaction";
	private String transactionKey = TRANSACTION_KEY;
	private Set<String> results = new HashSet<String>();
	private boolean onlyPost = false;

	/**
	 * Creates a new TransactionFilter using the default key for the
	 * transaction and the default result for the transaction commit.
	 *
	 */
	public TransactionFilter() {
		results.add(Action.SUCCESS);
	}

	public TransactionFilter(boolean onlyPost) {
		this();
		this.onlyPost = onlyPost;
	}

	/**
	 * Creates a new TransactionFilter using the given key for the
	 * transaction and the default result for the transaction commit.
	 * @param transaction_key
	 */
	public TransactionFilter(String transaction_key) {
		this();
		this.transactionKey = transaction_key;
	}

	public TransactionFilter(String transaction_key, boolean onlyPost) {
		this(transaction_key);
		this.onlyPost = onlyPost;
	}

	/**
	 * Creates a new TransactionFilter using the default key for the
	 * transaction and the given result for the transaction commit.
	 * @param results
	 */
	public TransactionFilter(String[] results) {
		for (int i = 0; i < results.length; i++) {
			this.results.add(results[i]);
		}
	}

	public TransactionFilter(String[] results, boolean onlyPost) {

		this(results);
		this.onlyPost = onlyPost;
	}

	public TransactionFilter(String transaction_key, String[] results) {

		this(transaction_key, results, false);
	}

	/**
	 * Creates a new TransactionFilter using the given key for the
	 * transaction and the given result for the transaction commit.
	 * @param transaction_key
	 * @param results
	 * @param onlyPost
	 */
	public TransactionFilter(String transaction_key, String[] results, boolean onlyPost) {
		this(results);
		this.transactionKey = transaction_key;
		this.onlyPost = onlyPost;
	}

	/**
	 * Filters the action, begining a transaction before the action's
	 * execution and commiting or rollbacking the trasaction after the action's
	 * exection depending on the result.
	 *
	 */
	public String filter(InvocationChain chain) throws Exception {
		Action action = chain.getAction();
		Input input = action.getInput();

		if (onlyPost) {

			// only execute for POST...

			String method = input.getProperty("method");

			boolean isPost = method != null && method.equalsIgnoreCase("post");

			if (!isPost) {
				return chain.invoke();
			}
		}

		Transaction transaction = (Transaction) input.getValue(transactionKey);

		// special case: two actions with transaction filter being called in a chain:

		if (transaction != null && (transaction.wasCommited() || transaction.wasRolledBack())) {

			input.removeValue(transactionKey);

			transaction = (Transaction) input.getValue(transactionKey);
		}

		if (transaction == null) {


			throw new FilterException("Cannot find transaction in action's "
					+ "input with the given key: " + transactionKey);
		}

		try {
			transaction.begin();
			String result = chain.invoke();

			if (results.contains(result)) {

				transaction.commit();

			} else {


				transaction.rollback();

			}

			return result;

		} catch (Exception e) {

			e.printStackTrace();

			//rollbacks the transcion if any error occours.

			transaction.rollback();

			throw e;
		}
	}

	/**
	 * @return the results that will make the transaction be commited after the
	 *  action's execution.
	 *
	 */
	public Set<String> getResultsForCommit() {
		return results;
	}

	/**
	 * Do nothing.
	 */
	public void destroy() {
	}
}
