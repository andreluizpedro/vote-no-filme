package br.com.bluesoft.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import br.com.bluesoft.model.Usuario;

@Component
public class UsuarioDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	public Session getSession() {
		return (Session) entityManager.getDelegate();
	}

	public void salva(Usuario usuario) {
		entityManager.persist(usuario);
	}

	public Usuario carregaPorId(long id) {
		return entityManager.find(Usuario.class, id);
	}

	public boolean existeEmailUsuario(String email) {
		String hql =
				"select u.id from Usuario u where u.email = :email";
		Query consulta = getSession().createQuery(hql);
		consulta.setParameter("email", email);
			
		return consulta.uniqueResult() != null;

	}
}
