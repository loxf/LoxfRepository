/**
 * BaseContext.java
 * luohj - 上午10:41:19
 * 
 */
package org.loxf.core.context;

import java.util.HashMap;
import java.util.Map;

/**
 * @author luohj
 *
 */
public class BaseContext {
	private Map<String,Object > map ;
	
	/**
	 * @return the map
	 */
	public Map<String, Object> getMap() {
		return map;
	}

	BaseContext(){
		map = new HashMap<String, Object> ();
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getBean(String key){
		return (T) map.get(key);
	}
	
	public void setBean(String key, Object o){
		map.put(key, o);
	}
	
	public boolean isExistsBean(String key){
		return map.containsKey(key);
	}
	
}
