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
		return isDynAction(chain) || isAjaxAction(chain);
	}

	public static boolean isDynAction(InvocationChain chain) {
		if(isInnerActionAnnotatedWith(chain, DYN)){
			return true;
		}
		if(isInnerActionAnnotatedWith(chain, AJAX)){
			return false;
		}
		if(DynAction.class.isAssignableFrom(chain.getAction().getClass())||isClassAnnotatedWIth(chain, DYN)){
			return true;
		}
		return false;
	}

	public static boolean isAjaxAction(InvocationChain chain) {
		if(isInnerActionAnnotatedWith(chain, AJAX)){
			return true;
		}
		if(isInnerActionAnnotatedWith(chain, DYN)){
			return false;
		}
		if(AjaxAction.class.isAssignableFrom(chain.getAction().getClass())||isClassAnnotatedWIth(chain, AJAX)){
			return true;
		}
		return false;
	}
	
	private static boolean isInnerActionAnnotatedWith(InvocationChain chain, AsynchronousActionType tipo){
		Method method = chain.getMethod();
		if(method!=null){
			AsynchronousAction annotation = method.getAnnotation(AsynchronousAction.class);
			if(annotation!=null && annotation.value().equals(tipo)){
				return true;
			}
		}
		return false;
	}

	private static boolean isClassAnnotatedWIth(InvocationChain chain, AsynchronousActionType tipo){
		AsynchronousAction annotation = chain.getAction().getClass().getAnnotation(AsynchronousAction.class);
		if(annotation!=null && annotation.value().equals(tipo)){
			return true;
		}
		return false;

	}
}

