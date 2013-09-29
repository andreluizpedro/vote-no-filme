package br.com.bluesoft.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.bluesoft.dao.UsuarioDao;
import br.com.bluesoft.model.Filme;
import br.com.bluesoft.model.Usuario;
import br.com.bluesoft.provider.VotosUsuario;

@Component
@Transactional
public class UsuarioServiceImpl implements UsuarioService {
	
	@Autowired
	public UsuarioDao usuarioDao;
	
	@Autowired
	private FilmeService filmeService;

	public void salva(Usuario usuario) {
		usuarioDao.salva(usuario);
	}

	@Override
	public Usuario carregaPorId(Long id) {
		return usuarioDao.carregaPorId(id);
	}

	@Override
	public void salvaVotacao(VotosUsuario votosUsuario, Usuario usuario) {
		List<Long> filmeIds = votosUsuario.getFilmeIdVotados();
		for (Long filmeId : filmeIds) {
			Filme filme = filmeService.carregaPorId(filmeId);
			if (filme != null) {
				usuario.getFilmes().add(filme);
				filme.getUsuarios().add(usuario);
			}
		}
		usuarioDao.salva(usuario);
		votosUsuario.resetarVotacao();
	}

	@Override
	public boolean existeEmailUsuario(String email) {
		return usuarioDao.existeEmailUsuario(email);
	}

	
}
