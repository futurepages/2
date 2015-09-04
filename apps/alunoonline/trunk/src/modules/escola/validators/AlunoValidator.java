package modules.escola.validators;

import modules.escola.beans.Aluno;
import modules.escola.dao.AlunoDao;
import org.futurepages.core.persistence.Dao;
import org.futurepages.core.validation.Validator;
import org.futurepages.util.Is;

public class AlunoValidator extends Validator {

	public void createOrUpdate(Aluno aluno) {

        // Validação da turma
        if (Is.empty(aluno.getTurma())) {
            error("Selecione uma turma.");
        }

		// Validação do nome
		if (Is.empty(aluno.getNomeCompleto())) {
			error("Preencha o nome do aluno.");
		}

		// Validação da matrícula
		if (Is.empty(aluno.getMatricula())) { //verifica se matrícula vazia
			error("Especifique a matrícula do aluno.");
		} else {
			// Caso matrícula não seja vazia, verifica se a matrícula já existe
			if (AlunoDao.getOutroComMatriculaDeste(aluno) != null) {
				error("Já existe um aluno com esta matrícula.");
			}
		}

		//@TODO falta validação do arquivo de foto. O formato do arquivo teria que ser somente jpg.
	}
}
