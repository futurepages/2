package modules.escola;

import modules.escola.actions.AlunoActions;
import modules.escola.actions.TurmaActions;
import modules.escola.beans.Aluno;
import modules.escola.beans.Turma;
import org.futurepages.core.control.AbstractModuleManager;
import org.futurepages.filters.PIFilter;
import org.futurepages.filters.VOFilter;

public class ModuleManager extends AbstractModuleManager {

    @Override
    public void loadActions() {

        //Configura��o da Action com o apelido "Aluno"
        action("Aluno", AlunoActions.class)
            // VOFilter: pega os valores dos campos do formul�rio da vis�o e
            // casa-os com os atributos da classe Aluno.class, instancia um
            // objeto deste tipo e insere os valores dos campos do formul�rio
            // nas respectivas propriedades, o objeto preenchido � atribuido
            // ao campo da action com o nome "aluno".
            .filter(new VOFilter("aluno", Aluno.class))

            // PIFIlter: recupeda do banco uma "turma" de acordo com o id
            // adquirida no formul�rio e insere-o em um objeto "aluno".
            // Retirando assim, esta tarefa do programador.
            .filter(new PIFilter("aluno", "turma", Turma.class))

            // se chamar Aluno?type=create, o m�todo execute ser� chamado, e
            // a consequ�ncia do m�todo dever� mandar o resultado do output
            // (fwIn) para a tela de cria��o do aluno dentro do m�dulo
            // (com o template montado)
            .on(CREATE, fwIn("Aluno.create.page"))

            // se ocorrer sucesso na cria��o do aluno vai para a tela de
            // listagem dos alunos cadastrados
            .on(SUCCESS, CREATE, fwIn("Aluno?type=explore"))

            // se ocorrer error na criacao do aluno volta para a tela de
            // cria��o do aluno
            .on(ERROR,   CREATE, fwIn("Aluno.create.page"))

            // Aluno?type=explore ocorre o redirecionamento para a tela
            // de listagem dos alunos cadastrados no sistema
            .on(EXPLORE, fwIn("Aluno.explore.page"))

            // Aluno.explore.fpg retorna SUCCESS e redireciona para a
            // tela de listagem de alunos cadastrados, filtrados por
            // turma.
            .on(SUCCESS, EXPLORE, fwIn("Aluno.explore.page"))

            // Se type for igual a UPDATE a consequ�ncia ser� a tela
            // de atualiza��o
            .on(UPDATE, fwIn("Aluno.update.page"))

            // Se update retornar SUCCESS a consequ�ncia ser� a tela
            // de explora��o. (OBS: n�o foi implementado a consequ�ncia
            // caso update retorne ERROR. Isto fica � cargo do estudante.)
            .on(SUCCESS, UPDATE, fwIn("Aluno?type=explore"))

            // Se delete retornar SUCCESS a consequ�ncia ser� a tela
            // de explora��o. (OBS: n�o foi implementado a consequ�ncia
            // caso delete retorne ERROR. Isto fica � cargo do estudante.)
            .on(SUCCESS, DELETE, fwIn("Aluno.fpg?type=explore"));

        // Configura��o da Action "Turma"
        // TurmaActions recebe um apelido "Turma"
        action("Turma", TurmaActions.class)

            //VOFilter: pega os valores dos campos do formul�rio da vis�o e
            // casa-os com os atributos da classe Turma.class, instancia um
            // objeto deste tipo e insere os valores dos campos do formul�rio
            // nas respectivas propriedades, o objeto preenchido � atribuido
            // ao campo da action com o nome "turma".
            .filter(new VOFilter("turma", Turma.class))

            // se chamar Turma?type=create, o m�todo execute ser� chamado, e
            // a consequ�ncia do m�todo dever� mandar o resultado do output
            // (fwIn) para a tela de cria��o do aluno dentro do m�dulo
            // (com o template montado)
            .on(CREATE, fwIn("Turma.create.page"))

            // se ocorrer sucesso na cria��o de turma vai para a tela de
            // listagem das turmas cadastrados
            .on(SUCCESS, CREATE, fwIn("Turma.fpg?type=explore"))

            // se ocorrer error na criacao da turma volta para a tela de
            // cria��o de turma
            .on(ERROR, CREATE, fwIn("Turma.create.page"))

            // Turma?type=explore ocorre o redirecionamento para a tela
            // de listagem das turmas cadastrados no sistema
            .on(EXPLORE, fwIn("Turma.explore.page"));
    }
}
