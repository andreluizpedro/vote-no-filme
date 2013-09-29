<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <title>Vote no Filme</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">

    <!-- Le styles -->
    <link href="assets/css/bootstrap.css" rel="stylesheet">
    <link href="assets/css/jquery.dataTables.css" rel="stylesheet">
    <link href="assets/css/style.css" rel="stylesheet">
    <link href="assets/css/bootstrap-responsive.css" rel="stylesheet">

    <!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
      <script src="assets/js/html5shiv.js"></script>
      <![endif]-->

    <!-- Fav and touch icons -->
    <link rel="apple-touch-icon-precomposed" sizes="144x144" href="assets/ico/apple-touch-icon-144-precomposed.png">
    <link rel="apple-touch-icon-precomposed" sizes="114x114" href="assets/ico/apple-touch-icon-114-precomposed.png">
      <link rel="apple-touch-icon-precomposed" sizes="72x72" href="assets/ico/apple-touch-icon-72-precomposed.png">
                    <link rel="apple-touch-icon-precomposed" href="assets/ico/apple-touch-icon-57-precomposed.png">
                                   <link rel="shortcut icon" href="assets/ico/favicon.png">
  </head>

  <body style="background-color: gainsboro">
  	<script type="text/javascript">
  		var locale = '<%= request.getLocale().toString() %>';
  	</script>

    <div class="container-narrow">

      <div class="masthead">
        <ul class="nav nav-pills pull-right">
          <li class="active"><a href="#">Inicio</a></li>
          <li><a href="#">Sobre</a></li>
          <li><a href="#">Contato</a></li>
        </ul>
        <h3 class="muted">Vote no filme</h3>
      </div>
      <hr>
        <div data-bind="if : estado() == 'INICIO'">
			<div class="jumbotron">
				<h1 class="muted">Vote no seu filme favorito!</h1>
				<p class="lead">Serão postos filmes em pares para escolha um.
					Voçê ainda pode pular ou terminar a votação quando convier. No
					final veja as colocações de seus filmes favoritos.</p>
				<a class="btn btn-large btn-success" data-bind="click : iniciar">Começe	agora!</a>
			</div>
		</div>
		<div class="row-fluid" data-bind="if : estado() == 'VOTACAO'">
			<div class="row-fluid">
            	<h1 class="muted span9" style="text-align: center;">Qual seu Favorito?</h1>
			</div>
            <br/>
            <div class="row-fluid">
                <div class="filme">
                    <div class="row-fluid" >
                        <div class="span3" data-bind="with: filmeA, fadeOutIn: 'fade'" >
                            <div class="filme-images">
                                <a href="#" data-bind="attr:{title: titulo}" >
                                    <div class="mask">
                                        <button class="btn btn-success votar" data-bind="click : $root.votar, enable : $root.votoHabilitado" >Votar</button>
                                    </div>
                                    <img src="imagens/00_filme.jpg" data-bind="attr:{src: capa}"  alt="160x240"/>
                                </a>
                            </div>
                        </div>
                        <div class="span3 row-fluid" style="margin-top: 60px;">
                        	<div class="row-fluid">
                        		<button class="btn btn-info span12" data-bind="click : $root.pular" >Pular</button>
                        	</div>
                        	<div class="row-fluid">
                        		&nbsp;
                        	</div>
                        	<div class="row-fluid">
				                <button class="btn btn-info  span12" data-bind="click : $root.terminar" >Terminar Votação</button>
                        	</div>
                        </div>
                        <div class="span3" data-bind="with: filmeB, fadeOutIn: 'fade'" >
                            <div class="filme-images">
                                <a href="#" data-bind="attr:{title: titulo}" >
                                    <div class="mask">
                                        <button class="btn btn-success votar" data-bind="click : $root.votar, enable : $root.votoHabilitado" >Votar</button>
                                    </div>
                                    <img src="imagens/00_filme.jpg" data-bind="attr:{src: capa}"  alt="160x240"/>
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
	<div class="box-panel " >
        <div class="box-panel " data-bind="if : estado() == 'CADASTRO'">
            <div>
                <h1 class="muted">Salve sua votação</h1>
                <form data-bind="formValidate: {submit: salvaVotacao}" action="#" >
                    <div class="row-fluid">
                        <div class="field row-fluid">
                            <label for="nome" accesskey="N">Nome*</label>
                            <input type="text" class="input-block-level span5" name="usuario.nome"  data-bind="vaule : usuario.nome"  id="nome" placeholder="Nome" required>
                        </div>
                        <div class="field row-fluid">
                            <label for="email" accesskey="E">Email*</label>
                            <input type="email" class="input-block-level span5" name="usuario.email" data-bind="vaule : usuario.email" id="email" placeholder="seu_email@endereco.com" required>
                        </div>
                    </div>
                    <div class="row-fluid">
	                    <button type="submit" class="btn-success btn " >Salvar</button>
                    </div>
                    
                </form>
            </div>
        </div>
        <div class="row-fluid">
	        <div class="row-fluid" data-bind="if: estado() == 'RANKING' || estado() == 'CADASTRO'">
				<button class="btn-success btn" data-bind="click : resetaVotacao">Novo usuário para votar</button>
	        </div>
	        <div class="row-fluid" style="height: 100px;">
	        	<br>
	            <div class="row-fluid" data-bind="if : erro.tem">
	                <div class="alert alert-danger" data-bind="with : erro">
	                    <button type="button" class="close" data-dismiss="alert" data-bind="click : fecha">&times;</button>
	                    <span><i class="icon-notification"></i><strong>Erro</strong> <span data-bind="text : texto"></span> </span>
	                </div>
	            </div>
	            <div class="row-fluid" data-bind="if : alerta.tem">
	                <div class="alert alert-info" data-bind="with : alerta, alertHide : {hide : alerta.fecha}">
	                    <button type="button" class="close" data-dismiss="alert">&times;</button>
	                    <span><i class="icon-notification"></i><strong>Informação</strong> <span data-bind="text : texto"></span> </span>
	                </div>
	            </div>
	        </div>
	        <div class="row-fluid" data-bind="if: estado() == 'RANKING'">
		        <div class="row-fluid" style="margin: 20px">
		        	<h3>Ranking dos meus favoritos</h3>
		            <div class="row-fluid box">
		                <table class="table table-bordered table-hover datatable" data-bind="
		                		dataTable:{data : rankingUsuario, 
		                		options: { 'bFilter': false, 'bPaginate': false/**, 'oLanguage' : {'sUrl': 'assets/lng/' + lng() + '.txt'}**/}
		                	}" >
		                    <thead>
		                    <tr>
		                        <th>ranking</th>
		                        <th>Título do filme</th>
		                        <th>votos</th>
		                    </tr>
		                    </thead>
		                    <tbody/>
		                </table>
		            </div>
		        </div>
		        <div class="row-fluid" style="margin: 20px">
		        	<h3>Ranking Geral</h3>
		        	<div class="row-fluid">
		        		<button class="btn btn-info" data-bind="click : carregaRanking"  data-loading-text="Carregando...">Atualizar</button>
		        	</div>
		            <div class="row-fluid box">
		                <table class="table table-bordered table-hover datatable" data-bind="
		                		dataTable:{data : rankings, 
		                		options: { 'bFilter': false, 'bPaginate': false /**, 'oLanguage' : {'sUrl': 'assets/lng/' + lng() + '.txt'}**/}
		                	}" >
		                    <thead>
		                    <tr>
		                        <th>ranking</th>
		                        <th>Título do filme</th>
		                        <th>votos</th>
		                    </tr>
		                    </thead>
		                    <tbody/>
		                </table>
		            </div>
		        </div>
	        </div>
	        <hr>
        </div>
      <div class="footer">
        <p>&copy; 2013</p>
      </div>

    </div> <!-- /container -->

    <!-- Le javascript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="assets/js/jquery.min.js"></script>
    <script src="assets/js/jquery.validate.min.js"></script>
    <script src="assets/js/jquery.dataTables.min.js"></script>
    <script src="assets/js/bootstrap.min.js"></script>

    <script src="assets/js/knockout.min.js"></script>
    <script src="assets/js/knockout.custom.js"></script>
    <script src="assets/js/URLS.js"></script>
    <script src="assets/js/app.js"></script>

  </body>
</html>

