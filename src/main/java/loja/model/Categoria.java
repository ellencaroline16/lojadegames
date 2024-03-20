package loja.model; // a referência da tabela

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity // quem cria a tabela no banco de dados
@Table(name = "tb_categoria")
public class Categoria {

//	Model - chefe de cozinha 
//	Repository - Estoque 
//	Controller - Garçom
//	Client - Cozinha

//	CREATE TABLE tb_categoria(
//	ID bigint auto_increment,
//	Genero varchar (255) not null,
//	Faixa_etaria int not null,
//	primary key (ID)
//	);

	// as anotações são facilitadores, códigos prontos, validações.

	@Id // o que tem em comum nas tabelas e será a chave primária igual no sql mirmã
	@GeneratedValue(strategy = GenerationType.IDENTITY) // tô dizendo aqui q a estratégia é criar a chave primária como
														// auto incremento, igual quinem o sql
	private Long id;

	// tudo isso ai de cima é p/ criar o ID lá no MySQL (palhaçada)

	@NotBlank(message = " O atributo gênero é obrigatório!")
	// não pode ser nulo, tem que ter algo
	private String genero;

	@Size(min = 1, max = 3, message = " O atributo faixa etária deve conter no mínimo 1 e no máximo 3 dígitos!")
	private Integer faixa_etaria;
	// Integer é uma classe, igual o String, sempre com letra Maiúscula

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public Integer getFaixa_etaria() {
		return faixa_etaria;
	}

	public void setFaixa_etaria(Integer faixa_etaria) {
		this.faixa_etaria = faixa_etaria;
	}

	public Tema getTema() {
		return tema;
	}

	public void setTema(Tema tema) {
		this.tema = tema;
	}

	@ManyToOne
	@JsonIgnoreProperties("categoria")
	private Tema tema;

}
