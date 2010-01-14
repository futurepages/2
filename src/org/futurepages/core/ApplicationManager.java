package org.futurepages.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.futurepages.core.config.Modules;
import org.futurepages.core.config.Params;
import org.futurepages.core.control.AbstractApplicationManager;
import org.futurepages.core.context.Context;

/**
 * Classe onde são feitas as inicializações da aplicação.
 * Sao registrados (agregados) todos os ApplicationManagers dos Módulos da Aplicação (ModuleManagers).
 */
public class ApplicationManager extends AbstractApplicationManager {

	private final List<AbstractApplicationManager> managers;

	private boolean initialized = false;

	/**
	 * Default constructor, capable to call the registerManagers() method.
	 *
	 * @throws Exception  about the ApplicationManager instanciation
	 */
	public ApplicationManager() {
		super();
		this.managers = new ArrayList<AbstractApplicationManager>();
		registerManagers();
		initialized = true;
	}

	/**
     * Registra o InitManager e os ModuleManagers dos demais módulos
     */
    public void registerManagers() {
        try {
            log("Iniciando Registro de Managers");
            //Lista os módulos na árvore de arquivos
            File[] modules = (new File(Params.get("MODULES_CLASSES_REAL_PATH"))).listFiles();

            //Registra o Módulo de Inicialização da Aplicação
            Class initManagerClass = Class.forName(Params.get("INIT_MANAGER_CLASS"));
            register(initManagerClass);

            // Registra os demais módulos: mapeia, registra os managers e conecta ao banco criando uma sessão.
            if(modules!=null){
                if (Params.get("CONNECT_EXTERNAL_MODULES").equals("false")) {
                    Modules.registerLocalModules(this, modules);
                } else if (Params.get("CONNECT_EXTERNAL_MODULES").equals("true")) {
                    Modules.registerAllModules(this, modules);
                }
            }
            log("Managers Iniciados");
        } catch (Exception ex) {
            log("Erro ao registrar os módulos do sistema: "+ex.getMessage());
            ex.printStackTrace();
        }
    }


	/**
	 * Call this method to register an ApplicationManager.
	 *
	 * @param manager The application manager to register.
	 */
	public void register(Class<? extends AbstractApplicationManager> manager) {

		if (initialized) throw new IllegalStateException("MultiApplicationManager is already initialized! Call register from registerManagers() method!");

		try {
			org.futurepages.core.control.AbstractApplicationManager newInstance = manager.newInstance();
			newInstance.setParent(this);
			this.managers.add(newInstance);
		} catch (Exception e) {
			throw new RuntimeException("Unable to instanciate the class: "
					+ manager.getSimpleName() + ".  Read the next stack for details.", e);
		}
	}

	@Override
	public final void init(Context application) {
		super.init(application);
		for (AbstractApplicationManager manager : this.managers) {
			manager.init(application);
		}
	}

	@Override
	public final void loadActions() {
		super.loadActions();
		for (AbstractApplicationManager manager : this.managers) {
			manager.loadActions();
		}
	}

	@Override
	public final void loadBeans() {
		super.loadBeans();
		for (AbstractApplicationManager manager : this.managers) {
			manager.loadBeans();
		}
	}

	@Override
	public final void loadLocales() {
		super.loadLocales();
		for (AbstractApplicationManager manager : this.managers) {
			manager.loadLocales();
		}
	}

	@Override
	public final void loadFormatters() {
		super.loadFormatters();
		for (AbstractApplicationManager manager : this.managers) {
			manager.loadFormatters();
		}
	}

	@Override
	public final void loadLists() throws IOException {
		super.loadLists();
		for (AbstractApplicationManager manager : this.managers) {
			manager.loadLists();
		}
	}

    private void log(String msg){
        System.out.println("[::appManager::] "+msg);
    }
}