package org.futurepages.formatters;

import java.text.DecimalFormat;
import java.util.Locale;
import org.futurepages.core.formatter.Formatter;

/**
 *
 * @author leandro
 */
public class FloatFormatter implements Formatter {

    public String format(Object value, Locale locale) {
        DecimalFormat df = new DecimalFormat("#.#");
        return df.format(value);
    }
}