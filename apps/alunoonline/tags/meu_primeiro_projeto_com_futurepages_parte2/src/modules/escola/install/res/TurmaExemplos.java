package modules.escola.install.res;

import modules.escola.beans.Turma;
import org.futurepages.core.persistence.Dao;

/*
 * Instala turmas para testes de exemplos
 */
public class TurmaExemplos {

    public void execute() {
        Dao.save(new Turma("T01",  "C�lculo I"));
        Dao.save(new Turma("T02",  "Introdu��o � computa��o"));
        Dao.save(new Turma("T03",  "L�gica"));
        Dao.save(new Turma("T04",  "Estat�stica"));
        Dao.save(new Turma("T05",  "Programa��o I"));
        Dao.save(new Turma("T06",  "Programa��o II"));
        Dao.save(new Turma("T07",  "C�lculo II"));
        Dao.save(new Turma("T08",  "Estrutura de dados"));
        Dao.save(new Turma("T09",  "Arquitetura de computadores"));
        Dao.save(new Turma("T10",  "Redes de computadores I"));
        Dao.save(new Turma("T011", "Sistemas Operacionais I"));
        Dao.save(new Turma("T012", "Redes de computadores II"));
        Dao.save(new Turma("T013", "Sistemas Operacionais II"));
    }

}
