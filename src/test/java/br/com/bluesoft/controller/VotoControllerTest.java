package br.com.bluesoft.controller;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import util.JsonSerializer;
import br.com.bluesoft.dto.Rankings;
import br.com.bluesoft.model.Filme;
import br.com.bluesoft.model.Usuario;
import br.com.bluesoft.provider.VotosUsuario;
import br.com.bluesoft.service.FilmeService;
import br.com.bluesoft.service.UsuarioService;
import br.com.caelum.vraptor.util.test.MockSerializationResult;

public class VotoControllerTest {
	
	private static final String MENSAGEM = "mensagem";

	private static final String ERRO = "erro";

	private VotoController controller;
	
	private MockSerializationResult result;
	
	private JsonSerializer serializer;
	
	@Mock
	private VotosUsuario votosUsuario;
	@Mock
	private FilmeService filmeService;
	
	@Mock
	private UsuarioService usuarioService;

	@Mock
	private HttpSession session;
	
	@Before
	public void setUp() {
		serializer = new JsonSerializer();
		result = new MockSerializationResult();
		MockitoAnnotations.initMocks(this);
		controller = new VotoController(result, votosUsuario, filmeService, usuarioService, session);
		when(filmeService.carregaDoisFilmes(votosUsuario)).thenReturn(todosOsFilmes());
	}

	@Test
	public void existePaginaDeVoto() {
		controller.index();
	}
	
	@Test
	public void quandoOcorreAlgumErroEntaoRetornaUmaMensage() throws Exception {
		doThrow(new RuntimeException("Erro interno do servidor")).when(filmeService).carregaDoisFilmes(votosUsuario);
		controller.carregaDoisFilmes();
		assertEquals(serializer.serialize(ERRO,"Erro interno do servidor"), result.serializedResult());
	}
	
	@Test
	public void quandoCarregaEExisteDadosEntaoRetornaTodosOsFilmes() throws Exception {
		controller.carregaDoisFilmes();
		assertEquals(serializer.serialize("filmes",todosOsFilmes()), result.serializedResult());
	}
	
	@Test
	public void quandoCarregaENaoExisteDadosEntaoRetornaUmaMensagem() throws Exception {
		when(filmeService.carregaDoisFilmes(votosUsuario)).thenReturn(Collections.<Filme>emptyList());
		when(votosUsuario.ehVotacaoEncerrada()).thenReturn(true);
		controller.carregaDoisFilmes();
		assertEquals(serializer.serialize(MENSAGEM,"votacao.encerrada"), result.serializedResult());
	}
	
	@Test
	public void quandoCarregaENaoTemMaisCombinacoesEntaoRetornaUmaMensagem() throws Exception {
		when(filmeService.carregaDoisFilmes(votosUsuario)).thenReturn(Collections.<Filme>emptyList());
		when(votosUsuario.ehVotacaoEncerrada()).thenReturn(false);
		controller.carregaDoisFilmes();
		assertEquals(serializer.serialize(MENSAGEM,"sem.filmes"), result.serializedResult());
	}

	
	@Test
	public void quandoVotaNumFilmeEntaoRetornaSucesso() throws Exception {
		Long filmeId = 1l;
		Long filmeIdNaoVotado = 2l;
		when(filmeService.carregaPorId(filmeId)).thenReturn(primeiroFilme());
		controller.votar(filmeId, filmeIdNaoVotado);
		assertEquals(serializer.serialize(MENSAGEM,"filme.voltado.sucesso"), result.serializedResult());
	}
	
	@Test
	public void quandoVotaNumFilmeEOcorreAlgumErroEntaoRetornaMensagem() throws Exception {
		Long filmeId = 1l;
		Long filmeIdNaoVotado = 2l;
		doThrow(new RuntimeException("Erro interno do servidor"))
		.when(votosUsuario).adicionaVotacaoFilme(filmeId, filmeIdNaoVotado);
		controller.votar(filmeId, filmeIdNaoVotado);
		assertEquals(serializer.serialize(ERRO,"Erro interno do servidor"), result.serializedResult());
	}
	
	@Test
	public void quandoConfirmaVotacaoComAlgumFilmeVotadoEntaoRetornaSucesso() throws Exception {
		when(votosUsuario.getVotacaoFilmes()).thenReturn(primeiraVotacao());
		Usuario usuario = new Usuario();
		controller.confirmaVotacao(usuario);
		Rankings rankings = new Rankings();
		rankings.rankings = Collections.emptyList();
		rankings.rankingUsuario = Collections.emptyList();
		rankings.mensagem = "usuario.salvo.sucesso";
		assertEquals(serializer.withoutRoot().serialize(rankings), result.serializedResult());
	}
	
	@Test
	public void quandoConfirmaVotacaoComNenhumFilmeVotadoEntaoRetornaMensagem() throws Exception {
		when(votosUsuario.getFilmeIdVotados()).thenReturn(Collections.<Long>emptyList());
		Usuario usuario = new Usuario();
		controller.confirmaVotacao(usuario);
		assertEquals(serializer.serialize(MENSAGEM, "nenhum.filme.voltado"), result.serializedResult());
	}
	
	@Test
	public void quandoConfirmaVotacaoEOcorreAlgumErroEntaoRetornaMensagem() throws Exception {
		when(votosUsuario.getVotacaoFilmes()).thenReturn(primeiraVotacao());
		doThrow(new RuntimeException("Erro interno do servidor"))
		.when(usuarioService).salvaVotacao(any(VotosUsuario.class), any(Usuario.class));
		Usuario usuario = new Usuario();
		controller.confirmaVotacao(usuario);
		assertEquals(serializer.serialize(ERRO,"Erro interno do servidor"), result.serializedResult());
	}
	
	@Test
	public void quandoConfirmaVotacaoEJaTemEmailCadastradoEntaoRetornaMensagem() throws Exception {
		when(usuarioService.existeEmailUsuario(any(String.class))).thenReturn(true);
		Usuario usuario = new Usuario();
		controller.confirmaVotacao(usuario);
		assertEquals(serializer.serialize(MENSAGEM,"usuario.ja.cadastrado"), result.serializedResult());
	}
	
	@Test
	public void quandoResetaVotacaoEntaoRetornaMensagem() throws Exception {
		controller.resetaVotacao();
		assertEquals(serializer.serialize(MENSAGEM,"votacao.reiniciada"), result.serializedResult());
	}
	
	@Test
	public void quandoCarregaRankingSemFilmesEntaoRetornaMensagem() throws Exception {
		controller.carregaRanking();
		assertEquals(serializer.serialize(MENSAGEM,"sem.filmes"), result.serializedResult());
	}
	
	@Test
	public void quandoCarregaRankingComFilmes() throws Exception {
		when(filmeService.carregaRanking()).thenReturn(todosOsFilmes());
		controller.carregaRanking();
		assertEquals(serializer.serialize("filmes",todosOsFilmes()), result.serializedResult());
	}
	
	@Test
	public void quandoCarregaRankingEOcorreAlgumErroEntaoRetornaMensagem() throws Exception {
		doThrow(new RuntimeException("Erro Interno do servidor"))
		.when(filmeService).carregaRanking();
		controller.carregaRanking();
		assertEquals(serializer.serialize("erro","Erro Interno do servidor"), result.serializedResult());
	}

	private List<Long[]> primeiraVotacao() {
		List<Long[]> list = new ArrayList<>();
		list.add(new Long[]{1l, 2l});
		return list;
	}

	private Filme primeiroFilme() {
		return criaFilme(1, "Depois da Terra");
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

	private Filme criaFilme(long id, String titulo) {
		Filme filme = new Filme();
		filme.setId(id);
		filme.setTitulo(titulo);
		return filme;
	}

}
