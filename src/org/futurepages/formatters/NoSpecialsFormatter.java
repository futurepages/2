package org.futurepages.formatters;

import java.util.Locale;
import org.futurepages.core.formatter.Formatter;
 
 public class NoSpecialsFormatter extends Formatter {
 	
 	public String format(Object value, Locale loc) {
            return ((String)value).replace(".", "_");
 	}
 }