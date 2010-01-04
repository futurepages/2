package org.futurepages.test.page.html.action;

import org.futurepages.test.page.FutureSelenium;
import org.futurepages.util.BooleanUtil;

public class CheckBox extends UIElement implements Action{


	public CheckBox(FutureSelenium selenium) {
		super(selenium);
	}

	public boolean isChecked(String nomeCampo){
		return selenium.getValue(nomeCampo).equals("on");
	}
	/**
	 * Marca o check se 'opcaoMarcado' for true, ou deixa desmarcado se a 'opcaoMarcado' for false; 
	 * @param nomeCampo
	 * @param opcaoMarcado 
	 */
	private void setMarcado(String nomeCampo, boolean opcaoMarcado){
		boolean isMarcado = this.isChecked(nomeCampo);
		if(BooleanUtil.xor(opcaoMarcado, isMarcado)){
			selenium.click(nomeCampo);
		}
		System.out.println();
	}
	
	public void check(String nomeCampo){
		this.setMarcado(nomeCampo, true);
	}
	
	public void unCheck(String nomeCampo){
		this.setMarcado(nomeCampo, false);
	}

	/**
	 * Se nao há argumentos, a ação será clicar independente do valor.
	 */
	public void act(String name, Object... args) {
		Boolean opcao = false;
		if(args == null || args.length == 0){
			opcao = true;
		}else{
			opcao = BooleanUtil.parse(args[0].toString());
		}
		this.setMarcado(name, opcao);
	}
}
