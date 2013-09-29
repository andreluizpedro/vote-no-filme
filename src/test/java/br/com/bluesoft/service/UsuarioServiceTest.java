package br.com.bluesoft.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import javax.persistence.PersistenceException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import br.com.bluesoft.model.Filme;
import br.com.bluesoft.model.Usuario;
import br.com.bluesoft.provider.VotosUsuario;
import dbunit.DbUnitManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-test.xml"})
@TransactionConfiguration(transactionManager="transactionManager")
public class UsuarioServiceTest {
	
	private static final String FILMES = "src/test/resources/dbunit/xml/filmes.xml";

	@Autowired
	private DbUnitManager manager;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Before
	public void setUp() {
		manager.cleanAndInsert(FILMES);
	}
	
	@Test
	public void naoSalvaUsuarioSemNome() {
		Usuario usuario = new Usuario();
		try {
			usuarioService.salva(usuario);
			fail("Erro, salvando usuario sem nome");
		} catch (PersistenceException e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void naoSalvaUsuarioSemEmail() {
		Usuario usuario = new Usuario();
		usuario.setNome("fulano");
		try {
			usuarioService.salva(usuario);
			fail("Erro, salvando usuario sem email");
		} catch (PersistenceException e) {
			assertTrue(true);
		}
	}

	@Test
	@Transactional
	public void salvaUsuario() {
		Usuario usuario = new Usuario();
		usuario.setNome("fulanin");
		usuario.setEmail("fulanin@email.com");
		usuario.getFilmes().add(umFilme());
		usuarioService.salva(usuario);
		usuario = usuarioService.carregaPorId(usuario.getId());
		assertEquals("Erro ao salvar os filmes do usuario", 1, usuario.getFilmes().size());
	}

	@Test
	@Transactional
	public void salvaVotacao() {
		Usuario usuario = new Usuario();
		usuario.setNome("fulanin");
		usuario.setEmail("fulanin@email.com");
		
		VotosUsuario votosUsuario = new VotosUsuario();
		
		votosUsuario.adicionaVotacaoFilme(1l, 2l);
		usuarioService.salvaVotacao(votosUsuario, usuario);
		
		usuario = usuarioService.carregaPorId(usuario.getId());
		assertEquals("Erro ao salvar os filmes do usuario", 1, usuario.getFilmes().size());
	}
	
	@Test
	public void quandoExisteEmailUsuarioEntaoRetornaVerdadeiro() {
		String email = "fulano@email.com";
		boolean existe = usuarioService.existeEmailUsuario(email);
		assertTrue("Usuario com email esperado", existe);
	}
	
	@Test
	public void quandoNaoExisteEmailUsuarioEntaoRetornaFalso() {
		String email = "fulanin@email.com";
		boolean naoexiste = !usuarioService.existeEmailUsuario(email);
		assertTrue("Usuario com email esperado", naoexiste);
	}

	private Filme umFilme() {
		return criaFilme(6, "Depois da Terra");
	}

	private Filme criaFilme(long id, String titulo) {
		Filme filme = new Filme();
		filme.setId(id);
		filme.setTitulo(titulo);
		return filme;
	}

}
