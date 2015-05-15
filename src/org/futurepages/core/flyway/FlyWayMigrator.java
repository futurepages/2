package org.futurepages.core.flyway;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;
import org.flywaydb.core.api.MigrationInfoService;
import org.futurepages.core.persistence.HibernateConfigurationFactory;
import org.futurepages.exceptions.ConfigFileNotFoundException;
import org.futurepages.util.Is;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

public class FlyWayMigrator {

	public static void main(String[] args) throws ConfigFileNotFoundException, InterruptedException, NamingException, IOException {

	}

	public static void execute() throws InterruptedException, NamingException, ConfigFileNotFoundException, IOException {
        Flyway flyway = new Flyway();
        flyway.setInitOnMigrate(true);
        configDataSource(flyway);

        int result = flyway.migrate();
        MigrationInfoService info = flyway.info();
        MigrationInfo[] applied = info.applied();
        for (MigrationInfo migrationInfo : applied) {
        	printMigration(migrationInfo);
		}
        System.out.println("Total de scripts executados "+result);
	}

	private static void configDataSource(Flyway flyway) throws NamingException, ConfigFileNotFoundException, IOException {
		Properties properties = HibernateConfigurationFactory.getInstance().getApplicationConfigurations().get("default").getEntitiesConfig().getProperties();
		boolean isDataSource = !Is.empty(properties.getProperty("hibernate.connection.datasource"));
		String url = properties.getProperty("hibernate.connection.url");
		String user = properties.getProperty("hibernate.connection.username");
		String pass = properties.getProperty("hibernate.connection.password");
//		System.out.println("DataSource: "+properties.getProperty("hibernate.connection.datasource"));
//		System.out.println("Url: "+url);
//		System.out.println("User: "+user);
//		System.out.println("Pass: "+pass);

        if(isDataSource){
        	Context initCtx = new InitialContext();
        	Context envCtx = (Context) initCtx.lookup("java:comp/env");
        	DataSource ds = (DataSource) envCtx.lookup("jdbc/intranet");
        	flyway.setDataSource(ds);
        }else{
        	flyway.setDataSource(url, user, pass);
        }
	}

	private static void printMigration(MigrationInfo info) throws NamingException {
		System.out.print(info.getVersion());
		System.out.print(" | ");
		System.out.print(info.getDescription());
		System.out.print(" 		| ");
		System.out.print(info.getScript());
		System.out.print(" 		| ");
		System.out.print(info.getInstalledOn());
		System.out.print(" 	| ");
		System.out.print(info.getExecutionTime());
		System.out.print(" 	| ");
		System.out.println();
	}
}
