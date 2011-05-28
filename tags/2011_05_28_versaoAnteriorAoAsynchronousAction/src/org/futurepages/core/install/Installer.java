package org.futurepages.core.install;

/**
 * Instalador. Herde essa classe e coloque dentro do pacote install
 * do seu m�dulo caso queira instalar algo quando o MODE_INSTALL estiver ligado.
 * 
 * @author leandro
 */
public abstract class Installer implements Installation {

	private long timeCount;

    public Installer() {
		try {
			timeCount = System.currentTimeMillis();
			this.execute();
			timeCount = (System.currentTimeMillis() - timeCount) / 1000l;
		} catch (Exception ex) {
			System.out.println("[::install::] Error installing... "+this.getClass().getName());
			ex.printStackTrace();
		}
    }

	protected void install(Installation installer) throws Exception{
		long initTime = System.currentTimeMillis() / 1000l;//em segundos
		String instaladorNome = installer.getClass().getName();

		System.out.println("     ----> Installing ["+instaladorNome+"]");
		try{
			installer.execute();
		}catch(Exception ex){
			System.out.println("     ----> ERROR DURING INSTALLATION.");
			ex.printStackTrace();
		}
		long endTime = (System.currentTimeMillis()/1000l);
		System.out.println("     ----> installed in "+(endTime-initTime)+" secs.\n");
	}

	public long totalTime(){
		return this.timeCount;
	}
}