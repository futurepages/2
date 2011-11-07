package org.futurepages.formatters;

import java.util.Calendar;
import java.util.Locale;

import org.futurepages.enums.UnitTimeEnum;
import org.futurepages.util.CalendarUtil;
import org.futurepages.util.DateUtil;
import org.futurepages.core.formatter.Formatter;

public class ElapsedTimeFormatter implements Formatter<Calendar> {

	@Override
	public String format(Calendar momentoNoPassado, Locale loc) {
		Calendar agora = Calendar.getInstance();
		return formatValue(agora, momentoNoPassado);
	}

	public static String formatValue(Calendar agora, Calendar momentoNoPassado){
		try {
			if(CalendarUtil.isNeighborDays(momentoNoPassado, agora) && CalendarUtil.getDifferenceInDays(momentoNoPassado, agora)>0){
					return "ontem às "+DateUtil.viewDateTime(momentoNoPassado, "HH:mm");
			}else{
				int[] time = CalendarUtil.getElapsedTime(momentoNoPassado, agora);
				return "há ± " + CalendarUtil.getElapsedTimeStatement(time, UnitTimeEnum.HOUR, 24, false);
			}
		} catch (CalendarUtil.TooBigDateException e) {
			return "em " + DateUtil.viewDateTime(momentoNoPassado);
		}
	}
}