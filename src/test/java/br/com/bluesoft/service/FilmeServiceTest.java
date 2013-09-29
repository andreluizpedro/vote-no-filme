package br.com.bluesoft.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import br.com.bluesoft.model.Filme;
import br.com.bluesoft.model.Usuario;
import br.com.bluesoft.provider.VotosUsuario;
import dbunit.DbUnitManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-test.xml"})
@TransactionConfiguration(transactionManager="transactionManager")
public class FilmeServiceTest {
	
	private static final String FILMES = "src/test/resources/dbunit/xml/filmes.xml";

	@Autowired
	private DbUnitManager manager;
	
	@Autowired
	private FilmeService filmeService;

	private VotosUsuario votosUsuario;
	
	@Before
	public void setUp() {
		manager.cleanAndInsert(FILMES);
		votosUsuario = new VotosUsuario();
	}

	@Test
	public void carregaTodosOsFilmes() {
		List<Filme> filmes = filmeService.carregaTodos();
		assertEquals(todosOsFilmes(), filmes);
	}
	
	@Test
	public void carregaDoisPrimeirosFilmes() {
		List<Filme> filmes = filmeService.carregaDoisFilmes(votosUsuario);
		assertEquals(doisPrimeirosFilmes().toString(), filmes.toString());
	}
	
	@Test
	public void carregaFilmesAposEscolherOPrimeiro() {
		votosUsuario.adicionaVotacaoFilme(1l, 2l);
		System.out.println(Arrays.toString(votosUsuario.getVotacaoFilmes().get(0)));
		List<Filme> filmes = filmeService.carregaDoisFilmes(votosUsuario);
		assertEquals(primeiroETerceiroFilme().toString(), filmes.toString());
	}
	
	@Test
	public void carregaPrimeiroFilmePorId() {
		long filmeId = 1;
		Filme primeiroFilme = filmeService.carregaPorId(filmeId);
		assertEquals(primeiroFilmeEscolhido().get(0), primeiroFilme);
	}
	
	@Test
	public void filmeNaoEncontrado() {
		long filmeId = 0;
		Filme primeiroFilme = filmeService.carregaPorId(filmeId);
		assertNull(primeiroFilme);
	}
	
	@Test
	public void carregaRakingGeral() {
		List<Filme> filmes = filmeService.carregaRanking();
		assertEquals(todosOsFilmes(), filmes);
	}
	
	@Test
	public void carregaRakingUsuario() {
		Usuario usuario = new Usuario();
		usuario.setId(1l);
		List<Filme> filmes = filmeService.carregaRanking(usuario);
		assertEquals(rankingDosFilmesDoUsuario().toString(), filmes.toString());
	}

	private List<Filme> rankingDosFilmesDoUsuario() {
		List<Filme> filmes = new ArrayList<>();
		filmes.add(criaFilme(1, "Depois da Terra", 2));
		filmes.add(criaFilme(2, "Meu Malvado Favorito 2", 1));
		return filmes;
	}

	private List<Filme> todosOsFilmes() {
		List<Filme> filmes = new ArrayList<>();
		filmes.add(criaFilme(1, "Depois da Terra"));
		filmes.add(criaFilme(2, "Meu Malvado Favorito 2"));
		filmes.add(criaFilme(3, "Velozes & Furiosos 6"));
		filmes.add(criaFilme(4, "O Senhor dos Anéis - O Retorno do Rei"));
		filmes.add(criaFilme(5, "Os Vingadores - The Avengers"));
		return filmes;
	}
	
	private List<Filme> doisPrimeirosFilmes() {
		List<Filme> filmes = new ArrayList<>();
		filmes.add(criaFilme(1, "Depois da Terra"));
		filmes.add(criaFilme(2, "Meu Malvado Favorito 2"));
		return filmes;
	}
	
	private List<Filme> primeiroFilmeEscolhido() {
		List<Filme> filmes = new ArrayList<>();
		filmes.add(criaFilme(1, "Depois da Terra"));
		return filmes;
	}

	private List<Filme> primeiroETerceiroFilme() {
		List<Filme> filmes = new ArrayList<>();
		filmes.add(criaFilme(1, "Depois da Terra"));
		filmes.add(criaFilme(3, "Velozes & Furiosos 6"));
		return filmes;
	}

	private Filme criaFilme(long id, String titulo) {
		return criaFilme(id, titulo, 0);
	}
	
	private Filme criaFilme(long id, String titulo, long votos) {
		Filme filme = new Filme();
		filme.setId(id);
		filme.setTitulo(titulo);
		filme.setVotos(votos);
		return filme;
	}

}
