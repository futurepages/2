package modules.escola.install;

import java.util.List;
import modules.escola.beans.Turma;
import modules.escola.install.res.AlunoExemplos;
import modules.escola.install.res.TurmaExemplos;
import org.futurepages.core.install.Installer;
import org.futurepages.core.persistence.Dao;

/*
 * Instala as turmas e depois os alunos.
 */
public class EscolaInstaller extends Installer{

    @Override
    public void execute() throws Exception {
        List<Turma> listaTurma;

        new TurmaExemplos().execute();

        listaTurma = Dao.list(Turma.class);

        new AlunoExemplos(listaTurma).execute();
    }
}