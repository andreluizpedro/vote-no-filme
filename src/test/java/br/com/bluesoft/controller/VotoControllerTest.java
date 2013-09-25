package br.com.bluesoft.controller;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import util.JsonSerializer;
import br.com.bluesoft.model.Filme;
import br.com.bluesoft.provider.UsuarioSessao;
import br.com.bluesoft.service.FilmeService;
import br.com.caelum.vraptor.util.test.MockSerializationResult;

public class VotoControllerTest {
	
	private VotoController controller;
	
	private MockSerializationResult result;
	
	private JsonSerializer serializer;
	
	@Mock
	private UsuarioSessao usuarioSessao;
	@Mock
	private FilmeService service;
	
	@Before
	public void setUp() {
		serializer = new JsonSerializer();
		result = new MockSerializationResult();
		MockitoAnnotations.initMocks(this);
		controller = new VotoController(result, usuarioSessao, service);
		when(service.carregaDoisFilmes(anyListOf(Filme.class))).thenReturn(todosOsFilmes());
	}

	@Test
	public void existePaginaDeVoto() {
		controller.index();
	}
	
	@Test
	public void quandoOcorreAlgumErroEntaoRetornaUmaMensage() throws Exception {
		doThrow(new RuntimeException("Erro interno do servidor")).when(service).carregaDoisFilmes(anyListOf(Filme.class));
		controller.carregaDoisFilmes();
		assertEquals(serializer.serialize("error","Erro interno do servidor"), result.serializedResult());
	}
	
	@Test
	public void quandoCarregaEExisteDadosEntaoRetornaTodosOsFilmes() throws Exception {
		controller.carregaDoisFilmes();
		assertEquals(serializer.serialize("filmes",todosOsFilmes()), result.serializedResult());
	}
	
	@Test
	public void quandoCarregaENaoExisteDadosEntaoRetornaUmaMensagem() throws Exception {
		when(service.carregaDoisFilmes(anyListOf(Filme.class))).thenReturn(Collections.<Filme>emptyList());
		controller.carregaDoisFilmes();
		assertEquals(serializer.serialize("mensagem","Consulta vazia"), result.serializedResult());
	}

	
	@Test
	public void quandoVotaNumFilmeEntaoRetornaSucesso() throws Exception {
		Long filmeId = 1l;
		when(service.carregaPorId(filmeId)).thenReturn(primeiroFilme());
		controller.votar(filmeId);
		assertEquals(serializer.serialize("mensagem","Filme votado com sucesso"), result.serializedResult());
	}
	
	@Test
	public void quandoVotaNumFilmeEOcorreAlgumErrorEntaoRetornaUmaMensagem() throws Exception {
		Long filmeId = 1l;
		when(service.carregaPorId(filmeId)).thenReturn(null);
		controller.votar(filmeId);
		assertEquals(serializer.serialize("mensagem","Filme não encontrado"), result.serializedResult());
	}
	
	@Test
	public void quandoVotaNumFilmeNaoEncontradoEntaoRetornaMensagem() throws Exception {
		Long filmeId = 1l;
		doThrow(new RuntimeException("Erro interno do servidor")).when(service).carregaPorId(filmeId);
		controller.votar(filmeId);
		assertEquals(serializer.serialize("error","Erro interno do servidor"), result.serializedResult());
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

	private Filme criaFilme(long id, String nome) {
		return new Filme(id, nome);
	}

}
