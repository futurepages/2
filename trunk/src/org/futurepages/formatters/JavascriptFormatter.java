package org.futurepages.formatters;

import java.util.Locale;
import org.futurepages.core.formatter.Formatter;

/**
 * Formata textos para exibir em páginas HTML sem erros,
 * retira só as aspas para não quebrar dentro de atributos de tags html.
 * .
 */
public class JavascriptFormatter implements Formatter {
    
    public String format(Object value, Locale loc) {
		if(((String)value).contains("'")){
			return ((String)value).replaceAll("'", "\\\\'");
		}
        return ((String) value);
    }
}