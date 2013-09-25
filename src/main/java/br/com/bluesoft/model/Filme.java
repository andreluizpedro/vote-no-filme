package br.com.bluesoft.model;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class Filme implements Comparable<Filme> {
	
	@Id
	private Long id;
	
	private String titulo;
	private String capa;

	public Filme() {}
	
	public Filme(String titulo) {
		this.titulo = titulo;
	}

	public Filme(Long id, String titulo) {
		this.id = id;
		this.titulo = titulo;
	}

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
		return "Filme [titulo=" + titulo + "]";
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
