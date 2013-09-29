package br.com.bluesoft.provider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Component;

import br.com.caelum.vraptor.ioc.SessionScoped;

@Component
@SessionScoped
public class VotosUsuario {

	private List<Long[]> votacaoFilmes = new ArrayList<>();
	private List<Long[]> combinacoes = new ArrayList<>();
	private static final int VOTADO     = 0;
	private static final int NAO_VOTADO = 1;
	private static final int USADA      = 2;

	private static final Comparator<Long[]> COMPARADOR = new Comparator<Long[]>() {
		
		@Override
		public int compare(Long[] o1, Long[] o2) {
			int cmp = compare0(o1, o2);
			if(cmp != 0) {
				cmp = compare0(new Long[]{o1[NAO_VOTADO], o1[VOTADO]}, o2);
			}
			return cmp;
		}

		private int compare0(Long[] o1, Long[] o2) {
			int cmp = o1[VOTADO].compareTo(o2[VOTADO]);
			if (cmp == 0) {
				cmp = o1[NAO_VOTADO].compareTo(o2[NAO_VOTADO]);
			}
			return cmp;
		}
	};
	
	public void adicionaVotacaoFilme(long filmeId, long filmeIdNaoVotado) {
		Long[] votacao = new Long[]{filmeId, filmeIdNaoVotado};
		int index = Collections.binarySearch(votacaoFilmes, votacao, COMPARADOR);
		if (index < 0) {
			index = (-(index) - 1);
			votacaoFilmes.add(index, votacao);
			marcarCombinacaoUsada(votacao);
		}
	}
	
	private void marcarCombinacaoUsada(Long[] combinacao) {
		int indice = Collections.binarySearch(combinacoes, combinacao, COMPARADOR);
		setCombinacaoUsada(indice);
	}

	private Long[] setCombinacaoUsada(int indice) {
		Long[] combinacao = null;
		if (indice != -1) {
			combinacao = combinacoes.get(indice);
			combinacao[USADA] = 1l;
		}
		return combinacao;
	}

	private boolean ehCombinacaoUsada(int indice) {
		return combinacoes.get(indice)[USADA].equals(1l);
	}
	
	public List<Long[]> getVotacaoFilmes() {
		return votacaoFilmes;
	}

	public boolean ehCombinacoesVazia() {
		return combinacoes.isEmpty();
	}

	/**
	 * n! / p!(n - p)!
	 * p => 2
	 * n! / 2 *(n - 2)!
	 */
	public void gerarCombinacoes(Long[] filmeIds) {
		combinacoes.clear();
		int size = filmeIds.length;
		for (int a = 0; a < size - 1; a++) {
			for (int b = a + 1; b < size; b++) {
				combinacoes.add(new Long[]{filmeIds[a], filmeIds[b], 0l});
			}
		}
		for (int i = 0; i < votacaoFilmes.size(); i++) {
			Long[] votacao = votacaoFilmes.get(i);
			marcarCombinacaoUsada(votacao);
		}
	}
	
	public Long[] proximaCombinacao() {
		int i = 0;
		int indice = -1;
		while (indice == -1 && i < combinacoes.size()) {
			if(!ehCombinacaoUsada(i)) {
				indice = i;
			}
			i++;
		}
		return setCombinacaoUsada(indice);
	}

	public List<Long> getFilmeIdVotados() {
		List<Long> filmeIdVotados = new ArrayList<>();
		for(Long[] votacao : votacaoFilmes) {
			filmeIdVotados.add(votacao[VOTADO]);
		}
		return filmeIdVotados;
	}
	
	public void resetarVotacao() {
		votacaoFilmes.clear();
		combinacoes.clear();
	}

	public boolean temCombinacao() {
		int i = 0;
		boolean encontrada = false;
		while (!encontrada && i < combinacoes.size()) {
			encontrada = !ehCombinacaoUsada(i);
			i++;
		}
		return encontrada;
	}

	public boolean temFilmesVoltados() {
		return !votacaoFilmes.isEmpty();
	}

	public boolean ehVotacaoEncerrada() {
		return !temCombinacao() && temFilmesVoltados();
	}
}
