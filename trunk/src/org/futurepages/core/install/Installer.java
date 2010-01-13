package org.futurepages.core.install;

/**
 * Instalador. Herde essa classe e coloque dentro do pacote install
 * do seu módulo caso queira instalar algo quando o MODE_INSTALL estiver ligado.
 * 
 * @author leandro
 */
public abstract class Installer implements InstallerImpl {

    public Installer() {
		try {
			this.execute();
		} catch (Exception ex) {
			System.out.println("[::install::] Error installing... "+this.getClass().getName());
			ex.printStackTrace();
		}
    }
}