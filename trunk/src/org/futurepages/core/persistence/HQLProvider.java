package org.futurepages.core.persistence;

import org.futurepages.util.Is;
import org.futurepages.util.StringUtils;

/**
 * @author leandro
 */
public class HQLProvider implements HQLable {

	public static HQLField field(String fieldName){
		return new HQLField(fieldName);
	}

	public static String distinct(String selectClause) {
		if (!Is.empty(selectClause)) {
			return " DISTINCT " + selectClause;
		} else {
			return "";
		}
	}

	public static String select(String selectClause) {
		if (!Is.empty(selectClause)) {
			return " SELECT " + selectClause;
		} else {
			return "";
		}
	}

	public static String updateSetting(Class entityClass) {
		return concat(" UPDATE " , entityClass.getName() , " SET ");
	}

	public static String max(String maxClause) {
		if (!Is.empty(maxClause)) {
			return concat(" MAX(",maxClause,")");
		} else {
			return "";
		}
	}

	public static String min(String minClause) {
		if (!Is.empty(minClause)) {
			return concat(" MIN(",minClause,")");
		} else {
			return "";
		}
	}

	public static String count(String countClause) {
		if (!Is.empty(countClause)) {
			return concat(" COUNT(",countClause,")");
		} else {
			return "";
		}
	}

	public static String sum(String sumClause) {
		if (!Is.empty(sumClause)) {
			return concat(" SUM(",sumClause,")");
		} else {
			return "";
		}
	}

	public static HQLField day(String date) {
		return new HQLField(concat(" DAY(",date,")"));
	}

	public static HQLField month(String date) {
		return new HQLField(concat(" MONTH(",date,")"));
	}

	public static HQLField year(String date) {
		return new HQLField(concat(" YEAR(",date,")"));
	}

	public static HQLField date(String date) {
		return new HQLField(concat(" DATE(",date,")"));
	}

	public static String from(Class entityClass) {
		if (entityClass != null) {
			return " FROM " + entityClass.getName();
		} else {
			return "";
		}
	}

	public static String from(Class entityClass,String alias) {
		if (entityClass != null) {
			return concat(" FROM ",entityClass.getName()," ",alias);
		} else {
			return "";
		}
	}

	public static String join(String joinClause) {
		if (!Is.empty(joinClause)) {
			return JOIN + joinClause;
		} else {
			return "";
		}
	}

	public static String notExists(String clause) {
		if(!Is.empty(clause)) {
			return NOT_EXISTS + "(" + clause + ")";
		} else {
			return "";
		}
	}

	public static String where(String whereClause) {
		if (!Is.empty(whereClause)) {
			return WHERE + whereClause;
		} else {
			return "";
		}
	}

	public static String orderBy(String... fields) {
		if (fields != null) {
			if(fields.length==1){
				if(!Is.empty(fields[0])){
					return ORDER_BY+fields[0];
				}
			}
			else
				if(fields.length>1){
					StringBuilder sb = new StringBuilder(ORDER_BY);
					for(String field : fields){
						sb.append(field).append(",");
					}
					String result = sb.toString();
					return  result.substring(0, result.length()-1);
				}
		}
		return "";
	}

	public static String as(String alias) {
		if (!Is.empty(alias)) {
			return AS+alias;
		} else {
			return "";
		}
	}

	public static String asc(String field) {
		return field+ASC;
	}

	public static String desc(String field) {
		return field+DESC;
	}
	/**
	 * Monta conjun��es com as expres�es passadas 
	 * @param clauses : array de express�es booleanas
	 * @return (clauses[0]) AND (clauses[1]) AND ... AND (clauses[length-1]) 
	 */
	public static String ands(String... clauses) {
		return connectClauses(AND, clauses);
	}

	/**
	 * @param clauses
	 * @return
	 */
	private static String connectClauses(String connector, String... clauses) {
		if (clauses == null || clauses.length == 0) {
			return "";
		}
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
		return concat(conector ,"(",clause,")");
	}
	
	public static String and(String clause) {
		if (Is.empty(clause))
			return "";
		return concat(AND,"(",clause,")");
	}
	
	public static String ors(String... clauses) {
		return connectClauses(OR, clauses);
	}

	public static String or(String clause) {
		if (Is.empty(clause))
			return "";
		return concat(OR,"(",clause,")");
	}

	public static String groupBy(String groupClause) {
		if (!Is.empty(groupClause)) {
			return GROUP_BY + groupClause;
		} else {
			return "";
		}
	}

	public static String concat(String... args){
	   return StringUtils.concat(args);
	}
}