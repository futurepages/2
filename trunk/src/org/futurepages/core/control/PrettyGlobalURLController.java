package org.futurepages.core.control;

import org.futurepages.core.context.CookieContext;
import org.futurepages.core.context.SessionContext;
import org.futurepages.core.action.Action;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.futurepages.core.input.PrettyGlobalURLRequestInput;
import org.futurepages.core.output.ResponseOutput;

import org.futurepages.core.i18n.LocaleManager;

public class PrettyGlobalURLController extends Controller {

	private static final char DEFAULT_INNER_ACTION_SEPARATOR = '-';

	private char innerActionSeparator;

    static final String EXTENSION = "." + AbstractApplicationManager.EXTENSION;

	@Override
	public void init(ServletConfig conf) throws ServletException {
		super.init(conf);
		this.innerActionSeparator = DEFAULT_INNER_ACTION_SEPARATOR;

	}
	
	private String getActionPlusInnerAction(HttpServletRequest req) {

		String context = req.getContextPath();

		String uri = req.getRequestURI().toString();

		// remove the context from the uri, if present

		if (context.length() > 0 && uri.indexOf(context) == 0) {

			uri = uri.substring(context.length());

		}

		// cut the first '/'
		if (uri.startsWith("/") && uri.length() > 1) {

			uri = uri.substring(1);

		}

		// cut the last '/'
		if (uri.endsWith("/") && uri.length() > 1) {

			uri = uri.substring(0, uri.length() - 1);
		}

		String[] s = uri.split("/");

		return s[0];
	}

	@Override
	protected String getActionName(HttpServletRequest req) {
	
		String s = getActionPlusInnerAction(req);

		// separate the inner action from action...

		int index = s.indexOf(innerActionSeparator);

		if (index > 0) {

			return s.substring(0, index);
		}

		return s;
	}

	@Override
	protected String getInnerActionName(HttpServletRequest req) {
	
		String s = getActionPlusInnerAction(req);

		// separate the inner action from action...

		int index = s.indexOf(innerActionSeparator);

		if (index > 0 && index + 1 < s.length()) {

			return s.substring(index + 1);
		}

		return null;
	}

	@Override
	protected void prepareAction(Action action, HttpServletRequest req,
			HttpServletResponse res) {
		
		action.setInput(new PrettyGlobalURLRequestInput(req));
		action.setOutput(new ResponseOutput(res));
		action.setSession(new SessionContext(req, res));
		action.setApplication(appContext);
		action.setCookies(new CookieContext(req, res));
		action.setLocale(LocaleManager.getLocale(req));
	}
}