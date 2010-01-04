package org.futurepages.test.page.html.action;

import org.futurepages.test.page.FutureSelenium;
import org.junit.Assert;

public abstract class UIElement{
	protected String name;
	protected FutureSelenium selenium;
	protected boolean possuiAjax;

	public UIElement(FutureSelenium selenium) {
		this.selenium = selenium;
	}
	
	protected void processarClique(String name, Integer tempoEspera) {
		selenium.click(name);
		if(possuiAjax)
			selenium.esperar(name, tempoEspera);
		else
			selenium.esperarCarregarPagina(tempoEspera);
	}
	/**
	 * Clica e executa a operação de um botão 
	 * @param tipo
	 * @param nomeColecao
	 * @param tempoEspera
	 * @throws ClassNotFoundException 
	 * @throws InterruptedException 
	 */
	public void clicar(String nome, Integer tempoEspera) {
		processarClique(nome, tempoEspera);
	}
	
	public void clicar(String nome) {
		clicar(nome, 30);
	}

	public void clicar(String nome, Boolean possuiAjax) {
		this.possuiAjax = possuiAjax;
		clicar(nome, 30);
	}
	
	public boolean verificarPresenca(String locator){
		return selenium.isElementPresent(locator);
	}
	
}
