package org.futurepages.formatters;

import java.util.Calendar;
import java.util.Locale;

import org.futurepages.enums.UnitTimeEnum;
import org.futurepages.util.CalendarUtil;
import org.futurepages.util.DateUtil;
import org.futurepages.core.formatter.Formatter;

public class ElapsedTimeFormatter implements Formatter<Calendar> {
	
	public String format(Calendar calendar, Locale loc) {
		if (Calendar.class.isInstance(calendar)) {
			try {
				int[] time = CalendarUtil.getElapsedTime(calendar, Calendar.getInstance());
				return "há "+CalendarUtil.getElapsedTimeStatement(time, UnitTimeEnum.DAY, 7);

			} catch (CalendarUtil.TooBigDateException e) {
				return "em "+DateUtil.viewDateTime(calendar);
			}

		} else {
			return "";
		}
	}
}
