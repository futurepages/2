package org.futurepages.core.consequence;

import org.futurepages.core.filter.AfterConsequenceFilter;
import org.futurepages.core.filter.Filter;
import org.futurepages.exceptions.ConsequenceException;
import org.futurepages.core.action.Action;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.futurepages.core.control.ActionConfig;
import org.futurepages.core.control.Controller;
import org.futurepages.core.input.Input;
import org.futurepages.core.input.RequestInput;

/**
 * An action chaining consequence.
 * 
 * @author Sergio Oliveira
 */
public class Chain implements Consequence {

	private ActionConfig ac;
	private String actionPath = null;
	private String innerAction = null;

	public Chain(String actionPath, String innerAction) {
		this.actionPath = actionPath;
		this.innerAction = innerAction;
	}

	public Chain(ActionConfig ac) {
		this.ac = ac;
	}

	/**
	 * Creates a chain consequence for the given ActionConfig
	 * @param ac
	 * @param innerAction
	 */
	public Chain(ActionConfig ac, String innerAction) {
		this(ac);
		this.innerAction = innerAction;
	}

	@Override
	public void execute(Action originalAction, HttpServletRequest req, HttpServletResponse res) throws ConsequenceException {
		Action newAction = ac.getAction();

		if (newAction == null) {
			throw new ConsequenceException("Could not load action for chain: '" + ac + "'");
		}

//		Because of the new InputWrapper filters, do not re-use the input but copy its values!
//		@TODO legacy, verify why that.
		Input input = new RequestInput(req);
		Input old = originalAction.getInput();
		Iterator<String> iterOld = old.keys();

		while (iterOld.hasNext()) {
			String key = (String) iterOld.next();
			input.setValue(key, old.getValue(key));
		}

		newAction.setInput(input);
		newAction.setOutput(originalAction.getOutput());
		newAction.setSession(originalAction.getSession());
		newAction.setApplication(originalAction.getApplication());
		newAction.setMessages(originalAction.getMessages());
		newAction.setLocale(originalAction.getLocale());
		newAction.setCookies(originalAction.getCookies());

		Consequence c = null;

		List<Object> filters = new LinkedList<Object>();

		boolean conseqExecuted = false;
		boolean actionExecuted = false;
		StringBuilder returnedResult = new StringBuilder(32);

		try {
			String inner;
			if (this.innerAction != null) {
				inner = this.innerAction;
			} else {
				inner = ac.getInnerAction();
			}
			c = Controller.invokeAction(ac, newAction, inner, filters, returnedResult);
			actionExecuted = true;
			c.execute(newAction, req, res);
			conseqExecuted = true;

		} catch (ConsequenceException e) {
			throw e;
		} catch (Exception e) {
			throw new ConsequenceException(e);
		} finally {
			Iterator<Object> iter = filters.iterator();
			while (iter.hasNext()) {

				Filter f = (Filter) iter.next();

				if (f instanceof AfterConsequenceFilter) {
					AfterConsequenceFilter acf = (AfterConsequenceFilter) f;
					try {
						String s = returnedResult.toString();
						acf.afterConsequence(newAction, c, conseqExecuted, actionExecuted, s.length() > 0 ? s : null);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public String getActionPath() {
		return actionPath;
	}

	public String getInnerAction() {
		return innerAction;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer(128);
		sb.append("Chain to ").append(ac);
		if (innerAction != null) {
			sb.append(" (innerAction = ").append(innerAction).append(")");
		}
		return sb.toString();
	}

	public void setAc(ActionConfig ac) {
		this.ac = ac;
	}
}