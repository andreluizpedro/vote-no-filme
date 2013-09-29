var Mensagem = function () {
	var self = this;
	self.texto =  ko.observable();
	self.tem =  ko.computed(function() {
		return self.texto() && true;
	});
	self.fecha = function (data, event) {
		self.texto(undefined);
	};
};

var ViewModel = function () {
	var self = this;
	self.erro =  new Mensagem();
	self.alerta =  new Mensagem();
	self.filmes = ko.observableArray([]);
	self.rankings = ko.observableArray([]);
	self.rankingUsuario = ko.observableArray([]);
	self.votoHabilitado = ko.observable(false);
	self.lng = ko.observable(locale);

	/**
	 INICIO
	 VOTACAO
	 CADASTRO
	 RANKING
	 */
	self.estado = ko.observable('INICIO');

	self.usuario = {
			nome: ko.observable(),
			email: ko.observable()
	};

	self.iniciar = function (data, event) {
		self.erro.texto(undefined);
		self.alerta.texto(undefined);
		self.votoHabilitado(false);
		self.estado('VOTACAO');
		self.filmes([
		             {titulo: '', capa: 'imagens/00_filme.jpg'},
		             {titulo: '', capa: 'imagens/00_filme.jpg'}
		             ]);
		self.rankings([]);
		self.rankingUsuario([]);

		self.usuario.nome('');
		self.usuario.email('');

		self.carregaDoisFilmes();
	};

	self.filmeA = ko.computed(function() {
		return self.filmes().length > 0 ? self.filmes()[0] : undefined;
	});

	self.filmeB = ko.computed(function() {
		return self.filmes().length > 0 ? self.filmes()[1]: undefined;
	});

	self.binding = function (filmes) {
		if(filmes.length > 1) {
			filmes[0].naoVotadoId = filmes[1].id;
			filmes[1].naoVotadoId = filmes[0].id;
			self.filmes(filmes);
			self.votoHabilitado(true);
		} else {
			self.filmes([]);
		}
	};

	var _bindingRanking = function (filmes, rankingsKo) {
		var rankings = [];
		$.each(filmes, function(i, filme) {
			rankings.push([filme.ranking, filme.titulo, filme.votos]);
		});
		rankingsKo(rankings);
	};
	self.bindingRankingGeral = function (filmes) {
		_bindingRanking(filmes, self.rankings);
	};

	self.bindingRankingUsuario = function (filmes) {
		_bindingRanking(filmes, self.rankingUsuario);
	};

	self.processaErro = function (erro, url) {
		if(url) {
			erro = erro + ", url: '" + url + "'";
		}
		self.erro.texto(erro);
		console.debug(erro);
	};

	self.carregaDoisFilmes = function () {
		self.votoHabilitado(false);
		var url = URLS.carregaDoisFilmes;
		$.ajax({
			url: url,
			dataType: 'json',
			success: function(data){
				if(data.mensagem) {
					switch (data.mensagem) {
					case 'votacao.encerrada' :
						self.estado('CADASTRO');
						break;
					default:
						self.alerta.texto(data.mensagem)
					}
				} else if(data.erro) {
					self.processaErro(data.erro);
				} else {
					self.binding(data.filmes);
				}
			},
			error: function(e){
				self.processaErro(e.statusText, url);
			}
		});
	};

	self.pular = function (data, event) {
		self.carregaDoisFilmes();
	};

	self.terminar = function (data, event) {
		self.estado('CADASTRO');
	};
	self.votar = function (data, event) {
		self.votoHabilitado(false);
		if (!data || !data.id || !data.naoVotadoId) {
			return;
		}
		var url =  URLS.votar(data);
		$.ajax({
			url: url,
			dataType: 'json',
			success: function(data){
				if (data.mensagem) {
					self.alerta.texto(data.mensagem);
					self.carregaDoisFilmes();
				} else if(data.erro) {
					self.processaErro(data.erro);
				}
			},
			error: function(e){
				self.processaErro(e.statusText, url);
			}
		});

	};
	self.resetaVotacao = function (data, event) {
		self.estado('INICIO');
		var url =  URLS.resetaVotacao;
		$.ajax({
			url: url,
			dataType: 'json',
			success: function(data){
				self.alerta.texto(data.mensagem);
				self.iniciar();
			},
			error: function(e){
				self.processaErro(e.statusText, url);
			}
		});
	};

	self.salvaVotacao = function(formElement) {
		if ($(formElement).valid()) {
			var url =  URLS.confirmaVotacao;
			$.ajax({
				url: url,
				type: 'post',
				data: $(formElement).serialize(),
				success: function(data){
					if (data.mensagem) {
						switch(data.mensagem) {
						case 'usuario.salvo.sucesso' :
							self.estado('RANKING');
							self.bindingRankings(data);
							break;
						};
						self.alerta.texto(data.mensagem);
					} else if (data.erro){
						self.erro.texto(data.erro);
					}
				},
				error: function(e){
					self.processaErro(e.statusText, url);
				}
			});
		};
		self.carregaRanking = function(data, event) {
			var botaoAtualizar = $(event.currentTarget);  
			botaoAtualizar.button('loading');
			var url =  URLS.carregaRanking;
			$.ajax({
				url: url,
				dateType: 'json',
				success: function(data){
					botaoAtualizar.button('reset');
					if(data.mensagem) {
						self.alerta.texto(data.mensagem)
					} else if(data.erro) {
						self.processaErro(data.erro);
					} else {
						self.bindingRankingGeral(data.filmes);
					}
				},
				error: function(e){
					botaoAtualizar.button('reset');
					self.processaErro(e.statusText, url);
				}
			});

		};

		self.bindingRankings = function(data) {
			if (data.rankings) {
				self.bindingRankingGeral(data.rankings);
			}
			if (data.rankingUsuario) {
				self.bindingRankingUsuario(data.rankingUsuario);
			}
		};
	};
};

ko.applyBindings(new ViewModel());