package org.futurepages.test.page.html.action;

import org.futurepages.test.page.FutureSelenium;


public class InputAction extends UIElement  implements Action {

	public InputAction() {
		super(FutureSelenium.getInstance());
	}

	public InputAction(FutureSelenium futureSelenium) {
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
	 * @param idAutoComplete - ID do campo a ser selecionado no autocomplete
	 * @throws ClassNotFoundException 
	 */
	public void digitar(String nomeCampo, String valor, String idAutoComplete) {
		String valorInput = valor;
		if(valor.length() > 4){
			valorInput = valor.substring(0, valor.length() - 1);
		}
		selenium.typeKeys(nomeCampo, valorInput);
		selenium.esperar(idAutoComplete, null);
		selenium.click(idAutoComplete);
	}

	/**
	 * @param name nome identificado do Input
	 * @param args argumentos ao input args[0]: valor a ser digitado, 
	 * args[1]: id do elemento (para 'autoComplete')
	 */
	public void act(String name, Object... args) {
		if(args != null && args.length >0){
			String valor = args[0].toString();
			try {
				String id = args[1].toString();
				digitar(name, valor,id);
			} catch (IndexOutOfBoundsException e) {
				digitar(name, valor);
			}
		}
	}

}
