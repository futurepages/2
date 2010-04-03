package org.futurepages.core.control;

import org.futurepages.core.context.CookieContext;
import org.futurepages.core.context.SessionContext;
import org.futurepages.core.action.Action;
import org.futurepages.core.config.Params;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.futurepages.core.input.PrettyURLRequestInput;
import org.futurepages.core.output.ResponseOutput;

import org.futurepages.core.i18n.LocaleManager;

public class PrettyURLController extends Controller {

	private static final String INNER_ACTION_SEPARATOR_PARAM = "innerActionSeparator";

	private static final char DEFAULT_INNER_ACTION_SEPARATOR = '-';

	private char innerActionSeparator;

    static final String EXTENSION = "." + AbstractApplicationManager.EXTENSION;

	@Override
	public void init(ServletConfig conf) throws ServletException {
		
		super.init(conf);

		String innerActionSeparatorParam = conf.getInitParameter(INNER_ACTION_SEPARATOR_PARAM);

		if (innerActionSeparatorParam != null) {
			
			validateLength(innerActionSeparatorParam);

			validateContent(innerActionSeparatorParam);
			
			this.innerActionSeparator = innerActionSeparatorParam.charAt(0);
			
		} else {
			
			this.innerActionSeparator = DEFAULT_INNER_ACTION_SEPARATOR;
		}
	}

	private void validateContent(String innerActionSeparatorParam) throws ServletException {
		
		if (innerActionSeparatorParam.equals("/")) {
			
			throw new ServletException("The "
					+ INNER_ACTION_SEPARATOR_PARAM
					+ " context parameter cannot be the \'/\' char");
		}
	}

	private void validateLength(String innerActionSeparatorParam) throws ServletException {
		
		if (innerActionSeparatorParam.length() != 1) {
			
			throw new ServletException("The "
					+ INNER_ACTION_SEPARATOR_PARAM
					+ " context parameter must have only one char");
		}
	}

	
	private boolean isPrettyURL(HttpServletRequest req) {
		
		String uri = req.getRequestURI().toString();

		// cut the last '/'
		if (uri.endsWith("/") && uri.length() > 1) {
			
			uri = uri.substring(0, uri.length() - 1);
		}
		
		return !uri.endsWith(EXTENSION);
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

        if(s.length==1){
            if(s[0].equals(Params.get("START_PAGE_NAME"))){
                return s[0];
            }
            else
            if(!s[0].equals(EXTENSION)){
                return s[0]+"/"+Params.get("START_PAGE_NAME");
            }
        }
        else
		if (s.length >= 2) {

            //@byLeandro (para prever URLs dos módulos)
            if(!s[0].equals(EXTENSION)){
                return s[0]+"/"+s[1];
            }
			return s[1];
		}
		return null;
	}

	@Override
	protected String getActionName(HttpServletRequest req) {

		if (!isPrettyURL(req)) {
			
			return super.getActionName(req);
		}
		
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

		if (!isPrettyURL(req)) {
			
			return super.getInnerActionName(req);
		}
		
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
		
		if (!isPrettyURL(req)) {
			
			super.prepareAction(action, req, res);
			
			return;
		}

		action.setInput(new PrettyURLRequestInput(req));
		action.setOutput(new ResponseOutput(res));
		action.setSession(new SessionContext(req, res));
		action.setApplication(appContext);
		action.setCookies(new CookieContext(req, res));
		action.setLocale(LocaleManager.getLocale(req));
	}
}
