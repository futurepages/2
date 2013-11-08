package org.futurepages.core.formatter;

import java.util.Locale;

/**
 * This is a simple interface to format an object from the action output.
 * 
 * Check one of its implementations that comes with Mentawai before implementing this class.
 *
 * @author Sergio Oliveira
 */
public abstract class Formatter<T> {
    
    /**
     * Formats an output object from this action.
     *
     * @param value The value to format
     * @param loc The locale to use (if needed)
     * @return The value formatted to a String
     */
    public abstract String format(T value, Locale loc);
	
    public String format(T value, Locale loc, String param){
		return null;
	}
	
}
	
