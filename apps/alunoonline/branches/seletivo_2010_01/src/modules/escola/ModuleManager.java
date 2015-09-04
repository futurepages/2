package modules.escola;

import modules.escola.actions.AlunoActions;
import modules.escola.beans.Aluno;
import org.futurepages.core.control.AbstractModuleManager;
import org.futurepages.filters.VOFilter;

public class ModuleManager extends AbstractModuleManager {

    @Override
    public void loadActions() {

        //Configuração da Action com o apelido "Aluno"
        action("Aluno", AlunoActions.class)
            //VOFilter: pega os valores dos campos do formulário da visão e casa-os com os atributos da classe Aluno.class, instancia um objeto
            //deste tipo e insere os valores dos campos do formulário nas respectivas propriedades,
            //o objeto preenchido é atribuido ao campo da action com o nome "aluno".
            .filter(new VOFilter("aluno", Aluno.class))

            // se chamar Aluno?type=create, o método execute será chamado, e a consequência do método deverá
            // mandar o resultado do output (fwIn) para a tela de criação do aluno dentro do módulo (com o template montado)
            .on(CREATE, fwIn("Aluno.create.page"))
                //se ocorrer sucesso na criação do aluno vai para a tela de listagem dos alunos cadastrados
                .on(SUCCESS, CREATE, fwIn("Aluno?type=explore"))
                //se ocorrer error na criacao do aluno volta para a tela de criação do aluno
                .on(ERROR,   CREATE, fwIn("Aluno.create.page"))

            //Aluno?type=explore ocorre o redirecionamento para a tela de listagem dos alunos cadastrados no sistema
            .on(EXPLORE, fwIn("Aluno.explore.page"))
               .on(SUCCESS, EXPLORE, fwIn("Aluno.explore.page"))
         ;

    }
}