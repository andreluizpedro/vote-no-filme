package br.com.bluesoft.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import dbunit.DbUnitManager;
import br.com.bluesoft.model.Filme;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-test.xml"})
@TransactionConfiguration(transactionManager="transactionManager")
public class FilmeServiceTest {
	
	private static final String FILMES = "src/test/resources/dbunit/xml/filmes.xml";

	@Autowired
	private DbUnitManager manager;
	
	@Autowired
	private FilmeService filmeService;
	
	@Before
	public void setUp() {
		manager.cleanAndInsert(FILMES);
	}

	@Test
	public void carregaTodosOsFilmes() {
		List<Filme> filmes = filmeService.carregaTodos();
		assertEquals(todosOsFilmes(), filmes);
	}
	
	@Test
	public void carregaDoisPrimeirosFilmes() {
		List<Filme> filmes = filmeService.carregaDoisFilmes(Collections.<Filme>emptyList());
		assertEquals(doisPrimeirosFilmes(), filmes);
	}
	
	@Test
	public void carregaFilmesAposEscolherOPrimeiro() {
		List<Filme> filmes = filmeService.carregaDoisFilmes(primeiroFilmeEscolhido());
		assertEquals(segundoETerceiroFilme(), filmes);
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

	private List<Filme> segundoETerceiroFilme() {
		List<Filme> filmes = new ArrayList<>();
		filmes.add(criaFilme(2, "Meu Malvado Favorito 2"));
		filmes.add(criaFilme(3, "Velozes & Furiosos 6"));
		return filmes;
	}

	private Filme criaFilme(long id, String nome) {
		return new Filme(id, nome);
	}

}
