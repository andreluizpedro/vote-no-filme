package br.com.bluesoft.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.bluesoft.dao.FilmeDao;
import br.com.bluesoft.model.Filme;

@Component
@Transactional
public class FilmeServiceImpl implements FilmeService {
	
	@Autowired
	public FilmeDao filmeDao;
	
	@Override
	public List<Filme> carregaTodos() {
		return filmeDao.carregaTodos();
	}

	@Override
	public List<Filme> carregaDoisFilmes(List<Filme> list) {
		return filmeDao.carregaDoisFilmes(list);
	}

	@Override
	public Filme carregaPorId(long filmeId) {
		return filmeDao.carregaPorId(filmeId);
	}
	
	@Override
	public Filme adicionaVoto(Long filmeId) {
		Filme filme = carregaPorId(filmeId);
		if (filme != null) {
			filme.adicionaVoto();
			filme = filmeDao.atualiza(filme);
		}
		
		return filme;
	}

}
