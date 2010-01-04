package org.futurepages.core.list;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.futurepages.core.control.AbstractApplicationManager;
import org.futurepages.core.i18n.LocaleManager;
import org.futurepages.util.InjectionUtils;

/**
 * @author Sergio Oliveira
 */
public class ListManager {
	
	private static final String SEP = File.separator;
	public static String LIST_DIR = "lists";
	private static final String FULLDIR = AbstractApplicationManager.getRealPath();
	
	private static Map<String, ListData> lists = new HashMap<String, ListData>();
	
	public static void setListDir(String listDir) {
		LIST_DIR = listDir.replace('\\', '/');
	}
	
	/**
	 * Load any list inside the "/lists" directory (default).
	 * 
	 * If the directory does not exist, ignore because the user probably doesn't want to load
	 * any list. He may not even know about this feature!
	 * 
	 * @throws IOException
	 */
	public static void init() throws IOException {
		
		String listDir = LIST_DIR.replace('\\', File.separatorChar).replace('/', File.separatorChar);
		
		if (listDir.indexOf(SEP) == 0 && listDir.length() > 1) {
			
			listDir = listDir.substring(1);
		}
		
		File root = new File(FULLDIR + SEP + listDir);
		
		if (!root.exists()) return;
		
		File [] files = root.listFiles();
		
		for(int i=0;i<files.length;i++) {
		
			File f = files[i];
			
			if (f.isDirectory()) continue;
			
			String filename = f.getName();
			
			if (!filename.endsWith(".i18n")) continue; // only load i18n files...
			
			int index = filename.indexOf(".i18n");
			
			String listname = filename.substring(0, index);
			
			index = listname.indexOf("_");
			
			if (index > 0) {
				
				// the listname is the name of the file without the locale info...
				
				listname = listname.substring(0, index);
			}
			
			// all locales will be loaded by BaseListData, so next time you see this list just skip it
			
			if (lists.get(listname) == null) {
				
				ListData list = new BaseListData(listname, BaseListData.ORDER_BY_VALUE, listDir);
				
				lists.put(listname, list);
				
			}
		}
	}
	
	/**
	 * Get a ListData by its name.
	 * 
	 * @param listname The name of the list to return
	 * @return The list (ListData) 
	 */
	public static ListData getList(String listname) {
		return lists.get(listname);
	}
	
	/**
	 * Add a ListData to this ListManager.
	 * 
	 * @param list The list to add
	 */
	public static void addList(ListData list) {
		lists.put(list.getName(), list);
	}
	
	/**
	 * Return all lists (ListData) in a java.util.List
	 * 
	 * @return The list of all ListDatas in this ListManager.
	 */
	public static List<ListData> getLists() {
		Collection<ListData> c = lists.values();
		List<ListData> list = new ArrayList<ListData>(c.size());
		Iterator<ListData> iter = c.iterator();
		while(iter.hasNext()) {
			list.add(iter.next());
		}
		return list;
	}
	
	public static ListData convert(Collection data, String keyProperty, String valueProperty) {
		
		return convert("", data, keyProperty, valueProperty);
	}
    
    public static ListData convert(String listName, Collection data, String keyProperty, String valueProperty) {
        
        SimpleListData list = new SimpleListData(listName);
        
        if (data.isEmpty()) return list;
        
        Method keyMethod = null;
        
        Method valueMethod = null;
        
        Field keyField = null;
        
        Field valueField = null;
        
        try {
        	
        	Iterator iter = data.iterator();
        	
        	int counter = 0;
            
            while(iter.hasNext()) {

            	counter++;
            	
                Object element = iter.next();
                
                if (counter == 1) {
                    
                    // do only on the first pass...
                    
                    keyMethod = InjectionUtils.findMethodToGet(element.getClass(), keyProperty);
                }
                
                String key = null;
                
                boolean ok = false;
                
                if (keyMethod == null) {
                    
                    if (counter == 1) {
                        
                        // do this only on the first pass...
                        
                        keyField = InjectionUtils.getField(element.getClass(), keyProperty);
                    }
                    
                    if (keyField != null) {
                    
                        key = (String) keyField.get(element);
                        
                        ok = true;
                        
                    }
                    
                } else {
                    
                    Object o = keyMethod.invoke(element,  new Object[0]);
                    
                    if (o != null) {
                    
                        key = o.toString();
                        
                        ok = true;
                        
                    }
                }
                
                if (!ok) {
                    
                    throw new RuntimeException("Unable find key for list: " + listName + " / " + keyProperty + " / " + valueProperty);
                }
                
                Object value = null;
                
                ok = false;
                
                if (counter == 1) {
                    
                    // do only on the first pass...
                    
                    valueMethod = InjectionUtils.findMethodToGet(element.getClass(), valueProperty);
                    
                }                
                
                if (valueMethod == null) {
                    
                    if (counter == 1) {
                        
                        // do only on the first pass...
                        
                        valueField = InjectionUtils.getField(element.getClass(), valueProperty);
                    }
                    
                    if (valueField != null) {
                    
                        value = valueField.get(element);
                        
                        ok = true;
                        
                    }
                    
                } else {
                    
                    value = valueMethod.invoke(element,  new Object[0]);
                    
                    ok = true;
                    
                }
                
                if (!ok) {
                    
                    throw new RuntimeException("Unable to find value for list: " + listName + " / " + keyProperty + " / " + valueProperty);
                }
                
                list.add(key, value.toString());
            }
            
        } catch (Exception e) {

            e.printStackTrace();
            
            throw new RuntimeException("Erro generating list: " + listName + " / " + keyProperty + " / " + valueProperty, e);
        }
        
        return list;
    }
    
    public static String getValue(String listName, int id) {
    	
    	return getValue(listName, String.valueOf(id));
    }
    
    public static String getValue(String listName, String id) {
    
    	ListData list = getList(listName);
    	
    	if (list == null) return null;
    	
    	if (list instanceof SimpleListData) {
    		
    		SimpleListData sld = (SimpleListData) list;
    		
    		return sld.getValue(id);
    		
    	}
    	
    	return list.getValue(id, LocaleManager.getDefaultLocale());
    }
    
    public static String getValue(String listName, int id, Locale loc) {
    	
    	return getValue(listName, String.valueOf(id), loc);
    }
    
    public static String getValue(String listName, String id, Locale loc) {
        
    	ListData list = getList(listName);
    	
    	if (list != null) {
    		
    		return list.getValue(id, loc);
    	}
    	
    	return null;
    }

    
    public static ListData convert(Collection data) {
    	
    	return convert("", data);
    	
    }
    
    public static ListData convert(String listName, Collection data) {
    	
    	SimpleListData list = new SimpleListData(listName);
    	
    	Iterator iter = data.iterator();
    	
    	while(iter.hasNext()) {
    		
    		String id;
    		
    		String value;
    		
    		Object obj = iter.next();
    		
    		if (obj instanceof ListItem) {
    			
    			ListItem item = (ListItem) obj;
    			
    			id = item.getKey();
    			
    			value = item.getValue();
    			
    		} else {
    			
    			id = value = obj.toString();
    			
    		}
    		
    		list.add(id, value);
    	}
    	
    	return list;
    	
    }
    
    public static ListData convert(String listName, Enum[] enums) {
    	
    	ListData list = new SimpleListData(listName);
    	
    	
    	for (Enum enumer : enums) {
    		String id;
    		
    		String value;
    		
    		if (enumer instanceof ListItem) {
    			
    			ListItem item = (ListItem) enumer;
    			
    			id = item.getKey();
    			
    			value = item.getValue();
    			
    		} else {
    			
    			id = enumer.name();
    			value = enumer.getClass().getSimpleName() + '.' + enumer.name();
    			
    		}
    		
       		((SimpleListData)list).add(id, value);
 		}
    	
    	return list;
    	
    }
    
    public static ListData convert(Map data) {
    	
    	return convert("", data);
    	
    }

    public static ListData convert(String listName, Map data) {
        
        SimpleListData list = new SimpleListData(listName);
        
        try {
            
            Iterator iter = data.keySet().iterator();
            
            for (int i = 0; i < data.size(); i++) {
                
                Object keyValue = iter.next();
                
                Object value = data.get(keyValue);
                
                list.add(keyValue.toString(), value.toString());
                
            }
            
        } catch (Exception e) {

            e.printStackTrace();
            
            throw new RuntimeException("Erro generating list: " + listName, e);
        }
        
        return list;
    }
    
    
}
