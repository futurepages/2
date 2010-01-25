package org.futurepages.core.install;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.futurepages.core.persistence.Dao;
import org.futurepages.core.persistence.HibernateManager;
import org.futurepages.core.tags.build.ModulesAutomation;

/**
 * Classe responsável pela automatização da instalação dos módulos.
 * Executa de forma reflexiva os 'Installers' de cada módulo.
 *
 * Os instaladores estão presente na raiz do pacote  install sob o seguinte nome
 * de classe: DataBaseInstaller
 *
 * @author leandro
 */
public class InstallersManager extends ModulesAutomation {

	private static final String INSTALL_DIR_NAME = "install";

	public InstallersManager(File[] modules) {
		super(modules, INSTALL_DIR_NAME);
	}

	/**
	 * Inicializa os Instaladores do banco de dados.
	 * 
	 * @throws java.lang.Exception
	 */
	public void install() throws Exception {
		log("BEGIN...");
		installModules(modules);
		log("END.");
	}

	/**
	 *
	 * @param modules são as pastas de módulos que serão varridas para instalação.
	 * 
	 * @throws Exception
	 */
	public static void initialize(File[] modules) throws Exception {
		new InstallersManager(modules).install();
	}

	/**
	 *
	 * Instalação do banco de dados.
	 *
	 * 1) Instala os Recursos (se houver: install.Resources)
	 * 2) Instala os módulos em ordem alfabética (dentro de uma única transação)
	 * 3) Instala os exemplos da aplicação (se houver: install.Examples)
	 *
	 * @param modules
	 * @throws Exception 
	 * @throws java.lang.Exception
	 */
	private void installModules(File[] modules) throws Exception {
		if (modules != null) {
			try {
				if (HibernateManager.isRunning()) {
					Dao.beginTransaction();
				}

				//Initial Resources Installer
				try{
					Class resourcesInstaller = Class.forName(INSTALL_DIR_NAME+".Resources");
					log(">>> installer " + resourcesInstaller.getSimpleName() + " running...  ");
					resourcesInstaller.newInstance();
					log("   Resources OK.");
				} catch(ClassNotFoundException ex){
					log(">>> installer of Examples isn't present.");
				}


				Map<String, List<Class<Installer>>> classes = getModulesDirectoryClasses(Installer.class, null);
				for (String moduleName : classes.keySet()) {
					log("module '" + moduleName + "' installing...");
					for (Class<?> installer : classes.get(moduleName)) {
						log(">>> installer " + installer.getSimpleName() + " running...  ");
						installer.newInstance();
						log(">>> installer " + installer.getSimpleName() + " OK");
					}
					log("module '" + moduleName + "' installed.");
				}
				//Examples Installer
				try{
					Class examplesInstaller = Class.forName(INSTALL_DIR_NAME+".Examples");
					log(">>> installer " + examplesInstaller.getSimpleName() + " running...  ");
					examplesInstaller.newInstance();
					log("   Examples OK.");
				} catch(ClassNotFoundException ex){
					log(">>> installer of Examples isn't present.");
				}

				if (HibernateManager.isRunning()) {
					Dao.commitTransaction();
					Dao.close();
				}
			} catch (Exception ex) {
				Dao.rollBackTransaction();
				ex.printStackTrace();
				throw new Exception("[::install::] Error trying to INSTALL something: (" + ex + ")");
			}
		}
	}

	private void log(String msg) {
		System.out.println("[:install:] " + msg);
	}
}