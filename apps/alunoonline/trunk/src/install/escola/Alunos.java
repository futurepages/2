package install.escola;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import install.Resources;
import modules.escola.beans.Turma;

import java.util.Random;

import modules.escola.beans.Aluno;
import org.futurepages.core.install.Installation;
import org.futurepages.core.persistence.Dao;
import org.futurepages.enums.PathTypeEnum;
import org.futurepages.util.FileUtil;

/*
 * Instala alunos de exemplo.
 */
public class Alunos implements Installation {

	private List<Turma> listaTurma;
	private Random rand = new Random(System.currentTimeMillis());

	public Alunos() {
	}

	public Alunos(List<Turma> listaTurma) {
		this.listaTurma = listaTurma;
	}

	private int modulo(int n) {
		return ((n < 0) ? (-n) : n);
	}

	private Turma obtemTurmaRandomica() {
		return listaTurma.get(modulo(rand.nextInt()) % listaTurma.size());
	}

	/*
	 * Instala os alunos, alguns com turmas outros sem turmas.
	 * As turmas são escolhidas aletariamente.
	 */
	public void execute() throws IOException {

		//Alunos com turma...
		installAluno("03N1047001", "Mario da Costa", obtemTurmaRandomica());
		installAluno("02N1047452", "Fulano Novato Cavalcante Soares", obtemTurmaRandomica());
		installAluno("99S5447053", "Antônio Alves da Nóbrega", obtemTurmaRandomica());
		installAluno("99S5000004", "Sicrano Spenser", obtemTurmaRandomica());
		installAluno("99S8348345", "Maria Rozilda Olivié", obtemTurmaRandomica());
		installAluno("93E9394326", "José Pampis Pormenores", obtemTurmaRandomica());

		//Alunos sem turma...
		installAluno("03N1047000", "Maria da Silva");
		installAluno("02N1047451", "João Fulano Cavalcante Soares");
		installAluno("99S5447050", "Antônio Feliciano Gomes");
		installAluno("99S5000001", "Sicrano da Silva e Silva");
		installAluno("99S8348349", "Maria Antonieta da Costa");
		installAluno("93E9394322", "José Pompeu Exemplus da Silva");
	}

	private void installAluno(String matricula, String nome) throws IOException {
		installAluno(matricula,nome,null);
	}

	private void installAluno(String matricula, String nome, Turma turma) throws IOException {
		Aluno aluno = new Aluno(matricula, nome);
		if(turma!=null){
			aluno.setTurma(turma);
		}
		Dao.save(aluno);
		FileUtil.copy(FileUtil.classRealPath(this.getClass()) + "res/" + aluno.getId() + ".jpg", Resources.getUploadsPath(PathTypeEnum.REAL) + "/alunos/" + aluno.getId()+".jpg");
	}
}
