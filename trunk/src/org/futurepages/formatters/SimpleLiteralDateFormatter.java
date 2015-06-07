package org.futurepages.formatters;

import org.futurepages.core.formatter.Formatter;
import org.futurepages.enums.MonthEnum;
import org.futurepages.util.StringUtils;

import java.util.Calendar;
import java.util.Locale;

public class SimpleLiteralDateFormatter implements Formatter<Calendar> {

	@Override
	public String format(Calendar momentoNoPassado, Locale loc) {
		Calendar agora = Calendar.getInstance();
		return formatValue(agora, momentoNoPassado);
	}

	public static String formatValue(Calendar agora, Calendar momentoInput){
		int anoAtual = agora.get(Calendar.YEAR);
		int diaInput = momentoInput.get(Calendar.DAY_OF_MONTH);
		int mesInput = momentoInput.get(Calendar.MONTH) + 1;
		int anoInput = momentoInput.get(Calendar.YEAR);
		String dia = (diaInput == 1 ? "1º" : String.valueOf(diaInput));
		String mes = MonthEnum.get(mesInput);
		String ano = "";
		if ((anoInput != anoAtual)) {
			ano = " de " + anoInput;
		}
		return StringUtils.concat(dia, " de ", mes, ano);
	}
}