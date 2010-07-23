package org.futurepages.core.persistence;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.futurepages.util.FileUtil;
import org.futurepages.util.Is;
import org.hibernate.SQLQuery;


/**
 * Gerador de Schema. Herde esta classe e coloque dentro do pacote schema
 * do seu m�dulo caso queira executar antes da instala��o alguma rotina sql.
 * Executado ap�s a gera��o do schema a partir das Entitys que s�o Table
 * 
 * @author leandro
 */
public abstract class SchemaGenerator {

    public SchemaGenerator() {
		try {
			System.out.println("come�ou a executar...");
			this.execute();
			System.out.println("executou.");
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
		System.out.println("vou carregar o arquivo no path >> "+path);
		String script = FileUtil.getStringContent(this.getClass(), path);
		System.out.println("carreguei o arquivo.");
		String[] commands = script.split(";");
		System.out.println("len: "+commands.length);
		for(String command : commands){
			executeSQL(command);
		}
	}

	public void execute() throws Exception {}
	
}