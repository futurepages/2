package org.futurepages.formatters;

import org.futurepages.core.formatter.Formatter;
import org.futurepages.util.CalendarUtil;
import org.futurepages.util.DateUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Formata uma data String como entrada "YYYY-MM-DD"  e retorna no formato formato DD/MM/YYYY
 */
 public class DBDateFormatter implements Formatter {
 	
 	public String format(Object value, Locale loc) {
        if(value instanceof Date){
            return DateUtil.dbDate((Date) value);
        }else{
            if(value instanceof Calendar){
                return DateUtil.dbDate((Calendar) value);
            }
        }
        return "";
 	}
 }