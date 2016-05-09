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
public class ApplicationContext {
	private static ApplicationContext ctx ;
	private Map<String,Object > map ;
	ApplicationContext(){
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
	
	public static ApplicationContext getInstance(){
		if(ctx==null){
			synchronized(ApplicationContext.class){
				if(ctx==null){
					ctx = new ApplicationContext();
				}
			}
		}
		return ctx;
	}
}
