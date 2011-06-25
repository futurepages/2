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
//			if(schemaUpdate.getExceptions().size()>0){
//				log("Found "+schemaUpdate.getExceptions().size()+" Exception(s) while updating");
//				for (Object obj : schemaUpdate.getExceptions()) {
////					if(!((Exception)obj).getMessage().contains("doesn't exist")){ //comentaado para entender o pq deste if
//						log("  "+((Exception)obj).getMessage());
////					}
//				}
//			}
            log("Schema-Generation UPDATE ---- END ----");
    }

    public static void export() throws Exception  {
            log("Schema-Generation EXPORT ---- BEGIN ----");
            SchemaExport schemaExport = new SchemaExport(HibernateManager.getConfigurations().getTablesConfig());
            schemaExport.create(true, true);
			(new SchemaGeneratorsManager(ModuleUtil.getIstance().getModules())).execute();

//			if(schemaExport.getExceptions().size()>0){
//				log("Found "+schemaExport.getExceptions().size()+" Exception(s) while exporting");
//				for (Object obj : schemaExport.getExceptions()) {
//					if(!((Exception)obj).getMessage().contains("doesn't exist")){ //comentaado para entender o pq deste if
//						log("  "+((Exception)obj).getMessage());
//					}
//				}
//			}
            log("Schema-Generation EXPORT ---- END ----");
    }

	private static void log(String msg){
		System.out.println("[::schema-generation::] "+msg);
	}
}