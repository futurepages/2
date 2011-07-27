package org.futurepages.formatters;

import java.util.Calendar;
import java.util.Locale;
import org.futurepages.core.formatter.Formatter;
import org.futurepages.enums.MonthEnum;

/**
 *
 * @author leandro
 */
public class LiteralAnniversaryFormatter implements Formatter {

	@Override
    public String format(Object value, Locale loc) {
    	String retornoFormater = "";
    	if(value!= null){
    		Calendar cal = (Calendar) value;
    		retornoFormater = cal.get(Calendar.DAY_OF_MONTH)+" de "+MonthEnum.get(cal);
    	}
		return retornoFormater;
    }

}
