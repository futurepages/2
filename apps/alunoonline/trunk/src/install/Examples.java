package install;

import install.escola.Alunos;
import install.escola.Turmas;
import modules.escola.beans.Turma;
import org.futurepages.core.install.Installer;
import org.futurepages.core.persistence.Dao;

import java.util.List;

public class Examples extends Installer {

	@Override
	public void execute() throws Exception {

		install(new Turmas());

		List<Turma> turmas = Dao.list(Turma.class);
		install(new Alunos(turmas));

	}
}