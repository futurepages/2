package org.futurepages.core.persistence;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.futurepages.core.tags.build.ModulesAutomation;

/**
 *  Resposável pela geração de schema após a geração.
 *
 * @author leandro
 */
public class SchemaGeneratorsManager extends ModulesAutomation {

	private static final String SCHEMA_DIR_NAME = "schema";

	public SchemaGeneratorsManager(File[] modules) {
		super(modules, SCHEMA_DIR_NAME);
	}

	public void execute() throws Exception {
		executeSQLFromModules();
		executeSQLAfterModules();
	}

	private void executeSQLFromModules() throws Exception {
		Map<String, List<Class<SchemaGenerator>>> classes = getModulesDirectoryClasses(SchemaGenerator.class, null);
		for (String moduleName : classes.keySet()) {
			if (classes.get(moduleName).size() > 0) {
				log("module '" + moduleName + "' generating schema...");
				for (Class<?> generator : classes.get(moduleName)) {
					log(">>> " + generator.getSimpleName() + " running...  ");
					generator.newInstance();
					log(">>> " + generator.getSimpleName() + " OK");
				}
				log("module '" + moduleName + "' generated.");
			}
		}
	}

	private void executeSQLAfterModules() throws Exception {
			final String SCHEMA_SCRIPTS_NAME =  SCHEMA_DIR_NAME + ".SchemaScripts";
			try {
				Class schemaGenerator = Class.forName(SCHEMA_SCRIPTS_NAME);
				log(">>> " + schemaGenerator.getSimpleName() + " running...  ");
				schemaGenerator.newInstance();
				log(">>>  SchemaScripts DONE.");
			} catch (ClassNotFoundException ex) {
				log(">>> "+SCHEMA_SCRIPTS_NAME+" not present.");
			}
	}


	private void log(String msg) {
		System.out.println("[::schema-manager::] " + msg);
	}
}