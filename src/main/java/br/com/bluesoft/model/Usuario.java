package br.com.bluesoft.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;

/**
 * @author andre
 *
 */
@Entity
public class Usuario {

	@Id
	@SequenceGenerator(name = "seq_usuario_id", sequenceName = "seq_usuario_id")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_usuario_id")
	private Long id;

	@Column(nullable = false)
	private String nome;
	@Column(unique = true, nullable = false)
	private String email;
	@ManyToMany(mappedBy = "usuarios", cascade = {CascadeType.ALL})
	private List<Filme> filmes;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Filme> getFilmes() {
		if (filmes == null) {
			filmes = new ArrayList<>();
		}
		return filmes;
	}

	public void setFilmes(List<Filme> filmes) {
		this.filmes = filmes;
	}

}
