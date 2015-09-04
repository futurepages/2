package modules.escola.actions;

import java.util.List;
import modules.escola.beans.Aluno;
import modules.escola.beans.Turma;
import modules.escola.validators.AlunoValidator;
import org.futurepages.actions.CrudActions;
import org.futurepages.core.persistence.Dao;
import org.futurepages.util.Is;

public class AlunoActions extends CrudActions {

    private Aluno aluno;

    /*
     * Em output.setValue(...), nesta inner action, � passado
     * a lista de turmas ao componente select.
     * Desta lista, uma turma ser� escolhida para cadastrar
     * um novo aluno.
     */
    @Override
    protected void listDependencies() {
        if(hasError()) {
            output.setValue("aluno", aluno);
        }
        // Recupera as turmas e adiciona-as � tela de cria��o
        // para que possa ser selecionada no cadastro de aluno.
        List<Turma> turmas = Dao.list(Turma.class, "", "codigo asc");
        output.setValue("turmas", turmas);
    }

    /*
     * Em create, al�m de recuperar o aluno, recupera o id
     * da turma que o aluno faz refer�ncia (input.getValue(...)).
     * Em seguida � recuperado do banco a turma que possui tal id
     * (turma = Dao.get(...)) e esta turma � atribu�da ao aluno.
     *
     * OBS: O uso de PIFilter possibilitou a n�o utiliza��o das tr�s
     * linhas comentadas logo abaixo. N�o h� necessidade do programador
     * injetar uma turma em aluno. Ao usar o PIFilter isso passar
     * a ser automatizado.
     */
    public String create() {

//        String sTurma = (String) input.getValue("turma");
//        Turma turma = Dao.get(Turma.class, Integer.parseInt(sTurma));
//        aluno.setTurma(turma);

        validate(AlunoValidator.class).create(aluno);

        Dao.saveTransaction(aluno);
        return success("Aluno criado com sucesso");
    }

    /*
     * listObjects() � chamado quando TYPE � igual a
     * EXPLORE.
     * Chama filtra(...) com par�metro inteiro igual �
     * zero, pois n�o necessita filtrar nenhum aluno por
     * turma.
     */
    @Override
    protected void listObjects() {
        filtra(0);
    }

    /*
     * Inner action chamada quando � solicitado a filtragem
     * de alunos por turma. Recebe como par�mtro o id da turma
     * pelo qual se deseja filtrar os alunos, e faz uma chamada
     * a filtra(...) com tal id (turma).
     */
    public String explore(int turma) {
        filtra(turma);
        return SUCCESS;
    }

    /*
     * Filtra os alunos de acontor com a turma (id da turma).
     */
    private void filtra(Integer turma) {
        String consulta;

        // Verifica se turma possui um valor dafault (n�o filtrar).
        if (!Is.selected(turma)) {
            consulta = "";
        }
        // Criando a String para refinar a busca no banco.
        else {
            consulta = "turma.id = " + turma;
        }

        // Buscas no banco de alunos e turmas
        List<Aluno> alunos = Dao.list(Aluno.class, consulta, "nomeCompleto asc");
        List<Turma> turmas = Dao.list(Turma.class, "", "codigo asc");

        output.setValue("alunos", alunos);
        output.setValue("turmas", turmas);

        // Faz com que select, na tela de vis�o, mostre
        // por padr�o o valor contido em turma.
        output.setValue("turma", turma);
    }
}
