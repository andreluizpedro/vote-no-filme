package br.com.bluesoft.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Component;

import br.com.bluesoft.model.Filme;
import br.com.bluesoft.model.Usuario;

@Component
public class FilmeDao {

	@PersistenceContext
	private EntityManager entityManager;

	public Session getSession() {
		return (Session) entityManager.getDelegate();
	}

	@SuppressWarnings("unchecked")
	public List<Filme> carregaTodos() {
		Query consulta = getSession().createQuery("from Filme");
		return consulta.list();
	}

	public Filme carregaPorId(long filmeId) {
		return entityManager.find(Filme.class, filmeId);
	}
	
	@SuppressWarnings("unchecked")
	public List<Filme> carregaRanking() {
		String hql =
				" select                                       " + 
				"   filme.id as id,                            " +
				"   filme.titulo as titulo,                    " +
				"   filme.capa as capa,                        " +
				"   cast(filme.usuarios.size as long) as votos " +
				" from Filme filme                             " +
				" group by                                     " +
				"   id,                                        " +
				"   titulo,                                    " +
				"   capa                                       " +
				" order by votos desc, id                      " ;
		Query consulta = getSession().createQuery(hql);
		consulta.setResultTransformer(
				new AliasToBeanResultTransformer(Filme.class));
			
		return consulta.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Filme> carregaRanking(Usuario usuario) {
		String hql =
				" select                           " + 
				"   filme.id as id,                " +
				"   filme.titulo as titulo,        " +
				"   filme.capa as capa,            " +
				"   count(usuario) as votos        " +
				" from Filme filme                 " +
				" left join filme.usuarios usuario " +
				" where                            " +
				" 	usuario =:usuario              " +
				" group by                         " +
				"   filme.id,                      " +
				"   filme.titulo,                  " +
				"   filme.capa                     " +
				" order by votos desc, id          ";
		Query consulta = getSession().createQuery(hql);
		consulta.setParameter("usuario", usuario);
		consulta.setResultTransformer(
				new AliasToBeanResultTransformer(Filme.class));
			
		return consulta.list();
	}

}
