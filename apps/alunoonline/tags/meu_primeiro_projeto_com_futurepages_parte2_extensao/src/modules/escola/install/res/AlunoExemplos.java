package modules.escola.install.res;

import java.util.List;
import modules.escola.beans.Turma;
import java.util.Random;
import modules.escola.beans.Aluno;
import org.futurepages.core.persistence.Dao;

/*
 * Instala alunos para testes de exemplos
 */
public class AlunoExemplos {

    private List<Turma> listaTurma;
    private Random rand = new Random(System.currentTimeMillis());

    public AlunoExemplos() {
    }

    public AlunoExemplos(List<Turma> listaTurma) {
        this.listaTurma = listaTurma;
    }

    private int modulo(int n) {
        return ((n < 0) ? (-n) : n);
    }

    /*
     * Retorna uma refência, de forma dinâmica, a uma turma
     * dentro da lista de turmas recuperadas do banco.
     */
    private Turma obtemTurmaRandomica() {
        return listaTurma.get(modulo(rand.nextInt()) % listaTurma.size());
    }

    /*
     * Instala os alunos e os cadastra em turmas
     * de forma randomicas.
     */
    public void execute() {
        
        Dao.save(new Aluno("03N1047000", "Maria da Silva", obtemTurmaRandomica()));
        Dao.save(new Aluno("02N1047451", "João Fulano Cavalcante Soares", obtemTurmaRandomica()));
        Dao.save(new Aluno("99S5447050", "Antônio Feliciano Gomes", obtemTurmaRandomica()));
        Dao.save(new Aluno("99S5000001", "Sicrano da Silva e Silva", obtemTurmaRandomica()));
        Dao.save(new Aluno("99S8348349", "Maria Antonieta da Costa", obtemTurmaRandomica()));
        Dao.save(new Aluno("93E9394322", "José Pompeu Exemplus da Silva", obtemTurmaRandomica()));
    }
}
