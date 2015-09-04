package modules.escola.install;

import org.futurepages.core.install.Installer;
import org.futurepages.core.persistence.Dao;
import modules.escola.beans.Aluno;
import org.futurepages.util.The;

/**
 * Instala alunos para testes de exemplos
 *
 */
public class AlunosInstaller extends Installer{

    public void execute(){
        Dao.save(new Aluno(00301, "00351063366" , "Leandro Santana Pereira"));
		//Dao.save(new Aluno(00802, "00816459371" , "Dalino Batista Medeiros"));
        Dao.save(new Aluno(02603, "02686685310" , "Alan Carlos Lima"));
		//Dao.save(new Aluno(00904, "00987518321" , "Diógenes Façanha Pires"));

		//Coloquei até 99 porque com 100 tá dando problema.
		for(int i = 5 ; i <= 99 ; i++){
			String matricula = "099"+The.intWithLeftZeros(i, 2);
			Dao.save(new Aluno(Integer.valueOf(matricula), (matricula+matricula),"Nome Aleatório "+ i));
		}
    }
}