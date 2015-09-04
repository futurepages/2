package modules.escola.dao;

import modules.escola.beans.Aluno;
import org.futurepages.core.persistence.Dao;
import org.futurepages.core.persistence.HQLProvider;
import org.futurepages.util.Is;

import java.util.List;


public class AlunoDao extends HQLProvider{

	private static final String  DEFAULT_ORDER = asc("nomeCompleto");

	public static List<Aluno> listByTurmaId(int turma) {
		return Dao.list(Aluno.class, Is.selected(turma)? field("turma.id").equalsTo(turma) : "" ,DEFAULT_ORDER);
	}

	public static Aluno getById(int id) {
		return Dao.get(Aluno.class,id);
	}

	public static Aluno getOutroComMatriculaDeste(Aluno aluno) {
		return Dao.uniqueResult(Aluno.class,
									ands(field("id").differentFrom(aluno.getId()),
											field("matricula").equalsTo(aluno.getMatricula())
									)
				);

	}
}