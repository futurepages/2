package org.futurepages.formatters;

import org.futurepages.util.MoneyUtil;
import java.util.Locale;
import org.futurepages.core.formatter.Formatter;

/**
 * Formata o double em formato de dinheiro local.
 */
public class MoneyFormatter implements Formatter {

	public String format(Object value, Locale loc) {
		if (value instanceof Float) {
			return MoneyUtil.moneyFormat(((Float)value).doubleValue());
		} else {
			return MoneyUtil.moneyFormat((Double) value);
		}
	}
}
