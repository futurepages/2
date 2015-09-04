package modules.escola.actions;

import java.util.List;
import modules.escola.beans.Aluno;
import modules.escola.validators.AlunoValidator;
import org.futurepages.actions.CrudActions;
import org.futurepages.annotations.Transactional;
import org.futurepages.core.persistence.Dao;

public class AlunoActions extends CrudActions {

    private Aluno aluno;

    @Override
    protected void listDependencies() {
        if(hasError()){
            output.setValue("aluno", aluno);
        }
    }

    @Transactional
    public String create() {
        validate(AlunoValidator.class).create(aluno);
        Dao.save(aluno);
        return success("Aluno criado com sucesso");
    }

    @Override
    protected void listObjects() {
        List<Aluno> alunos = Dao.list(Aluno.class,"","nomeCompleto asc");
        output.setValue("alunos", alunos);
    }
}