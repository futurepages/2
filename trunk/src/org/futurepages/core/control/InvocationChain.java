package org.futurepages.core.control;

import org.futurepages.core.filter.Filter;
import org.futurepages.exceptions.ActionException;
import org.futurepages.core.action.Action;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.futurepages.filters.MethodParamFilter;
import org.futurepages.util.InjectionUtils;
import org.futurepages.core.input.Input;

/**
 * When an action is executed, a chain of filters is created.
 * The last step of any InvocationChain is the action.
 * An action may have one or more filters and global filters.
 * 
 * @author Sergio Oliveira
 */
public class InvocationChain {

	private LinkedList<Filter> filters = new LinkedList<Filter>();
	private Action action;
	private String innerAction = null;
	private final String actionName;
	private Method innerMethod = null;

	/**
	 * Creates an InvocationChain for this action.
	 *
	 * @param action The action for what this InvocationChain will be created.
	 */
	public InvocationChain(String actionName, Action action) {

		this.actionName = actionName;

		this.action = action;
	}

	public Filter getFilter(Class<? extends Filter> filterClass) {

		Iterator<Filter> iter = filters.iterator();

		while (iter.hasNext()) {

			Filter f = iter.next();

			if (filterClass.isAssignableFrom(f.getClass())) {

				return f;
			}
		}

		return null;
	}

	/**
	 * Gets the action of this InvocationChain
	 *
	 * @return The action of this InvocationChain
	 */
	public Action getAction() {
		return action;
	}

	void addFilter(Filter filter) {
		filters.add(filter);
	}

	void addFilters(List<Filter> list) {
		filters.addAll(list);
	}

	void clearFilters() {
		filters.clear();
	}

	/**
	 * Invoke and execute the next step in this InvocationChain.
	 * This can be the next filter or the action.
	 *
	 * @return The result of a filter or the action.
	 */
	public String invoke() throws Exception {

		if (!filters.isEmpty()) {
			Filter f = (Filter) filters.removeFirst();

			return f.filter(this);
		}

		String result = null;

		String methodToExec = innerAction;

		if (methodToExec == null) {

			methodToExec = "execute";
			
		}

		Method[] m = action.getClass().getMethods();

		for (int i = 0; i < m.length; i++) {

			if (!Modifier.isPublic(m[i].getModifiers())) {
				continue;
			}
			if (m[i].getName().equals(methodToExec)) {

				Method theOne = m[i];

				Class<?>[] params = theOne.getParameterTypes();

				Input input = action.getInput();

				Object[] paramValues = new Object[params.length];

				Set<String> paramKeys = new HashSet<String>();

				for (int j = 0; j < params.length; j++) {

					boolean found = false;

					// check if we are using the MethodParamFilter!

					List<String> list = (List<String>) input.getValue(MethodParamFilter.PARAM_KEY);

					Iterator<String> keys;

					if (list == null) {

						keys = input.keys();

					} else {

						keys = list.iterator();
					}

					while (keys.hasNext()) {

						String key = keys.next();

						if (paramKeys.contains(key)) {
							continue;
						}
						Object o = input.getValue(key);
						o = InjectionUtils.trimValue(o);

						if (params[j].isInstance(o)) {

							paramValues[j] = o;

							paramKeys.add(key);

							found = true;

							break;

						} else {

							Object converted = InjectionUtils.tryToConvert(o, params[j], action.getLocale(), true);

							if ((converted != null) || // @leandro (to solve a problem)
									((params[j] == Integer.class) || (params[j] == Long.class))) {

								paramValues[j] = converted;

								paramKeys.add(key);

								found = true;

								break;
							}
						}
					}

					if (!found) {

						// let's try to create an object on the fly here...
						// if we have something like add(User u1, User u2) we may get in trouble here,
						// but for this case the user should configure the parameters by hand using a
						// VOFilter ou MethodParamFilter...

						// The if is because this is suppose to be a POJO, not a java.lang.String for example...

						if (!params[j].getName().startsWith("java.lang.") && !params[j].isPrimitive() && InjectionUtils.hasDefaultConstructor(params[j])) {

							Object obj = action.getInput().getObject(params[j]);

							String key = params[j].getSimpleName().toLowerCase();

							paramKeys.add(key);

							paramValues[j] = obj;

							action.getInput().setValue(key, obj);

							found = true;
						}

					}

					if (!found) {
						throw new ActionException("Cannot find parameter value for method: " + methodToExec + " / " + params[j]);
					}
				}

				Object retval = theOne.invoke(action, paramValues);

				if (theOne.getReturnType() == null || theOne.getReturnType().equals(Void.TYPE)) {

					return Action.SUCCESS; // default for void method...

				} else if (theOne.getReturnType() == null || theOne.getReturnType().equals(String.class)) {

					return retval.toString(); // simple string...

				} else { //@byLeandro: este else provavelmente existe por conta da antiga pojoAction

					if (retval == null) {

						return Action.NULL;

					} else {

						return retval.toString();
					}
				}
			}
		}
		Method method = getInvokedMethod();
		if (method != null) {
			try {
				result = (String) method.invoke(action, (Object[]) null);
			} catch (Exception e) {
				throw new ActionException(e);
			}
		} else {
			throw new ActionException("The inner action does not exist: " + innerAction);
		}
		return result;
	}

	/**
	 * Initialize inner action class contexts with the main action contexts...
	 *
	 * @param mainAction The main action.
	 * @param innerAction The inner action object. (inner class)
	 * @since 1.9
	 */
	protected void initInnerAction(Action mainAction, Action innerAction) {

		innerAction.setInput(mainAction.getInput());
		innerAction.setOutput(mainAction.getOutput());
		innerAction.setSession(mainAction.getSession());
		innerAction.setApplication(mainAction.getApplication());
		innerAction.setCookies(mainAction.getCookies());
		innerAction.setLocale(mainAction.getLocale());

	}

	public Method getInvokedMethod() {
		if (innerMethod == null) {
			if (innerAction != null) {
				innerMethod = getMethod(action, innerAction);
			} else {
				innerMethod = getMethod(action, "execute");
			}
		}
		return innerMethod;
	}

	private Method getMethod(Object action, String innerAction) {

		try {
			Method m = action.getClass().getMethod(innerAction, (Class[]) null);
			if (m != null) {
				return m;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Sets an inner action to be executed.
	 * An inner action is a method inside the action implementation that can be executed instead of the execute() method.
	 *
	 * @param innerAction The name of the method to be executed as an inner action
	 */
	public void setInnerAction(String innerAction) {
		this.innerAction = innerAction;
	}

	/**
	 * Returns the inner action being executed in the invocation chain.
	 *
	 * @return The innner action or null if there is no inner action being executed.
	 * @since 1.2.1
	 */
	public String getInnerAction() {
		return innerAction;
	}

	/**
	 * Returns the name of the action being executed in the invocation chain.
	 *
	 * @return The action name like HelloMentawai
	 * @since 1.8
	 */
	public String getActionName() {
		return actionName;
	}

	/**
	 * Returns the filters of this invocation chain.
	 *
	 * @return all filters of this invocation chain
	 * @since 1.4
	 */
	public List<Filter> getFilters() {

		return filters;

	}
}
