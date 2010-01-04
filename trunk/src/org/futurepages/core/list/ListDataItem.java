package org.futurepages.core.list;

/**
 * @author Sergio Oliveira
 */
public class ListDataItem implements ListItem {
	
	private String id;
	private String value;
	private int idInteger;
	
	public ListDataItem(String id, String value) {
		this.id = id;
		this.value = value;
		
		try {
			
			idInteger = Integer.parseInt(id);
			
		} catch(NumberFormatException e) {
			
			idInteger = -1;
		}
	}
	
	public ListDataItem(int id, String value) {
		
		this(String.valueOf(id), value);
	}
	
	
	public int getId() { return idInteger; }
	
	public String getValue() { return value; }
	
	public String getKey() { return id; }
}
