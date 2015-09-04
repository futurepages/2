package modules.escola.validators;

import modules.escola.beans.Turma;
import modules.escola.dao.TurmaDao;
import org.futurepages.core.validation.Validator;
import org.futurepages.util.Is;

public class TurmaValidator extends Validator {

    public void create(Turma turma) {

        // validando o nome da turma
        if (Is.empty(turma.getNome())) {
            error("Preencha o nome da turma.");
        }

        // validação do código está vazio
        if (Is.empty(turma.getCodigo())) {
            error("Preencha o código da turma.");
        }
        // verifica se o código da turma já está cadastrado.
        else if (TurmaDao.getComMesmoCodigoDesta(turma)!=null) {
            error("Já existe uma turma com esse código.");
        }
	    if(turma.getTipo()==null){
            error("Informe o tipo da turma.");
	    }

    }
}
