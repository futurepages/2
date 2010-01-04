package org.futurepages.core.list;

import java.util.List;
import java.util.Locale;

/**
 * Defines how a data list works.
 *
 * @author Sergio Oliveira
 */
public interface ListData {
	
    /**
     * Returns the string value of the list data item with the given id in the given locale.
     *
     * @param id The id of the list data item.
     * @param loc The locale of the list data item.
     * @return The string value for the list data item.
     */
	public String getValue(String id, Locale loc);
	
	/**
	 * Returns the strign value fo the list data item with the default locale.
	 * 
	 * If there is no value for the default locale, use any locale available to
	 * return the value.
	 * 
	 * @param id
	 * @return The strint value of the list data item.
	 */
	public String getValue(String id);
	
    /**
     * Returns the string value of the list data item with the given id in the given locale.
     *
     * @param id The id of the list data item.
     * @param loc The locale of the list data item.
     * @return The string value for the list data item.
     */
	public String getValue(int id, Locale loc);
    
    /**
     * Returns a list of ListItem in the given locale.
     *
     * @param loc The locale of the ListItems.
     * @return A list of ListItems.
     */
	public List<ListItem> getValues(Locale loc);
	
	/**
	 * Returns a list of ListItem for the default locale.
	 * 
	 * If there is no list for the default locale, then
	 * try any locale in order to return a list.
	 * 
	 * @return A list of ListItems.
	 */
	public List<ListItem> getValues();
	
    
    /**
     * Returns the name of this list.
     *
     * @return The name of the list.
     */
	public String getName();
    
    /**
     * Returns the size of this list.
     * Note: the size does not depend on the number of locales.
     * 
     * @return The size of the list.
     */
	public int size();
	
}