package init;

import org.futurepages.actions.FreeAction;

import java.util.Calendar;

/**
 * Ação Inicial da Aplicação
 */
public class Index extends FreeAction{

    @Override
    public String execute(){
        output("mensagemInicial", "Seja bem-vindo.");
	    output("momentoAtual",Calendar.getInstance());
        return SUCCESS;
    }
}