package modules.escola.beans;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name="escola_turma")
public class Turma  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 10, unique = true, nullable = false)
    private String codigo;

    @Column(nullable = false)
    private String nome;

    @ManyToOne(cascade = CascadeType.ALL,optional = false)
	private TipoTurma tipo;

    public Turma() {
    }

    public Turma(String codigo, String nome) {
        setCodigo(codigo);
        setNome(nome);
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodigo() {
        return codigo;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

	public TipoTurma getTipo() {
		return tipo;
	}

	public void setTipo(TipoTurma tipo) {
		this.tipo = tipo;
	}
}
