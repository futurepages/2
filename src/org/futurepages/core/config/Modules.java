package org.futurepages.core.config;

import java.io.File;
import org.futurepages.core.ApplicationManager;
import org.futurepages.core.control.AbstractApplicationManager;

/**
 * Registrar os Módulos da aplicação
 */
public class Modules extends Mapper{
    /**
     * Registra o Gerenciador dos Módulos (ModuleManager) não comentado
     */
    public static void registerModule(ApplicationManager manager, File module) throws Exception {
        if (module.isDirectory()) {
            String moduleName = Params.MODULES_PACK + "." + module.getName() + ".ModuleManager";
            File moduleManagerFile = new File(Params.get("MODULES_CLASSES_REAL_PATH") + "/" + module.getName() + "/ModuleManager.class");
            //Registra o Manager do Módulo, caso ele exista.
            if (moduleManagerFile.exists()) {
                Class<? extends AbstractApplicationManager> moduleAppManager = (Class<? extends AbstractApplicationManager>) Class.forName(moduleName);
                manager.register(moduleAppManager);
            }
        }
    }

    /**
     * Registra os Gerenciadores de todos os Módulos (ModuleManager)
     * não comentados da aplicação
     */
    public static void registerAllModules(ApplicationManager manager, File[] modules) throws Exception {
        for (File module : modules) {
            registerModule(manager, module);
        }
    }

    /**
     * Registra somente os Gerenciadores dos Módulos (ModuleManager)
     * que acessam somente a base de dados local
     */
    public static void registerLocalModules(ApplicationManager manager, File[] modules) throws Exception {
        for (File module : modules) {
            if (!moduleHasDB(module)) {
                registerModule(manager, module);
            }
        }
    }

}