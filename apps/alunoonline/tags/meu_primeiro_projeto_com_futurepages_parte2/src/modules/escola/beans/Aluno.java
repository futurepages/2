package modules.escola.beans;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "escola_aluno")
public class Aluno implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(length = 10, unique = true, nullable = false)
	private String matricula;

    @Column(nullable = false)
	private String nomeCompleto;

    //@Column(name = "turmaId")
    //private int turmaId;
    @ManyToOne
    private Turma turma;

	public Aluno() {
	}

	public Aluno(String matricula, String nomeCompleto) {
		this.matricula = matricula;
		this.nomeCompleto = nomeCompleto;
	}

    public Aluno(String matricula, String nomeCompleto, Turma turma) {
        this.matricula = matricula;
        this.nomeCompleto = nomeCompleto;
        this.turma = turma;
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getNomeCompleto() {
		return nomeCompleto;
	}

	public void setNomeCompleto(String nomeCompleto) {
		this.nomeCompleto = nomeCompleto;
	}

    public Turma getTurma() {
        return turma;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }
}
