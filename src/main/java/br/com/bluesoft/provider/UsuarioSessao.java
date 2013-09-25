package br.com.bluesoft.provider;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import br.com.bluesoft.model.Filme;
import br.com.caelum.vraptor.ioc.SessionScoped;

@Component
@SessionScoped
public class UsuarioSessao {

	private Set<Filme> filmesVotados;
	
	public Set<Filme> getFilmesVotados() {
		if (filmesVotados == null) {
			filmesVotados = new HashSet<>();
		}
		return filmesVotados;
	}
	
}
