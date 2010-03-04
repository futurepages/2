package org.futurepages.core.persistence;

import org.futurepages.util.Is;

/**
 * @author leandro
 */
public class HQLProvider implements HQLable {

	protected static HQLField field(String fieldName){
		return new HQLField(fieldName);
	}

	protected static String distinct(String selectClause) {
		if (!Is.empty(selectClause)) {
			return " DISTINCT " + selectClause;
		} else {
			return "";
		}
	}

	protected static String select(String selectClause) {
		if (!Is.empty(selectClause)) {
			return " SELECT " + selectClause;
		} else {
			return "";
		}
	}

	protected static String updateSetting(Class entityClass) {
		return " UPDATE " + entityClass.getName() + " SET ";
	}

	protected static String max(String maxClause) {
		if (!Is.empty(maxClause)) {
			return " MAX("+maxClause+")";
		} else {
			return "";
		}
	}

	protected static String min(String minClause) {
		if (!Is.empty(minClause)) {
			return " MIN("+minClause+")";
		} else {
			return "";
		}
	}

	protected static String count(String countClause) {
		if (!Is.empty(countClause)) {
			return " COUNT("+countClause+")";
		} else {
			return "";
		}
	}

	protected static HQLField day(String date) {
		return new HQLField(" DAY("+date+")");
	}

	protected static HQLField month(String date) {
		return new HQLField(" MONTH("+date+")");
	}

	protected static HQLField year(String date) {
		return new HQLField(" YEAR("+date+")");
	}

	protected static HQLField date(String date) {
		return new HQLField(" DATE("+date+")");
	}

	protected static String from(Class entityClass) {
		if (entityClass != null) {
			return " FROM " + entityClass.getName();
		} else {
			return "";
		}
	}

	protected static String from(Class entityClass,String alias) {
		if (entityClass != null) {
			return " FROM " + entityClass.getName()+" "+alias;
		} else {
			return "";
		}
	}

	protected static String join(String joinClause) {
		if (!Is.empty(joinClause)) {
			return " JOIN " + joinClause;
		} else {
			return "";
		}
	}

	protected static String where(String whereClause) {
		if (!Is.empty(whereClause)) {
			return " WHERE " + whereClause;
		} else {
			return "";
		}
	}

	protected static String orderBy(String... fields) {
		if (fields != null) {
			if(fields.length==1){
				if(!Is.empty(fields[0])){
					return " ORDER BY "+fields[0];
				}
			}
			else
				if(fields.length>1){
					String f =" ORDER BY ";
					for(String field : fields){
						f = f+field+",";
					}
					return  f.substring(0, f.length()-1);
				}
		}
		return "";
	}

	protected static String as(String alias) {
		if (!Is.empty(alias)) {
			return AS+alias;
		} else {
			return "";
		}
	}

	protected static String asc(String field) {
		return field+" ASC";
	}

	protected static String desc(String field) {
		return field+" DESC";
	}
	/**
	 * Monta conjun��es com as expres�es passadas 
	 * @param clauses : array de express�es booleanas
	 * @return (clauses[0]) AND (clauses[1]) AND ... AND (clauses[length-1]) 
	 */
	protected static String ands(String... clauses) {
		return connectClauses(AND, clauses);
	}

	/**
	 * @param clauses
	 * @return
	 */
	private static String connectClauses(String connector, String... clauses) {
		if (clauses == null || clauses.length == 0)
			return "";
		
		StringBuffer st = new StringBuffer();
		boolean primeiro = true;
		for (String clause : clauses) {
			if(primeiro){
				st.append(expressionBuilder("", clause));
				if(!Is.empty(clause)){
					primeiro = false;
				}
			}else{
				st.append(expressionBuilder(connector, clause));
			}
		}
		return st.toString();
	}
	
	private static String expressionBuilder(String conector, String clause) {
		if (Is.empty(clause))
			return "";
		return conector + "("+clause+")";
	}
	
	protected static String and(String clause) {
		if (Is.empty(clause))
			return "";
		return AND+"("+clause+")";
	}
	
	protected static String ors(String... clauses) {
		return connectClauses(OR, clauses);
	}

	protected static String or(String clause) {
		if (Is.empty(clause))
			return "";
		return OR+"("+clause+")";
	}

	protected static String groupBy(String groupClause) {
		if (!Is.empty(groupClause)) {
			return " GROUP BY " + groupClause;
		} else {
			return "";
		}
	}
}