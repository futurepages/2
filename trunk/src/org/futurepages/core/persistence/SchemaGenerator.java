package org.futurepages.core.persistence;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.futurepages.core.exception.DefaultExceptionLogger;
import org.futurepages.util.FileUtil;

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
			this.execute();
		} catch (Exception ex) {
			System.out.println("[::schema-generator::] Error exporting... " + this.getClass().getName());
			DefaultExceptionLogger.getInstance().execute(ex);
		}
	}

	protected void executeSQL(String sql) {
			System.out.println("[::schema-generator::] " + sql);
			Dao.executeSQL(sql);
	}

	protected void executeSQLFromFile(String path) throws FileNotFoundException, IOException {
		String[] sqls = FileUtil.getStringLines(this.getClass(), path);
		Dao.executeSQLs(sqls);
	}

	public abstract void execute() throws Exception;
}
