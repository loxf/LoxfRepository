/**
 * ApplicationContext.java
 * luohj - 下午7:22:37
 * 
 */
package org.loxf.registry.context;

import java.util.HashMap;
import java.util.Map;

/**
 * @author luohj
 *
 */
public class ReferServiceContext {
	private static ReferServiceContext ctx ;
	private Map<String,Object > map ;
	ReferServiceContext(){
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
	
	public static ReferServiceContext getInstance(){
		if(ctx==null){
			synchronized(ReferServiceContext.class){
				if(ctx==null){
					ctx = new ReferServiceContext();
				}
			}
		}
		return ctx;
	}
}
