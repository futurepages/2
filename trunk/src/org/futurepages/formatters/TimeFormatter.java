package org.futurepages.formatters;

import org.futurepages.core.formatter.AbstractFormatter;
import org.futurepages.util.DateUtil;
import org.futurepages.util.Is;

import java.util.Locale;

/**
 * Formata uma data String cou Date ou Calendar em HH:mm
 */
 public class TimeFormatter extends AbstractFormatter {

    @Override
 	public String format(Object value, Locale loc) {
            return DateUtil.viewDateTime(value, "HH:mm:ss");
 	}

	@Override
    public String format(Object value, Locale locale, String param) {
        return DateUtil.viewDateTime(value, Is.empty(param)? "HH:mm:ss":param);
    }
 }