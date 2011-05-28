package org.futurepages.formatters;

import java.util.Calendar;
import java.util.Locale;
import org.futurepages.core.formatter.Formatter;
import org.futurepages.enums.MonthEnum;
import org.futurepages.util.DateUtil;

/**
 *
 * @author leandro
 */
public class LiteralAnniversaryFormatter implements Formatter {

    public String format(Object value, Locale loc) {
    	String retornoFormater = "";
    	if(value!= null){
    		Calendar cal = (Calendar) value;
    		retornoFormater = DateUtil.format(cal, "d")+" de "+MonthEnum.get(cal);
    	}
		return retornoFormater;
    }

}
