package org.futurepages.core.persistence;

import org.hibernate.tool.hbm2ddl.SchemaExport;

public class SchemaGeneration {
    
    public static void update() {
            System.out.println("[::w7i::] Schema-Generation UPDATE ---- BEGIN ----");
            System.out.println("[::w7i::] Schema-Generation UPDATE ---- END ----");
    }

    public static void export()  {
            System.out.println("[::w7i::] Schema-Generation EXPORT ---- BEGIN ----");
            SchemaExport schemaExport = new SchemaExport(HibernateManager.getConfiguration());
            schemaExport.create(true, true);
            System.out.println("[::w7i::] Schema-Generation EXPORT ---- END ----");
    }
}