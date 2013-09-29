package br.com.bluesoft.provider;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class VotosUsuarioTest {
	
	private static final Long USADA = 1L;
	private VotosUsuario votosUsuario;
	
	@Before
	public void setUp() {
		votosUsuario = new VotosUsuario();
	}

	@Test
	public void adicionaVotacaoFilme() {
		Long filmeId = 1l;
		Long filmeIdNaoVotado = 2l;
		Long[][] votacao = {{filmeId, filmeIdNaoVotado}};
		votosUsuario.adicionaVotacaoFilme(filmeId, filmeIdNaoVotado);
		
		assertTrue(votosUsuario.ehCombinacoesVazia());
		
		assertArrayEquals(votacao, votosUsuario.getVotacaoFilmes().toArray());
	}
	
	@Test
	public void adicionaVotacaoFilmeComOsMesmosFilmes() {
		Long filmeId = 1l;
		Long filmeIdNaoVotado = 2l;
		Long[][] votacao = {{filmeId, filmeIdNaoVotado}};
		votosUsuario.gerarCombinacoes(votacao[0]);
		votosUsuario.adicionaVotacaoFilme(filmeId, filmeIdNaoVotado);
		votosUsuario.adicionaVotacaoFilme(filmeId, filmeIdNaoVotado);
		
		assertArrayEquals(votacao, votosUsuario.getVotacaoFilmes().toArray());
	}
	
	@Test
	public void adicionaVotacaoFilmeComOsMesmosFilmesDeOrdemInversa() {
		Long filmeId = 1l;
		Long filmeIdNaoVotado = 2l;
		Long[][] votacao = {{filmeId, filmeIdNaoVotado}};
		votosUsuario.gerarCombinacoes(votacao[0]);
		votosUsuario.adicionaVotacaoFilme(filmeId, filmeIdNaoVotado);
		votosUsuario.adicionaVotacaoFilme(filmeIdNaoVotado, filmeId);
		
		assertArrayEquals(votacao, votosUsuario.getVotacaoFilmes().toArray());
	}

	@Test
	public void proximaCombinacao() {
		Long[] valores = {1l, 2l, 3l};
		votosUsuario.gerarCombinacoes(valores);
		Long[] combinacao = votosUsuario.proximaCombinacao();
		
		Long[] esperado = {1l, 2l, USADA};
		assertArrayEquals(esperado, combinacao);
	}
	
	@Test
	public void duasProximaCombinacao() {
		Long[] valores = {1l, 2l, 3l};
		votosUsuario.gerarCombinacoes(valores);
		Long[] esperado;
		Long[] combinacao;
		
		esperado = new Long[]{1l, 2l, USADA};
		combinacao = votosUsuario.proximaCombinacao();
		assertArrayEquals(esperado, combinacao);
		
		esperado = new Long[]{1l, 3l, USADA};
		combinacao = votosUsuario.proximaCombinacao();
		assertArrayEquals(esperado, combinacao);
	}
	
	@Test
	public void proximaCombinacaoAteNaoExistirMais() {
		Long[] valores = {1l, 2l, 3l};
		votosUsuario.gerarCombinacoes(valores);
		Long[] esperado;
		Long[] combinacao;
		
		boolean temCombincao;
		
		temCombincao = votosUsuario.temCombinacao();
		assertTrue(temCombincao);
		
		esperado = new Long[]{1l, 2l, USADA};
		combinacao = votosUsuario.proximaCombinacao();
		assertArrayEquals(esperado, combinacao);
		
		esperado = new Long[]{1l, 3l, USADA};
		combinacao = votosUsuario.proximaCombinacao();
		assertArrayEquals(esperado, combinacao);
		
		
		esperado = new Long[]{2l, 3l, USADA};
		combinacao = votosUsuario.proximaCombinacao();
		assertArrayEquals(esperado, combinacao);
		
		
		combinacao = votosUsuario.proximaCombinacao();
		
		assertNull(combinacao);
		
		temCombincao = votosUsuario.temCombinacao();
		assertFalse(temCombincao);
	}

	@Test
	public void quandoGetFilmeIdVotados() {
		Long[] valores = {1l, 2l, 3l};
		Long filmeId = 2l;
		Long filmeIdNaoVotado = 1l;
		Long[] votos = {filmeId};
		
		votosUsuario.gerarCombinacoes(valores);
		votosUsuario.adicionaVotacaoFilme(filmeId, filmeIdNaoVotado);
		
		List<Long> votados = votosUsuario.getFilmeIdVotados();
		assertArrayEquals(votos, votados.toArray());
	}

	@Test
	public void quandoResetarVotacao() {
		Long[] valores = {1l, 2l, 3l};
		Long filmeId = 2l;
		Long filmeIdNaoVotado = 1l;
		Long[] votos = {filmeId};
		
		votosUsuario.gerarCombinacoes(valores);
		votosUsuario.adicionaVotacaoFilme(filmeId, filmeIdNaoVotado);
		
		
		List<Long> votados;
		
		votados = votosUsuario.getFilmeIdVotados();
		assertArrayEquals(votos, votados.toArray());
		
		votosUsuario.resetarVotacao();
		
		votos = new Long[]{};
		
		votados = votosUsuario.getFilmeIdVotados();
		assertArrayEquals(votos, votados.toArray());
	}

}
