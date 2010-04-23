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

	public void installExamples() throws Exception {
		if (modules != null) {
			try {
				if (HibernateManager.isRunning()) {
					Dao.beginTransaction();
				}
				try {
					Class exInstallerClass = Class.forName(INSTALL_DIR_NAME + ".Examples");
					log(">>> Examples installing...  ");
					Installer examples = (Installer) exInstallerClass.newInstance();
					log(">>> Examples installed in " + examples.totalTime() + " secs.");
				} catch (ClassNotFoundException ex) {
					log(">>> installer of Examples not present.");
				}
				if (HibernateManager.isRunning()) {
					Dao.commitTransaction();
					Dao.close();
				}
			} catch (Exception ex) {
				Dao.rollBackTransaction();
				throw ex;
			}
		}
	}

	public void installProduction() throws Exception {
		if (modules != null) {
			try {
				if (HibernateManager.isRunning()) {
					Dao.beginTransaction();
				}
				try {
					Class prodInstallerClass = Class.forName(INSTALL_DIR_NAME + ".Production");
					log(">>> Production installing...  ");
					Installer production = (Installer) prodInstallerClass.newInstance();
					log(">>> Production installed in " + production.totalTime() + " secs.");
				} catch (ClassNotFoundException ex) {
					log(">>> installer of Production not present.");
				}
				if (HibernateManager.isRunning()) {
					Dao.commitTransaction();
					Dao.close();
				}
			} catch (Exception ex) {
				Dao.rollBackTransaction();
				throw ex;
			}
		}
	}

	public void installAllModules() throws Exception {
		if (modules != null) {
			try {
				if (HibernateManager.isRunning()) {
					Dao.beginTransaction();
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
				if (HibernateManager.isRunning()) {
					Dao.commitTransaction();
					Dao.close();
				}
			} catch (Exception ex) {
				Dao.rollBackTransaction();
				throw ex;
			}
		}
	}

	public void installResources() throws Exception {
		if (modules != null) {
			try {
				if (HibernateManager.isRunning()) {
					Dao.beginTransaction();
				}
				try {
					Class resourcesInstaller = Class.forName(INSTALL_DIR_NAME + ".Resources");
					log(">>> installer " + resourcesInstaller.getSimpleName() + " running...  ");
					resourcesInstaller.newInstance();
					log(">>>   Resources OK.");
				} catch (ClassNotFoundException ex) {
					log(">>> installer of Resources isn't present.");
				}
				if (HibernateManager.isRunning()) {
					Dao.commitTransaction();
					Dao.close();
				}
			} catch (Exception ex) {
				Dao.rollBackTransaction();
				throw ex;
			}
		}
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
					log(">>>   Resources OK.");
				} catch(ClassNotFoundException ex){
					log(">>> installer of Resources isn't present.");
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
					Class exInstallerClass = Class.forName(INSTALL_DIR_NAME+".Examples");
					log(">>> Examples installing...  ");
					Installer examples = (Installer) exInstallerClass.newInstance();
					log(">>> Examples installed in "+examples.totalTime()+" secs.");
				} catch(ClassNotFoundException ex){
					log(">>> installer of Examples not present.");
				}

				if (HibernateManager.isRunning()) {
					Dao.commitTransaction();
					Dao.close();
				}
			} catch (Exception ex) {
				Dao.rollBackTransaction();
				throw ex;
			}
		}
	}

	private void log(String msg) {
		System.out.println("[:install:] " + msg);
	}
}