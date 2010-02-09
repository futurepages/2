package org.futurepages.formatters;

import java.util.Locale;
import org.futurepages.core.formatter.Formatter;

/**
 * Formata textos para exibir em p�ginas HTML sem erros,
 * retira s� as aspas para n�o quebrar dentro de atributos de tags html.
 * .
 */
public class JavascriptFormatter implements Formatter {
    
    public String format(Object value, Locale loc) {
		if(((String)value).contains("\n")){
			value = ((String)value).replaceAll("\n", "\\\\n");
		}
		if(((String)value).contains("\r")){
			value = ((String)value).replaceAll("\r", "\\\\r");
		}
		if(((String)value).contains("'")){
			value = ((String)value).replaceAll("'", "\\\\'");
		}
        return ((String) value);
    }
}