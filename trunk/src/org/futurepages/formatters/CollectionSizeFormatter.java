package org.futurepages.formatters;

import java.util.Collection;
import java.util.Locale;
import org.futurepages.core.formatter.Formatter;

/**
 * Exibe a quantidade de elementos da coleção.
 * .
 */
public class CollectionSizeFormatter implements Formatter {
    
    public String format(Object value, Locale loc) {
		
        return String.valueOf(((Collection) value).size());
    }
}