/**
 * MapUtils.java
 * luohj - 下午8:28:58
 * 
 */
package org.loxf.core.utils;

import java.util.HashMap;
import java.util.Iterator;

/**
 * @author luohj
 *
 */
public class MapUtils {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap clone(HashMap map) {   
		HashMap target = null;     
		if(map!=null){
			target = new HashMap();  
			synchronized(map){
		        for (Iterator keyIt = map.keySet().iterator(); keyIt.hasNext();) {
		            Object key = keyIt.next();    
		            target.put(key, map.get(key));    
		        }    
			}
		}
        return target;    
    }  
}
