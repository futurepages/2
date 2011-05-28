package org.futurepages.core.context;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * A simple Context that can be used for testing.
 * A SessionContext and an ApplicationContext can be mocked with this class.
 *
 * @author Sergio Oliveira
 */
public class MapContext implements Context {
    
    private Map<String, Object> values;
    
    public MapContext() {
        this.values = new HashMap<String, Object>();
    }
    
    public MapContext(Map<String, Object> values) {
        this.values = values;
    }
    
	public Object getAttribute(String name) {
        return values.get(name);
    }
	
	public void setAttribute(String name, Object value) {
        values.put(name, value);
    }
	
	public void removeAttribute(String name) {
        values.remove(name);
    }
	
	public Iterator<String> keys() {
		
		return values.keySet().iterator();
	}
	
	public void reset() {
        values.clear();
    }
	
	public boolean hasAttribute(String name) {
        return values.containsKey(name);
    }
}
