package modules.escola.validators;

import modules.escola.beans.Aluno;
import org.futurepages.core.persistence.Dao;
import org.futurepages.core.validation.Validator;
import org.futurepages.util.Is;

public class AlunoValidator extends Validator {

	public void create(Aluno aluno) {

        // Valida��o da turma
        if (Is.empty(aluno.getTurma())) {
            error("Selecione uma turma.");
        }

		// Valida��o do nome
		if (Is.empty(aluno.getNomeCompleto())) {
			error("Preencha o nome do aluno.");
		}

		// Valida��o da matr�cula
		if (Is.empty(aluno.getMatricula())) { //verifica se matr�cula vazia
			error("Especifique a matr�cula do aluno.");
		} else {
			// Caso matr�cula n�o seja vazia, verifica se a matr�cula j� existe
			if (Dao.uniqueResult(Aluno.class, "matricula='" + aluno.getMatricula() + "'") != null) {
				error("J� existe um aluno com esta matr�cula.");
			}
		}
	}
}
