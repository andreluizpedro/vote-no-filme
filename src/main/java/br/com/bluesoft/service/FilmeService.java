package br.com.bluesoft.service;

import java.util.List;

import br.com.bluesoft.model.Filme;

public interface FilmeService {

	List<Filme> carregaTodos();

	List<Filme> carregaDoisFilmes(List<Filme> list);

	Filme carregaPorId(long filmeId);

	Filme adicionaVoto(Long filmeId);

}
