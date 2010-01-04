package org.futurepages.formatters;

import java.util.Locale;
import org.futurepages.util.SEOUtil;
import org.futurepages.core.formatter.Formatter;
 
/**
 * Formata��o de texto para SEO URL.
 *
 * Exemplo: Maria M�e de Jos� --> maria-mae-de-jose.html
 *  
 * - sa�da: DD/MM/YYYY HH:MM:SS
 *
 */
 public class SEOURLFormatter implements Formatter {
 	
 	public String format(Object value, Locale loc) {
            return SEOUtil.urlFormat(((String) value).trim());
 	}
 }