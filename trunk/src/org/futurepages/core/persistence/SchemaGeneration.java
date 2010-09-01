package org.futurepages.core.persistence;

import java.io.File;

import org.futurepages.util.ModuleUtil;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;

public class SchemaGeneration {

	public static void update() throws Exception  {
            log("Schema-Generation UPDATE ---- BEGIN ----");
            SchemaUpdate schemaUpdate = new SchemaUpdate(HibernateManager.getConfigurations().getTablesConfig());
            schemaUpdate.execute(true, true);
			
            File[] modules = ModuleUtil.getIstance().getModules();
            (new SchemaGeneratorsManager(modules)).execute();
			log("Found Exceptions while updating: \n"+schemaUpdate.getExceptions().toString());
            log("Schema-Generation UPDATE ---- END ----");
    }

    public static void export() throws Exception  {
            log("Schema-Generation EXPORT ---- BEGIN ----");
            SchemaExport schemaExport = new SchemaExport(HibernateManager.getConfigurations().getTablesConfig());
            schemaExport.create(true, true);
			(new SchemaGeneratorsManager(ModuleUtil.getIstance().getModules())).execute();
			log("Found Exceptions while exporting: \n"+schemaExport.getExceptions().toString());
            log("Schema-Generation EXPORT ---- END ----");
    }

	private static void log(String msg){
		System.out.println("[::schema::] "+msg);
	}
}