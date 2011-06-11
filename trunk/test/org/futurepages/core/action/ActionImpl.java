package org.futurepages.core.action;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.futurepages.actions.AjaxAction;
import org.futurepages.actions.DynAction;
import org.futurepages.annotations.AsynchronousAction;
import org.futurepages.core.admin.DefaultUser;
import org.futurepages.core.context.Context;
import org.futurepages.core.control.InvocationChain;
import org.futurepages.core.input.Input;
import org.futurepages.core.output.Output;
import org.futurepages.enums.AsynchronousActionType;

public class ActionImpl implements Action{

	@Override
	public void setChain(InvocationChain chain) {
	}

	@AsynchronousAction(AsynchronousActionType.DYN)
	public class ClasseDynAnotada extends ActionImpl{}
	
	@AsynchronousAction(AsynchronousActionType.AJAX)
	class ClasseAjaxAnotada extends ActionImpl{}
	
	class ClasseInnterActionDynAnotada extends ActionImpl{
		@AsynchronousAction(AsynchronousActionType.DYN)
		public String action(){return null;}
	}

	class ClasseInnterActionAjaxAnotada extends ActionImpl{
		@AsynchronousAction(AsynchronousActionType.AJAX)
		public String action(){return null;}
	}

	class ClasseInnterActionDynNaoAnotada extends ActionImpl{
		public String action(){return null;}
	}
	
	class ClasseInnterActionAjaxNaoAnotada extends ActionImpl{
		public String action(){return null;}
	}
	
	public class ClasseAjaxImplements extends ActionImpl implements AjaxAction{}
	public class ClasseDynImplements extends ActionImpl implements DynAction{}
	
	class ClasseNaoAsynchronousAction extends ActionImpl{
		public String action(){return null;}
	}

	@Override
	public Context getApplication() {
		return null;
	}

	@Override
	public Context getCookies() {
		return null;
	}

	@Override
	public String getError() {
		return null;
	}

	@Override
	public HttpSession getHttpSession() {
		return null;
	}

	@Override
	public Input getInput() {
		return null;
	}

	@Override
	public Locale getLocale() {
		return null;
	}

	@Override
	public Map<String, String> getMessages() {
		return null;
	}

	@Override
	public Output getOutput() {
		return null;
	}

	@Override
	public HttpServletRequest getRequest() {
		return null;
	}

	@Override
	public HttpServletResponse getResponse() {
		return null;
	}

	@Override
	public Context getSession() {
		return null;
	}

	@Override
	public String getSuccess() {
		return null;
	}

	@Override
	public boolean hasError() {
		return false;
	}

	@Override
	public boolean hasNoCache() {
		return false;
	}

	@Override
	public boolean hasSuccess() {
		return false;
	}

	@Override
	public boolean isLogged() {
		return false;
	}

	@Override
	public DefaultUser loggedUser() {
		return null;
	}

	@Override
	public void setApplication(Context context) {
	}

	@Override
	public void setCookies(Context context) {
	}

	@Override
	public void setInput(Input input) {
	}

	@Override
	public void setLocale(Locale loc) {
	}

	@Override
	public void setMessages(Map<String, String> messages) {
	}

	@Override
	public void setOutput(Output output) {
	}

	@Override
	public void setSession(Context context) {
	}
}