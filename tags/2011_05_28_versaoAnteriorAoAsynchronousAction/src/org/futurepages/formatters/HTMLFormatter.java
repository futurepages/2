package org.futurepages.formatters;

import org.futurepages.util.The;
import java.util.Locale;
import org.futurepages.core.formatter.Formatter;

/**
 * Formata textos para exibir em p�ginas HTML sem erros,
 * retira s� as aspas para n�o quebrar dentro de atributos de tags html.
 * .
 */
public class HTMLFormatter implements Formatter {
    
    public String format(Object value, Locale loc) {
        return The.htmlSimpleValue((String) value);
    }
}