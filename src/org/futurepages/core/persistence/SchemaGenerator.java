package org.futurepages.core.persistence;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.futurepages.util.FileUtil;
import org.futurepages.util.Is;
import org.hibernate.SQLQuery;


/**
 * Gerador de Schema. Herde esta classe e coloque dentro do pacote schema
 * do seu módulo caso queira executar antes da instalação alguma rotina sql.
 * Executado após a geração do schema a partir das Entitys que são Table
 * 
 * @author leandro
 */
public abstract class SchemaGenerator {

    public SchemaGenerator() {
		try {
			this.execute();
		} catch (Exception ex) {
			System.out.println("[::schema::] Error exporting... "+this.getClass().getName());
			ex.printStackTrace();
		}
    }

	protected void executeSQL(String sql){
		if(!Is.empty(sql)){
			System.out.println("[::schema::] "+sql);
			SQLQuery sqlQuery = HibernateManager.getSession().createSQLQuery(sql);
			sqlQuery.executeUpdate();
		}
	}

	protected void executeSQLFromFile(String path) throws FileNotFoundException, IOException{
		String script = FileUtil.getStringContent(this.getClass(), path);
		String[] commands = script.split(";");
		for(String command : commands){
			executeSQL(command);
		}
	}

	public void execute() throws Exception {}
	
}