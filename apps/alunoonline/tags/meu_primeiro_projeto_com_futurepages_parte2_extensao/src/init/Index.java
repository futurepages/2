package init;

import org.futurepages.actions.FreeAction;
import org.futurepages.util.DateUtil;

/**
 * Ação Inicial da Aplicação
 */
public class Index extends FreeAction{

    @Override
    public String execute(){
        output.setValue("mensagemInicial", "Seja bem-vindo. Data atual: " + DateUtil.viewToday("dd/MM/yyyy"));
        return SUCCESS;
    }
}