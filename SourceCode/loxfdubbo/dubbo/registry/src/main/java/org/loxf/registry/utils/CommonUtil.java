/**
 * CommonUtil.java
 * luohj - 下午4:48:07
 * 
 */
package org.loxf.registry.utils;

import org.apache.commons.lang.StringUtils;

/**
 * @author luohj
 *
 */
public class CommonUtil {
	public static int valueofInt(String str, int defaultValue){
		if(StringUtils.isEmpty(str)){
			return defaultValue;
		} else {
			return Integer.valueOf(str);			
		}
	}
	public static String valueofString(String str, String defaultValue){
		if(StringUtils.isEmpty(str)){
			return defaultValue;
		} else {
			return str;			
		}
	}
	public static boolean contains(Object[] arr, Object o){
		for(Object tmp : arr){
			if(tmp.equals(o)){
				return true;
			}
		}
		return false;
	}
}
