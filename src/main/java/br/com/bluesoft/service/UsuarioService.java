package br.com.bluesoft.service;

import br.com.bluesoft.model.Usuario;
import br.com.bluesoft.provider.VotosUsuario;

public interface UsuarioService {

	void salva(Usuario usuario);

	Usuario carregaPorId(Long id);

	void salvaVotacao(VotosUsuario votosUsuario, Usuario usuario);

	boolean existeEmailUsuario(String email);

}
