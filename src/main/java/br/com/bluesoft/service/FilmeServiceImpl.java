package br.com.bluesoft.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.bluesoft.dao.FilmeDao;
import br.com.bluesoft.model.Filme;
import br.com.bluesoft.model.Usuario;
import br.com.bluesoft.provider.VotosUsuario;

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
	public Filme carregaPorId(long filmeId) {
		return filmeDao.carregaPorId(filmeId);
	}
	
	@Override
	public List<Filme> carregaRanking() {
		List<Filme> ranking = filmeDao.carregaRanking();
		classificaRanking(ranking);
		return ranking;
	}

	private void classificaRanking(List<Filme> ranking) {
		for (int i = 0; i < ranking.size(); i++) {
			Filme filme = ranking.get(i);
			filme.setRanking((long) (i + 1));
		}
	}

	@Override
	public List<Filme> carregaDoisFilmes(VotosUsuario votosUsuario) {
		if (votosUsuario.ehCombinacoesVazia()) {
			List<Filme> filmes = filmeDao.carregaTodos();
			Long[] filmeIds = getFilmeIds(filmes);
			votosUsuario.gerarCombinacoes(filmeIds);
		}
		List<Filme> filmeVotacao = Collections.emptyList();
		Long[] filmeIds = votosUsuario.proximaCombinacao();
		if (filmeIds != null) {
			Filme a = filmeDao.carregaPorId(filmeIds[0]);
			Filme b = filmeDao.carregaPorId(filmeIds[1]);
			filmeVotacao = Arrays.asList(a, b);
		}
		return filmeVotacao;
	}

	@Override
	public List<Filme> carregaRanking(Usuario usuario) {
		List<Filme> ranking = filmeDao.carregaRanking(usuario);
		classificaRanking(ranking);
		return ranking;
	}

	private Long[] getFilmeIds(List<Filme> filmes) {
		Collections.sort(filmes);
		Long[] filmeIds = new Long[filmes.size()];
		int i = 0;
		for (Filme filme : filmes) {
			filmeIds[i++] = filme.getId();
		}
		return filmeIds;
	}

}
