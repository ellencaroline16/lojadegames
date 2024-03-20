package loja.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tb_produtos")
public class Produto {

//			CREATE TABLE tb_produtos(
//			ID bigint auto_increment,
//			Titulo varchar (255) not null,
//			Valor decimal,
//			Ano_de_lancamento date,
//			Plataforma varchar (255) not null,
//			categoria_ID bigint not null, - Perguntar p/ Camis 
//			primary key(ID),Perguntar p/ Camis 
//			foreign key (categoria_ID)references tb_categoria(ID)
//			); Perguntar p/ Camis 

	// 1º os atributos | 2º as validações/anotações

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = " O atributo título é obrigatório!")
	private String titulo;

	@Size(min = 2, max = 4, message = " O atributo valor deve conter no mínimo 2 e no máximo 4 dígitos!")
	private double valor;

	@NotBlank(message = " O atributo ano de lançamento é obrigatório!")
	private String ano_de_lancamento;

	@NotBlank(message = " O atributo plataforma é obrigatório!")
	private String plataforma;

	@NotBlank
	private Integer categoria_id;
	// Perguntar p/ Camis

	@ManyToOne
	@JsonIgnoreProperties("categoria")
	private Tema tema;

	@ManyToOne
	@JsonIgnoreProperties("usuario")
	private Usuario usuario;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public String getAno_de_lancamento() {
		return ano_de_lancamento;
	}

	public void setAno_de_lancamento(String ano_de_lancamento) {
		this.ano_de_lancamento = ano_de_lancamento;
	}

	public String getPlataforma() {
		return plataforma;
	}

	public void setPlataforma(String plataforma) {
		this.plataforma = plataforma;
	}

	public Integer getCategoria_id() {
		return categoria_id;
	}

	public void setCategoria_id(Integer categoria_id) {
		this.categoria_id = categoria_id;
	}

	public Tema getTema() {
		return tema;
	}

	public void setTema(Tema tema) {
		this.tema = tema;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
