package org.futurepages.core.persistence;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.futurepages.util.DateUtil;
import org.futurepages.util.HQLUtil;
import org.futurepages.util.Is;
import org.futurepages.util.The;

/**
 *
 * @author leandro
 */
public class HQLField implements HQLable {

    private String fieldName;

    public HQLField(String fieldName) {
        this.fieldName = fieldName;
    }

    public String between(String dateBegin, String dateEnd) {
        return "((" + fieldName + GREATER_EQUALS + "'" + esc(dateBegin) + "') " + AND + " (" + fieldName + LOWER_EQUALS + "'" + esc(dateEnd) + "'))";
//		return HQLProvider.ands( greaterEqualsThen(dateBegin) ,lowerEqualsThen(dateEnd) );
    }

    public String inDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return inDate(cal);
    }

    public String inDate(int year, int month, int day) {
        GregorianCalendar cal = new GregorianCalendar(year, month - 1, day);
        return inDate(cal);
    }

    public String inDate(Calendar calendar) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(calendar.getTime());

        cal.set(Calendar.HOUR_OF_DAY, cal.getActualMinimum(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal.getActualMinimum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getActualMinimum(Calendar.SECOND));
        String dateBegin = DateUtil.dbDateTime(cal.getTime());


        cal.set(Calendar.HOUR_OF_DAY, cal.getActualMaximum(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal.getActualMaximum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getActualMaximum(Calendar.SECOND));
        String dateEnd = DateUtil.dbDateTime(cal.getTime());



        return this.between(dateBegin, dateEnd);
    }

    public String equalsTo(String value) {
        if (Is.empty(value)) {
            return "";
        }
        return fieldName + " = '" + escCote(value) + "'";
    }

    /**
     *
     * Falta implementar o método {@link HQLField#matches(java.lang.String)}
     */
    public String matches(String value) {
        if (Is.empty(value)) {
            return "";
        }
        return HQLUtil.matches(fieldName, esc(value));
    }

    public String is(Boolean bool) {
        if (Is.empty(bool)) {
            return "";
        }
        return fieldName + " is " + bool;
    }

    public String equalsTo(long value) {
        return fieldName + " = " + value;
    }

    public String equalsTo(Integer value) {
        if (value == null) {
            return "";
        }
        return fieldName + " = " + value;
    }

    public String equalsTo(Long value) {
        if (value == null) {
            return "";
        }
        return fieldName + " = " + value;
    }

    public String equalsTo(Boolean value) {
        if (value == null) {
            return isNull();
        } else if (value == false) {
            return isFalse();
        }
        return isTrue();
    }

    public String differentFrom(String value) {
        if (Is.empty(value)) {
            return "";
        }
        return fieldName + " != '" + esc(value) + "'";
    }

    public String differentFrom(int value) {
        return fieldName + " != " + value;
    }

    public String differentFrom(long value) {
        return differentFrom(new Long(value));
    }

    public String differentFrom(Integer value) {
        return differentFrom(value.intValue());
    }

    public String differentFrom(Long value) {
        return differentFrom(value.intValue());
    }

    public String greaterThen(String value) {
        return fieldName + GREATER + "'" + esc(value) + "'";
    }

    public String greaterThen(long value) {
        return fieldName + GREATER + value;
    }

    public String greaterEqualsThen(String value) {
        return fieldName + GREATER_EQUALS + "'" + esc(value) + "'";
    }

    public String greaterEqualsThen(long value) {
        return fieldName + GREATER_EQUALS + value;
    }

    public String lowerThen(String value) {
        return fieldName + LOWER + "'" + esc(value) + "'";
    }

    public String lowerThen(long value) {
        return fieldName + LOWER + value;
    }

    public String lowerEqualsThen(String value) {
        return fieldName + LOWER_EQUALS + "'" + esc(value) + "'";
    }

    public String lowerEqualsThen(long value) {
        return fieldName + LOWER_EQUALS + value;
    }

    public String hasAllWordsInSameSequence(String[] words) {
        if (words == null) {
            return "";
        }
        return HQLUtil.fieldHasAllWordsInSameSequence(fieldName, words);
    }

    public String hasAllWordsInSameSequence(String words) {
        if (words == null) {
            return "";
        }
        return HQLUtil.fieldHasAllWordsInSameSequence(fieldName, words);
    }

    public String hasAllWords(String[] tokens) {
        if (tokens == null) {
            return "";
        }
        return HQLUtil.fieldHasWords(fieldName, tokens, AND);
    }

    public String hasAllWords(String tokens) {
        if (Is.empty(tokens)) {
            return "";
        }
        return HQLUtil.fieldHasWords(fieldName, The.explodedToArray(tokens, " "), AND);
    }

    public String hasAnyOfWords(String[] tokens) {
        if (tokens == null) {
            return "";
        }
        return HQLUtil.fieldHasWords(fieldName, tokens, OR);
    }

    public String isTrue() {
        return fieldName + " = true";
    }

    public String isFalse() {
        return fieldName + " = false";
    }

    public String isNull() {
        return fieldName + " = null";
    }

    public String isNotNull() {
        return fieldName + " != null";
    }

    private String buildlStringExpression(String logicConector, String... tokens) {
        if (tokens == null || tokens.length == 0) {
            return "";
        }
        return fieldName + " " + logicConector + " (" + HQLUtil.imploded(tokens) + ")";
    }

    private String buildlLongExpression(String logicConector, long... tokens) {
        if (tokens == null || tokens.length == 0) {
            return "";
        }
        return fieldName + " " + logicConector + " (" + HQLUtil.imploded(tokens) + ")";
    }

    public String in(String... tokens) {
        return buildlStringExpression("IN", tokens);
    }

    public String inSubQuery(String subQuery) {
        return fieldName + " IN " + "(" + subQuery + ")";
    }

    public String in(long... tokens) {
        return buildlLongExpression("IN", tokens);
    }

    public String notIn(String... tokens) {
        return buildlStringExpression("NOT IN", tokens);
    }

    public String notIn(List<String> elements) {
        return fieldName + " NOT IN (" + HQLUtil.imploded(elements) + ")";
    }

    public String notIn(String tokensStr) {
        return fieldName + " NOT IN (" + HQLUtil.imploded(tokensStr) + ")";
    }

    public String notIn(long... tokens) {
        return buildlLongExpression("NOT IN", tokens);
    }

    public String startsWith(String value) {
        if (Is.empty(value)) {
            return "";
        }
        return fieldName + LIKE + "'" + esc(value) + "%'";
    }

    public String endsWith(String value) {
        if (Is.empty(value)) {
            return "";
        }
        return fieldName + LIKE + "'%" + esc(value) + "'";
    }

    public String contains(String value) {
        if (Is.empty(value)) {
            return "";
        }
        return fieldName + LIKE + "'%" + esc(value) + "%'";
    }

    public String as(String alias) {
        return fieldName + AS + alias;
    }

    @Override
    public String toString() {
        return this.fieldName;
    }

    private String esc(String hql) {
        return HQLUtil.esc(hql);
    }

    private String escCote(String hql) {
        return HQLUtil.escQuote(hql);
    }

    /**
     * utilize o #equalsTo()
     *
     */
    @Override
    @Deprecated
    public boolean equals(Object obj) {
        return false;
    }

    private String timeExpression(Calendar cal, String comparator) {
        return fieldName + comparator + "'" + esc(DateUtil.dbDateTime(cal.getTime())) + "'";
    }

    public String greaterEqualsThen(Calendar cal) {
        return timeExpression(cal, GREATER_EQUALS);
    }

    public String lowerEqualsThen(Calendar cal) {
        return timeExpression(cal, LOWER_EQUALS);
    }
}
