package br.com.bluesoft.service;

import java.util.List;

import br.com.bluesoft.model.Filme;
import br.com.bluesoft.model.Usuario;
import br.com.bluesoft.provider.VotosUsuario;

public interface FilmeService {

	List<Filme> carregaTodos();

	Filme carregaPorId(long filmeId);

	List<Filme> carregaRanking();

	List<Filme> carregaDoisFilmes(VotosUsuario votosUsuario);

	List<Filme> carregaRanking(Usuario usuario);

}
