package br.com.bluesoft.controller;

import java.util.ArrayList;
import java.util.List;

import br.com.bluesoft.model.Filme;
import br.com.bluesoft.provider.UsuarioSessao;
import br.com.bluesoft.service.FilmeService;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.serialization.JSONSerialization;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/voto")
public class VotoController {
	
	private static final String FILMES = "filmes";
	private static final String ERROR = "error";
	private static final String MENSAGEM = "mensagem";
	private Result result;
	private UsuarioSessao usuarioSessao;
	private FilmeService filmeService;

	public VotoController(Result result, UsuarioSessao usuarioSessao, FilmeService filmeService) {
		this.result = result;
		this.usuarioSessao = usuarioSessao;
		this.filmeService = filmeService;
	}

	@Get("")
	public void index() {
		
	}
	
	@Get("/carregaDoisFilmes")
	public void carregaDoisFilmes() {
		try {
			List<Filme> filmes = filmeService.carregaDoisFilmes(
					new ArrayList<Filme>(usuarioSessao.getFilmesVotados()));
			if (filmes.isEmpty()) {
				json().from("Consulta vazia", MENSAGEM).serialize();
			} else {
				json().from(filmes, FILMES).serialize();
			}
		} catch (Exception e) {
			json().from(e.getMessage(), ERROR).serialize();
		}
	}

	private JSONSerialization json() {
		return result.use(Results.json());
	}
	
	@Get("/votar/{id}")
	public void votar(Long filmeId) {
		try {
			Filme filme = filmeService.adicionaVoto(filmeId);
			if (filme != null) {
				usuarioSessao.getFilmesVotados().add(filme);
				json().from("Filme votado com sucesso", MENSAGEM).serialize();
			} else {
				json().from("Filme não encontrado", MENSAGEM).serialize();
			}
		} catch (Exception e) {
			json().from(e.getMessage(), ERROR).serialize();
		}
	}

}
