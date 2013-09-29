package br.com.bluesoft.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import br.com.bluesoft.dto.Rankings;
import br.com.bluesoft.model.Filme;
import br.com.bluesoft.model.Usuario;
import br.com.bluesoft.provider.VotosUsuario;
import br.com.bluesoft.service.FilmeService;
import br.com.bluesoft.service.UsuarioService;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.serialization.JSONSerialization;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/voto")
public class VotoController {
	
	private static final String FILMES = "filmes";
	private static final String ERRO = "erro";
	private static final String MENSAGEM = "mensagem";
	private Result result;
	public VotosUsuario votosUsuario;
	private FilmeService filmeService;
	private UsuarioService usuarioService;
	
	private HttpSession session;

	public VotoController(Result result, VotosUsuario votosUsuario, FilmeService filmeService, UsuarioService usuarioService, HttpSession session) {
		this.result = result;
		this.votosUsuario = votosUsuario;
		this.filmeService = filmeService;
		this.usuarioService = usuarioService;
		this.session = session;
	}

	@Get
	@Path("")
	public void index() {
		
	}
	
	@Get
	@Path("/carregaDoisFilmes")
	public void carregaDoisFilmes() {
		try {
			List<Filme> filmes = filmeService.carregaDoisFilmes(votosUsuario);
			if (filmes.isEmpty()) {
				if (votosUsuario.ehVotacaoEncerrada()) {
					json().from("votacao.encerrada", MENSAGEM).serialize();
				} else {
					json().from("sem.filmes", MENSAGEM).serialize();
				}
			} else {
				json().from(filmes, FILMES).serialize();
			}
		} catch (Exception e) {
			json().from(e.getMessage(), ERRO).serialize();
		}
	}

	private JSONSerialization json() {
		return result.use(Results.json());
	}
	
	@Get
	@Path("/votar/{filmeId}/{filmeIdNaoVotado}")
	public void votar(Long filmeId, Long filmeIdNaoVotado) {
		try {
			votosUsuario.adicionaVotacaoFilme(filmeId, filmeIdNaoVotado);
			json().from("filme.voltado.sucesso", MENSAGEM).serialize();
		} catch (Exception e) {
			json().from(e.getMessage(), ERRO).serialize();
		}
	}
	
	@Post
	@Path("/confirmaVotacao")
	public void confirmaVotacao(Usuario usuario) {
		try {
			if (usuarioService.existeEmailUsuario(usuario.getEmail())){
				json().from("usuario.ja.cadastrado", MENSAGEM).serialize();
			} else if (!votosUsuario.getVotacaoFilmes().isEmpty()) {
				usuarioService.salvaVotacao(votosUsuario, usuario);
				Rankings rankings = getRankings(usuario);
				json().withoutRoot().from(rankings).include("rankings", "rankingUsuario").serialize();
			} else {
				json().from("nenhum.filme.voltado", MENSAGEM).serialize();
			}
		} catch (Exception e) {
			json().from(e.getMessage(), ERRO).serialize();
		}
	}
	
	private Rankings getRankings(Usuario usuario) {
		List<Filme> rankings = filmeService.carregaRanking();
		List<Filme> rankingUsuario = filmeService.carregaRanking(usuario);

		Rankings rankingsRetorno = new Rankings();
		rankingsRetorno.rankings       = new ArrayList<>(rankings);
		rankingsRetorno.rankingUsuario = new ArrayList<>(rankingUsuario);
		rankingsRetorno.mensagem       = "usuario.salvo.sucesso";
		return rankingsRetorno;
	}
	
	@Path("/carregaRanking")
	public void carregaRanking() {
		try {
			List<Filme> filmes = filmeService.carregaRanking();
			if (filmes.isEmpty()) {
				json().from("sem.filmes", MENSAGEM).serialize();
			} else {
				json().from(filmes, FILMES).serialize();
			}
		} catch (Exception e) {
			json().from(e.getMessage(), ERRO).serialize();
		}
		
	}

	@Get
	@Path("/resetaVotacao")
	public void resetaVotacao() {
		votosUsuario.resetarVotacao();
		json().from("votacao.reiniciada", MENSAGEM).serialize();
		session.invalidate();
	}
	
}
