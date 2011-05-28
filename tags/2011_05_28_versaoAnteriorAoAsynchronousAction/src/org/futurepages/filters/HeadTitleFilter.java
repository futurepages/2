package org.futurepages.filters;

import org.futurepages.actions.DynAction;
import org.futurepages.core.action.Action;
import org.futurepages.core.action.AsynchronousAction;
import org.futurepages.core.filter.Filter;
import org.futurepages.core.control.InvocationChain;
import org.futurepages.core.output.Output;


/**
 * Define um texto padr�o para o head-title da p�gina
 * de consequ�ncia da action (e suas innerActions)
 *
 * @author leandro
 */
public class HeadTitleFilter implements Filter {

    public static final String SEPARATOR = " - ";

    private String headTitle;   //chave do input do objeto que sofrer� a inje��o

    public HeadTitleFilter(String headTitle) {
        this.headTitle = SEPARATOR+headTitle;
    }

	@Override
    public String filter(InvocationChain chain) throws Exception {
        if(!(chain.getAction() instanceof AsynchronousAction )){
            Output output = chain.getAction().getOutput();
            output.setValue(Action.HEAD_TITLE, headTitle);
        }
        return chain.invoke();
    }

	@Override
    public void destroy() {
    }
}