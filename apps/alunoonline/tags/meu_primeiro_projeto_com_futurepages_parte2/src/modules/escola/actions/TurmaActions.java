package modules.escola.actions;

import java.util.List;
import modules.escola.beans.Turma;
import modules.escola.validators.TurmaValidator;
import org.futurepages.actions.CrudActions;
import org.futurepages.core.persistence.Dao;

public class TurmaActions extends CrudActions {

    private Turma turma;

    @Override
    protected void listDependencies() {
        if (hasError()) {
            output.setValue("turma", turma);
        }
    }

    public String create() {
        validate(TurmaValidator.class).create(turma);

        Dao.saveTransaction(turma);
        
        return success("Turma criada com sucesso.");
    }

    @Override
    protected void listObjects() {
        List<Turma> turmas = Dao.list(Turma.class, "", "nome asc");
        output.setValue("turmas", turmas);
    }
}
