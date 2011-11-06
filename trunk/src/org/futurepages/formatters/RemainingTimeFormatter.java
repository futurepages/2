package org.futurepages.formatters;

import java.util.Calendar;
import java.util.Locale;

import org.futurepages.enums.UnitTimeEnum;
import org.futurepages.util.CalendarUtil;
import org.futurepages.util.DateUtil;
import org.futurepages.core.formatter.Formatter;

//TODO - falta contemplar o "amanhã"
public class RemainingTimeFormatter implements Formatter<Calendar> {

	@Override
	public String format(Calendar value, Locale loc) {
		try {
			int[] time = CalendarUtil.getElapsedTime(value, Calendar.getInstance());
			return "em " + CalendarUtil.getElapsedTimeStatement(time, UnitTimeEnum.DAY, 7);

		} catch (CalendarUtil.TooBigDateException e) {
			return "em " + DateUtil.viewDateTime(value);
		}
	}
}