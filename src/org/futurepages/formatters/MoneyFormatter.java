package org.futurepages.formatters;

import java.math.BigDecimal;
import org.futurepages.util.MoneyUtil;
import java.util.Locale;
import org.futurepages.core.formatter.Formatter;

/**
 * Formata o double em formato de dinheiro local.
 */
public class MoneyFormatter implements Formatter {

	@Override
	public String format(Object value, Locale loc) {
		if(value == null){
			return "0,00"; //TODO temporário - formato exclusivamente brasileiro.
		}
		if (value instanceof Float) {
			return MoneyUtil.moneyFormat(((Float)value).doubleValue());
		} else if(value instanceof Double) {
			return MoneyUtil.moneyFormat((Double) value);
		} else {
			return MoneyUtil.moneyFormat((BigDecimal) value);
		}
	}
}
