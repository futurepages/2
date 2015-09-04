package modules.escola.install;

import org.futurepages.core.install.Installer;
import org.futurepages.core.persistence.Dao;
import modules.escola.beans.Aluno;

/**
 * Instala alunos para testes de exemplos
 *
 */
public class AlunosInstaller extends Installer{

    public void execute(){
        Dao.save(new Aluno("03N1047000", "Maria da Silva"));
        Dao.save(new Aluno("02N1047451", "João Fulano Cavalcante Soares"));
        Dao.save(new Aluno("99S5447050", "Antônio Feliciano Gomes"));
        Dao.save(new Aluno("99S5000001", "Sicrano da Silva e Silva"));
        Dao.save(new Aluno("99S8348349", "Maria Antonieta da Costa"));
        Dao.save(new Aluno("93E9394322", "José Pompeu Exemplus da Silva"));
    }
}