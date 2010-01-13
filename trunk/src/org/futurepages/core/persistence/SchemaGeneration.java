package org.futurepages.core.persistence;

import org.hibernate.tool.hbm2ddl.SchemaExport;

public class SchemaGeneration {
    
    public static void update() {
            System.out.println("[::schema::] Schema-Generation UPDATE ---- BEGIN ----");
            System.out.println("[::schema::] Schema-Generation UPDATE ---- END ----");
    }

    public static void export()  {
            System.out.println("[::schema::] Schema-Generation EXPORT ---- BEGIN ----");
            SchemaExport schemaExport = new SchemaExport(HibernateManager.getConfiguration());
            schemaExport.create(true, true);
            System.out.println("[::schema::] Schema-Generation EXPORT ---- END ----");
    }
}