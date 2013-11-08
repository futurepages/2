package org.futurepages.formatters;

import java.util.Collection;
import java.util.Locale;
import org.futurepages.core.formatter.Formatter;

/**
 * Exibe a quantidade de elementos da coleção.
 * .
 */
public class CollectionSizeFormatter extends Formatter {
    
    public String format(Object value, Locale loc) {
		if(value instanceof Object[]){
			return String.valueOf(((Object[])value).length);
		}
        return String.valueOf(((Collection) value).size());
    }
}