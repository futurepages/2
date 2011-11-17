package org.futurepages.core.action;

import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.futurepages.core.admin.DefaultUser;
import org.futurepages.core.context.Context;
import org.futurepages.core.input.Input;
import org.futurepages.core.output.Output;

/**
 * Describes a action, the central idea of the framework architecture.
 *
 * An action has an input and an output .
 *
 * An action generates a result (java.lang.String) after it is executed. The result is usually SUCCESS or ERROR.
 * For each result there is a {@link org.futurepages.core.consequence.Consequence}. The consequences for a web application are usually FORWARD or REDIRECT.
 * An action has access to contexts ({@link org.futurepages.core.context.Context}). The contexts for a web application are usually a SessionContext or a ApplicationContext.
 *
 * @author Sergio Oliveira
 */
public interface Action extends Manipulable {

	public static final String HEAD_TITLE        = "headTitle";
	public static final String MODULE_ID_KEY     = "actionModuleId";
	public static final String REDIR_URL         = "_fpg_redirect_url";
	public static final String PRETTY_URL_PARAMS = "_fpg_pretty_params";


	public DefaultUser loggedUser();

	public boolean isLogged();

	public void setInput(Input input);

	public void setOutput(Output output);

	public void setSession(Context context);

	public void setApplication(Context context);

	public void setCookies(Context context);

	public void setLocale(Locale loc);

	public Input getInput();

	public Output getOutput();

	public Context getSession();

	public HttpSession getHttpSession();

	public HttpServletRequest getRequest();

	public HttpServletResponse getResponse();
	
	public Context getApplication();

	public Context getCookies();

	public Locale getLocale();

	public boolean hasError();

	public String getError();

	public String getSuccess();

	public boolean hasSuccess();

	public boolean hasNoCache();

	public Map<String, String> getMessages();

	public void setMessages(Map<String, String> messages);
}