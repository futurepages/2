package org.futurepages.test.page;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.futurepages.test.page.html.UIElementEnum;
import org.futurepages.test.page.html.action.*;

import com.thoughtworks.selenium.DefaultSelenium;

public class FutureSelenium extends DefaultSelenium {

	public static FutureSelenium instance;
	
	public Map<UIElementEnum, Action> elements;
	public ScreenAction page;
	public ButtonAction button;
	public LinkAction link;
	public InputAction input;
	public InputFileAction inputFile;
	public DynSelectAction dynSelect;
	public SelectAction select;
	public TextAreaAction textArea;
	public PageAction table;
	public CheckBox checkBox;

	public FutureSelenium(String serverHost, int serverPort,
			String browserStartCommand, String browserURL) {
		super(serverHost, serverPort, browserStartCommand, browserURL);
		page = new ScreenAction(this);
		button = new ButtonAction(this);
		link = new LinkAction(this);
		input = new InputAction(this);
		inputFile  = new InputFileAction(this);
		dynSelect = new DynSelectAction(this);
		select = new SelectAction(this);
		table = new PageAction(this);
		textArea = new TextAreaAction(this);
		checkBox = new CheckBox(this);

		elements = new HashMap<UIElementEnum, Action>();
		elements.put(UIElementEnum.BUTTON, button);
		elements.put(UIElementEnum.CHECKBOX, checkBox);
		elements.put(UIElementEnum.DYNSELECT, dynSelect);
		elements.put(UIElementEnum.SELECT, select);
		elements.put(UIElementEnum.INPUT, input);
		elements.put(UIElementEnum.INPUTFILE, inputFile);
		elements.put(UIElementEnum.LINK, link);
		elements.put(UIElementEnum.TABLE, table);
		elements.put(UIElementEnum.TEXTAREA, textArea);
		elements.put(UIElementEnum.PAGE, page);
		
	}

	public static FutureSelenium init(String serverHost, int serverPort, String browserStartCommand, String browserURL) {
		instance = new FutureSelenium(serverHost, serverPort, browserStartCommand, browserURL);
		return instance;
	}

	public static FutureSelenium getInstance(){
		return instance;
	}
	
	public void esperar(String nomeCampo) {
		esperar(nomeCampo, 5);
	}
	public void esperar(String nomeCampo, int segundos) {
		for (int second = 0;; second++) {
			try {
				if(isElementPresent(nomeCampo)){
					return;
				}
				if (second >= segundos){
					Assert.fail("O tempo de espera pelo elemento " + nomeCampo + " expirou!");
				}
				Thread.sleep(1000l);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
	public void esperar(String nomeCampo, String valor) {
		esperar(nomeCampo, valor, 60); 
	}

	/**
	 * Método que espera durante 60 segundos até que uma condição informada seja aceita
	 * @param condicao
	 */
	public void esperar(String nomeCampo, String valor, Integer segundosTimeOut) {
		boolean isVerificarCampo = (nomeCampo == null) ? false : true;
		boolean isVerificarValor = (valor == null) ? false : true;

		for (int second = 0;; second++) {
			if (second >= segundosTimeOut) 
				Assert.fail("O tempo de espera pelo elemento " + nomeCampo + " expirou!");

			try {
				if(isVerificarCampo || isVerificarValor){
					if(isVerificarCampo && isVerificarValor)
						if(isPossuiElemento(nomeCampo) && isPossuiTexto(valor))
							break;

					if(isVerificarCampo && !isVerificarValor && isPossuiElemento(nomeCampo))
						break;

					if(!isVerificarCampo && isVerificarValor && isPossuiTexto(valor))
						break;
				}
				else break;

			} catch (Exception e) {}

			try {
				Thread.sleep(1000);
			} catch (Exception e) {}
		}
	}

	/**
	 * Espera a página carregar por um tempo especificado
	 * @param segundos
	 */
	public  void esperarCarregarPagina(Integer segundos){
		Integer milissegundos = segundos * 1000;
		waitForPageToLoad(milissegundos.toString());
	}

	/**
	 * Verifica a esistência de uma string na tela
	 * @param texto
	 * @return
	 */
	public  boolean isPossuiTexto(String texto){
		return isTextPresent(texto);
	}

	/**
	 * Verifica a existência de uma string em uma posição da tela
	 * @param texto
	 * @param posicao
	 * @return
	 */
	public  boolean isPossuiTexto(String name, String valor){
		return valor.equals(getText(name));
	}
	/**
	 * Verifica se existe um elemento no local especificado
	 * @param local
	 * @return
	 */
	public boolean isPossuiElemento(String name){
		return isElementPresent(name);
	}
	
	public Action getAction(UIElementEnum uiElement){
		return this.elements.get(uiElement);
	}

	public void waitForElement(String string) {
		for (int i = 0; i < 5; i++) {
			if(instance.isElementPresent(string)){
				return;
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}
		}
	}
}
