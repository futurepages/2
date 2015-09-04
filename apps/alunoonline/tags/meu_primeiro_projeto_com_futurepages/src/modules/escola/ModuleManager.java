package modules.escola;

import modules.escola.actions.AlunoActions;
import modules.escola.beans.Aluno;
import org.futurepages.core.control.AbstractModuleManager;
import org.futurepages.filters.VOFilter;

public class ModuleManager extends AbstractModuleManager {

    @Override
    public void loadActions() {

        //ConfiguraÃ§Ã£o da Action com o apelido "Aluno"
        action("Aluno", AlunoActions.class)
            //VOFilter: pega os valores dos campos do formulÃ¡rio da visÃ£o e casa-os com os atributos da classe Aluno.class, instancia um objeto
            //deste tipo e insere os valores dos campos do formulÃ¡rio nas respectivas propriedades,
            //o objeto preenchido Ã© atribuido ao campo da action com o nome "aluno".
            .filter(new VOFilter("aluno", Aluno.class))

            // se chamar Aluno?type=create, o mÃ©todo execute serÃ¡ chamado, e a consequÃªncia do mÃ©todo deverÃ¡
            // mandar o resultado do output (fwIn) para a tela de criaÃ§Ã£o do aluno dentro do mÃ³dulo (com o template montado)
            .on(CREATE, fwIn("Aluno.create.page"))
                //se ocorrer sucesso na criaÃ§Ã£o do aluno vai para a tela de listagem dos alunos cadastrados
                .on(SUCCESS, CREATE, fwIn("Aluno?type=explore"))
                //se ocorrer error na criacao do aluno volta para a tela de criaÃ§Ã£o do aluno
                .on(ERROR,   CREATE, fwIn("Aluno.create.page"))

            //Aluno?type=explore ocorre o redirecionamento para a tela de listagem dos alunos cadastrados no sistema
            .on(EXPLORE, fwIn("Aluno.explore.page"))
               .on(SUCCESS, EXPLORE, fwIn("Aluno.explore.page"))
         ;

    }
}