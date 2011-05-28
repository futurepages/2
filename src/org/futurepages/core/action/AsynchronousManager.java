package org.futurepages.core.action;

import static org.futurepages.enums.AsynchronousActionType.AJAX;
import static org.futurepages.enums.AsynchronousActionType.DYN;

import java.lang.reflect.Method;

import org.futurepages.actions.AjaxAction;
import org.futurepages.actions.DynAction;
import org.futurepages.annotations.AsynchronousAction;
import org.futurepages.core.control.InvocationChain;
import org.futurepages.enums.AsynchronousActionType;

public class AsynchronousManager {

	public static boolean isAsynchronousAction(InvocationChain chain) {
		return chain instanceof AsynchronousAction;
	}

	public static boolean isDynAction(InvocationChain chain) {
		return isLike(chain, DynAction.class, DYN);
	}

	public static boolean isAjaxAction(InvocationChain chain) {
		return isLike(chain, AjaxAction.class, AJAX);
	}

	private static boolean isLike(InvocationChain chain, Class<? extends org.futurepages.core.action.AsynchronousAction> classe,	AsynchronousActionType type) {
		return classe.isAssignableFrom(chain.getAction().getClass()) || isAnnotatedWIth(chain, type);
	}

	public static boolean isAnnotatedWIth(InvocationChain chain, AsynchronousActionType tipo){
		AsynchronousAction annotation = chain.getAction().getClass().getAnnotation(AsynchronousAction.class);
		if(annotation!=null && annotation.value().equals(tipo)){
			return true;
		}

		Method method = chain.getMethod();
		annotation = method.getAnnotation(AsynchronousAction.class);
		if(annotation!=null && annotation.value().equals(tipo)){
			return true;
		}
		return false;

	}
}

