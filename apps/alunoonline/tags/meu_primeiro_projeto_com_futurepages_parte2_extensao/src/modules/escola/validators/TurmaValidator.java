package modules.escola.validators;

import modules.escola.beans.Turma;
import org.futurepages.core.persistence.Dao;
import org.futurepages.core.validation.Validator;
import org.futurepages.util.Is;

public class TurmaValidator extends Validator {

    public void create(Turma turma) {

        // validando o nome da turma
        if (Is.empty(turma.getNome())) {
            error("Preencha o campo: nome da turma.");
        }

        // valida��o do c�digo est� vazio
        if (Is.empty(turma.getCodigo())) {
            error("Preencha o campo: c�digo da turma.");
        }
        // verifica se o c�digo da turma j� est� cadastrado.
        else if (Dao.uniqueResult(Turma.class, "codigo='" + turma.getCodigo() + "'") != null) {
            error("J� existe uma turma com esse c�digo.");
        }

    }
}
