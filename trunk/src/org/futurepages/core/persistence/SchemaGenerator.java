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
			System.out.println("[::schema::] Error exporting... " + this.getClass().getName());
			ex.printStackTrace();
		}
	}

	protected void executeSQL(String sql) {
		if (!Is.empty(sql)) {
			System.out.println("[::schema::] " + sql);
			SQLQuery sqlQuery = HibernateManager.getSession().createSQLQuery(sql);
			sqlQuery.executeUpdate();
		}
	}

	protected void executeSQLFromFile(String path) throws FileNotFoundException, IOException {
		String[] sqls = FileUtil.getStringLines(this.getClass(), path);
		String trimmedSql = null;
		String delimiter = ";";
		StringBuffer sqlToExecute = new StringBuffer();

		for (int i = 0; i < sqls.length; i++) {
			trimmedSql = sqls[i].trim();
			if (trimmedSql.length() == 0
					|| trimmedSql.startsWith("--")
					|| trimmedSql.startsWith("//")
					|| trimmedSql.startsWith("/*")) {
				continue;
			} else {
				if (trimmedSql.length() > 10 && trimmedSql.substring(0, 10).toLowerCase().equals("delimiter ")) {
					delimiter = trimmedSql.substring(10);
				} else {
					if (i == sqls.length - 1 && !trimmedSql.endsWith(delimiter)) {
						sqlToExecute.append(trimmedSql);
						executeSQL(sqlToExecute.toString());
						sqlToExecute.delete(0, sqlToExecute.length());
					} else {
						if (trimmedSql.endsWith(delimiter)) {
							sqlToExecute.append(trimmedSql.substring(0, trimmedSql.length() - delimiter.length()));
							executeSQL(sqlToExecute.toString());
							sqlToExecute.delete(0, sqlToExecute.length());
						} else {
							sqlToExecute.append(trimmedSql + " ");
						}
					}
				}

			}
		}
	}

	public abstract void execute() throws Exception;
}
