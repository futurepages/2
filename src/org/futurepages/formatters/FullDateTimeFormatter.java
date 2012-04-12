package org.futurepages.formatters;

import java.util.Calendar;
import java.util.Locale;
import org.futurepages.core.exception.DefaultExceptionLogger;
import org.futurepages.core.formatter.Formatter;
import org.futurepages.enums.MonthEnum;
import org.futurepages.util.NumberUtil;

/**
 * Formata a data no formato DD/MM/YYYY
 */
public class FullDateTimeFormatter implements Formatter<Calendar> {

	private boolean dateAndTime = true;

	public FullDateTimeFormatter(boolean onlyDate) {
		this.dateAndTime = onlyDate;
	}

	public String format(Calendar cal, Locale loc) {
		try {
			StringBuilder sb = new StringBuilder();
			int dia = cal.get(Calendar.DAY_OF_MONTH);
			if(dia==1){
				sb.append("primeiro");
			}else{
				sb.append(NumberUtil.numeroPorExteso(dia));
			}
			sb.append(" de ").append(MonthEnum.get(cal)).append("  de ");
			sb.append(NumberUtil.numeroPorExteso(cal.get(Calendar.YEAR)));


			if (dateAndTime) {
				int hora = cal.get(Calendar.HOUR_OF_DAY);
				int min = cal.get(Calendar.MINUTE);
				if (hora > 1) {
					sb.append(" às ");
				} else {
					sb.append(" à ");
				}
				if (hora == 1) {
					sb.append("uma");
				} else if (hora == 2) {
					sb.append("duas");
				} else if (hora == 21) {
					sb.append("vinte e uma");
				} else if (hora == 22) {
					sb.append("vinte e duas");
				} else {
					sb.append(NumberUtil.numeroPorExteso(hora));
				}
				if (hora > 1) {
					sb.append(" horas");
				} else {
					sb.append(" hora");
				}
				if (min > 0) {
					sb.append(" e ");
					sb.append(NumberUtil.numeroPorExteso(min));
					if (min != 1) {
						sb.append(" minutos");
					} else {
						sb.append(" minuto");
					}
				}
			}

			return sb.toString();
		} catch (Exception ex) {
			DefaultExceptionLogger.getInstance().execute(ex);
			return "";
		}

	}
}
