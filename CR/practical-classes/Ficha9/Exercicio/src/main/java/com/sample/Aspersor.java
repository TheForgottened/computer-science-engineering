package com.sample;

public class Aspersor {
	Divisao divisao;
	boolean ligado;
	
	public Aspersor(Divisao divisao) {
		this.divisao = divisao;
		ligado = false;
	}

	public Divisao getDivisao() { return divisao; }
	public void setDivisao(Divisao divisao) { this.divisao = divisao; }

	public boolean isLigado() { return ligado; }
	public void setLigado(boolean ligado) { this.ligado = ligado; }
}
