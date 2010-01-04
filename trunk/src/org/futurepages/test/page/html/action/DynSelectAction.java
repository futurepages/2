package org.futurepages.test.page.html.action;

import org.futurepages.test.page.FutureSelenium;


public class DynSelectAction extends UIElement  implements Action {

	public DynSelectAction() {
		super(FutureSelenium.getInstance());
	}

	public DynSelectAction(FutureSelenium futureSelenium) {
		super(futureSelenium);
	}

	/**
	 * Digita um texto num input comum
	 * @param nomeCampo - Name do campo que recerá o texto
	 * @param valor - Texto a ser digitado
	 */
	public  void digitar(String nomeCampo, String valor){
		selenium.type(nomeCampo, valor);
	}

	/**
	 * Digita um texto num textfield com autocomplete
	 * @param nomeCampo - Name do campo que recerá o texto
	 * @param valor - Texto a ser digitado
	 * @param opcaoLocator - localizador do campo a ser selecionado no autocomplete
	 * @throws ClassNotFoundException 
	 */
	public void selecionar(String nomeCampo, String valor, String opcaoLocator) {

		selenium.click(nomeCampo);
		selenium.type(nomeCampo, valor);
		selenium.esperar(opcaoLocator, 10);
		selenium.click(opcaoLocator);
	}

	/**
	 * @param name nome identificado do Input
	 * @param args argumentos ao input args[0]: valor a ser digitado, 
	 * args[1]: id do elemento (para 'autoComplete')
	 */
	public void act(String dynSelectName, Object... args) {
		
		if(args != null && args.length >0){
			String valor = args[0].toString();
			try {
				String opcao = args[1].toString();
				String opcaoLocator = localizadorOpcao(dynSelectName, opcao);
				System.out.println("DynSelectAction.act():> LOCALIZADOR OPTION CRIADO: "+opcaoLocator );
				
				selecionar(dynSelectName, valor, opcaoLocator );
			} catch (IndexOutOfBoundsException e) {
				digitar(dynSelectName, valor);
			}
		}
	}
	
	private String localizadorOpcao(String dynSelectName, String option){
		String locator = "//div[@id='complete_"+dynSelectName+"']/ul/li/div[text() = '"+option+"']";
		return locator;
	}

}
