package modules.escola.beans;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "escola_aluno")
public class Aluno implements Serializable {

	@Id
	private int matricula;
	
	@Column(length = 10, unique = true, nullable = false)
	private String cpf;

    @Column(length = 120, nullable = false)
	private String nomeCompleto;

	public Aluno() {
	}

	public Aluno(int matricula, String cpf, String nomeCompleto) {
		this.matricula = matricula;
		this.cpf = cpf;
		this.nomeCompleto = nomeCompleto;
	}

	public int getMatricula() {
		return matricula;
	}

	public void setMatricula(int id) {
		this.matricula = id;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getNomeCompleto() {
		return nomeCompleto;
	}

	public void setNomeCompleto(String nomeCompleto) {
		this.nomeCompleto = nomeCompleto;
	}
}