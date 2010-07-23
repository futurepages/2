package org.futurepages.core.persistence;

import java.io.File;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;

public class SchemaGeneration {
    
    public static void update(File[] modules)  throws Exception  {
            System.out.println("[::schema::] Schema-Generation UPDATE ---- BEGIN ----");
            SchemaUpdate schemaUpdate = new SchemaUpdate(HibernateManager.getConfigurations().getTablesConfig());
            schemaUpdate.execute(true, true);
			(new SchemaGeneratorsManager(modules)).execute();
            System.out.println("[::schema::] Schema-Generation UPDATE ---- END ----");
    }

    public static void export(File[] modules) throws Exception  {
            System.out.println("[::schema::] Schema-Generation EXPORT ---- BEGIN ----");
            SchemaExport schemaExport = new SchemaExport(HibernateManager.getConfigurations().getTablesConfig());
            schemaExport.create(true, true);
			(new SchemaGeneratorsManager(modules)).execute();
            System.out.println("[::schema::] Schema-Generation EXPORT ---- END ----");
    }
}