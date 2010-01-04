package org.futurepages.formatters;

import org.futurepages.util.DateUtil;
import java.util.Locale;
import org.futurepages.core.formatter.Formatter;
 
/**
 * Formata��o de data e hora:
 *  
 * - sa�da: DD/MM/YYYY HH:MM:SS
 *
 */
 public class DateTimeFormatter implements Formatter {
 	
 	public String format(Object value, Locale loc) {
			return DateUtil.viewDateTime(value);
	}
 }