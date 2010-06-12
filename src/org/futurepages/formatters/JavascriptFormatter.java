package org.futurepages.formatters;

import java.util.Locale;
import org.futurepages.core.formatter.Formatter;
import org.futurepages.util.The;

/**
 * Formata textos para exibir em p�ginas HTML sem erros,
 * retira s� as aspas para n�o quebrar dentro de atributos de tags html.
 * .
 */
public class JavascriptFormatter implements Formatter {
    
	@Override
    public String format(Object value, Locale loc) {
        return The.javascriptText(value);
    }
}