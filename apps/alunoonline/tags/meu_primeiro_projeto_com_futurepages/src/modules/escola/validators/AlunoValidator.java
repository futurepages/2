package modules.escola.validators;

import modules.escola.beans.Aluno;
import org.futurepages.core.persistence.Dao;
import org.futurepages.core.validation.Validator;
import org.futurepages.util.Is;

public class AlunoValidator extends Validator {

	public void create(Aluno aluno) {

		// ValidaÃ§Ã£o do nome
		if (Is.empty(aluno.getNomeCompleto())) {
			error("Preencha o nome do aluno.");
		}

		// ValidaÃ§Ã£o da matrÃ­cula
		if (Is.empty(aluno.getMatricula())) { //verifica se matrÃ­cula vazia
			error("Especifique a matrÃ­cula do aluno.");
		} else {
			// Caso matrÃ­cula nÃ£o seja vazia, verifica se a matrÃ­cula jÃ¡ existe
			if (Dao.uniqueResult(Aluno.class, "matricula='" + aluno.getMatricula() + "'") != null) {
				error("JÃ¡ existe um aluno com esta matrÃ­cula.");
			}
		}
	}
}
