package br.com.bluesoft.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import br.com.bluesoft.model.Filme;

@Component
public class FilmeDao {

	@PersistenceContext
	private EntityManager entityManager;

	public Filme atualiza(Filme filme) {
		return entityManager.merge(filme);
	}

	public Session getSession() {
		return (Session) entityManager.getDelegate();
	}

	@SuppressWarnings("unchecked")
	public List<Filme> carregaTodos() {
		Query consulta = getSession().createQuery("from Filme");
		return consulta.list();
	}

	@SuppressWarnings("unchecked")
	public List<Filme> carregaDoisFilmes(List<Filme> list) {
		Query consulta;
		if (list.isEmpty()) {
			consulta = getSession().createQuery("from Filme");
		} else {
			consulta = getSession().createQuery("from Filme f where f not in(:filmes)");
			consulta.setParameterList("filmes", list);
		}
		consulta.setMaxResults(2);
		return consulta.list();
	}

	public Filme carregaPorId(long filmeId) {
		return entityManager.find(Filme.class, filmeId);
	}

}
