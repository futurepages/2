package org.futurepages.formatters;

import org.futurepages.util.HtmlMapChars;
import java.util.Locale;
import org.futurepages.core.formatter.Formatter;
 
/**
 * Formata o texto escrito em textaerea em texto com quebras de linhas de html.
 */
 public class TextAreaFormatter implements Formatter {
 	
 	public String format(Object value, Locale loc) {
            return HtmlMapChars.textAreaValue((String) value);
 	}
 }