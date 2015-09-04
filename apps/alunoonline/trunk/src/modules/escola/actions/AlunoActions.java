package modules.escola.actions;

import install.Resources;
import modules.escola.beans.Aluno;
import modules.escola.beans.Turma;
import modules.escola.dao.AlunoDao;
import modules.escola.dao.TurmaDao;
import modules.escola.validators.AlunoValidator;
import org.apache.commons.fileupload.FileItem;
import org.futurepages.actions.CrudActions;
import org.futurepages.annotations.Transactional;
import org.futurepages.core.persistence.Dao;
import org.futurepages.enums.PathTypeEnum;
import org.futurepages.util.The;

import java.io.File;
import java.util.List;

/**
 * Esta é uma CRUD Action. Para entender seu funcionamento,
 * estude o método execute da super classe org.futurepages.CrudActions.
 */
public class AlunoActions extends CrudActions {

    private Aluno aluno;
    private FileItem foto;


    /*
     * Lista de dependências necessárias para telas de criação e atualização.
     */
    @Override
    protected void listDependencies() {

	    //quando tem erro no formulário, para recarregar a tela, deve-se colocar novamente o objeto no output.
	    if(hasError()) {
		    output.setValue("aluno", aluno);
	    }

        List turmas = TurmaDao.listAll();
	    output("turmas", turmas);
    }

    /*
     * É a execução da ação de criação. quando chama-se Aluno.create.fpg
     */
	@Transactional
    public String create() throws Exception {
        validate(AlunoValidator.class).createOrUpdate(aluno);
        Dao.save(aluno);
		gravaFoto(aluno,foto);
        return success("Aluno criado com sucesso");
    }

	/*
		 * Ação de atualização do aluno.  Aluno.create.fpg
		*/
	@Transactional
	public String update() throws Exception {
		validate(AlunoValidator.class).createOrUpdate(aluno);
		Dao.update(aluno);
		gravaFoto(aluno,foto);
		return success("Aluno atualizado com sucesso.");
	}

	/*
	 * Ação de deleção. Aluno.delete.fpg
	 */
	@Transactional
	public String delete() {
		aluno = AlunoDao.getById(aluno.getId());
		Dao.delete(aluno);
		return success("Aluno excluído com sucesso.");
	}


	/*
	 * É a execução da filtragem.
	 *
	 * É executado quando chamamos Aluno.explore.fpg?turma=99
	 */
	public String explore(int turma) {

		List<Aluno> alunos = AlunoDao.listByTurmaId(turma);
		List<Turma> turmas = TurmaDao.listAll();


		output("alunos", alunos);
		output("turmas", turmas);
		output("turma", turma); //id da turma filtrada

		return SUCCESS;
	}


	private void gravaFoto(Aluno aluno, FileItem foto) throws Exception {
		if(foto!=null){
			String uploadsPath = Resources.getUploadsPath(PathTypeEnum.REAL);
			String path = The.concat(uploadsPath, "/alunos/", String.valueOf(aluno.getId()),".jpg");
			File file = new File(path);
			foto.write(file);
		}
	}

	/*
	 * listObjects() é a filtragem padrão da listagem.
	 * É o filtro default da listagem. Entenda quando é chamado em CrudActions.execute()
	 * É quando executado quando chama-se Aluno.fpg?type=explore
	 */
    @Override
    protected void listObjects() {
        explore(0);
    }

    /*
     * método que implementamos com a restauração do objeto do banco para a visão.
     */
    @Override
    protected void restoreObject() {
        aluno = AlunoDao.getById(aluno.getId());
        output("aluno", aluno);
    }
}
