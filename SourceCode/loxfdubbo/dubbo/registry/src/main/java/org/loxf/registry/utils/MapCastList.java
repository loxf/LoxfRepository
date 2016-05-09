/**
 * MapCastList.java
 * luohj - 下午6:05:06
 * 
 */
package org.loxf.registry.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author luohj
 *
 */
public class MapCastList {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<?> convert(Map<?, ?> map){
		if(map!=null){
			synchronized(map){
				List list = new ArrayList();
				Iterator it = map.keySet().iterator();
				while(it.hasNext()){
					list.add(map.get(it.next().toString()));
				}
				return list;
			}
		}
		return null;
	}
}
