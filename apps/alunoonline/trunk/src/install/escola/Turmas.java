package install.escola;

import modules.escola.beans.TipoTurma;
import modules.escola.beans.Turma;
import modules.escola.dao.TipoTurmaDao;
import org.apache.commons.lang.math.RandomUtils;
import org.futurepages.core.install.Installation;
import org.futurepages.core.persistence.Dao;

/*
 * Instala turmas para testes de exemplos
 */
public class Turmas implements Installation {

    public void execute() {
        insereTurma("T01",  "Cálculo I");
        insereTurma("T02",  "Introdução a computação");
        insereTurma("T03",  "Lógica");
        insereTurma("T04",  "Estatística");
        insereTurma("T05",  "Programação I");
        insereTurma("T06",  "Programação II");
        insereTurma("T07",  "Cálculo II");
        insereTurma("T08",  "Estrutura de dados");
        insereTurma("T09",  "Arquitetura de computadores");
        insereTurma("T10",  "Redes de computadores I");
        insereTurma("T011", "Sistemas Operacionais I");
        insereTurma("T012", "Redes de computadores II");
        insereTurma("T013", "Sistemas Operacionais II");
    }

	private void insereTurma(String codigo, String nome){
		Turma turma = new Turma(codigo, nome);
		int totalTipos = (int) Dao.numRows(TipoTurma.class,null);
		TipoTurma tipo = TipoTurmaDao.getById(RandomUtils.nextInt(totalTipos) + 1);
		turma.setTipo(tipo);
		Dao.save(turma);
	}

}
