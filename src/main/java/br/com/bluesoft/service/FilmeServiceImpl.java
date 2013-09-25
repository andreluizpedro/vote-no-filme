package br.com.bluesoft.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.bluesoft.model.Filme;

@Component
@Transactional
public class FilmeServiceImpl implements FilmeService {
	
	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@Override
	public List<Filme> carregaTodos() {
		Query consulta = ((Session) entityManager.getDelegate()).createQuery("from Filme");
		return consulta.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Filme> carregaDoisFilmes(List<Filme> list) {
		Session session = (Session) entityManager.getDelegate();
		Query consulta;
		if (list.isEmpty()) {
			consulta = session.createQuery("from Filme");
		} else {
			consulta = session.createQuery("from Filme f where f not in(:filmes)");
			consulta.setParameterList("filmes", list);
		}
		consulta.setMaxResults(2);
		return consulta.list();
	}

	@Override
	public Filme carregaPorId(Long filmeId) {
		return entityManager.find(Filme.class, filmeId);
	}

}
