package br.com.bluesoft.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;


/**
 * @author andre
 *
 */
@Entity
public class Filme implements Comparable<Filme> {
	
	@Id
	private Long id;
	
	@Column(nullable=false)
	private String titulo;
	private String capa;
	@Transient
	private Long ranking = 0l;
	@Transient
	private Long votos = 0l;
	
	
	@ManyToMany
	@JoinTable(name = "FILME_USUARIO", joinColumns = { 
			@JoinColumn(name = "FILME_ID", nullable = false, updatable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "USUARIO_ID", 
					nullable = false, updatable = false) })
	private List<Usuario> usuarios;

	public Filme() {}
	
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
	
	public String getCapa() {
		return capa;
	}

	public void setCapa(String capa) {
		this.capa = capa;
	}

	public Long getVotos() {
		return votos;
	}

	public void setVotos(Long votos) {
		this.votos = votos;
	}

	public Long getRanking() {
		return ranking;
	}

	public void setRanking(Long ranking) {
		this.ranking = ranking;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((titulo == null) ? 0 : titulo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Filme other = (Filme) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (titulo == null) {
			if (other.titulo != null)
				return false;
		} else if (!titulo.equals(other.titulo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Filme [id=" + id + ", titulo=" + titulo
				+ ", votos=" + votos + "]";
	}

	@Override
	public int compareTo(Filme outro) {
		if (outro == null || outro.id == null)
			return 1;
		if (id == null) {
			return -1;
		} 
		return id.compareTo(outro.id);
	}

}
