/**
 * ReferContext.java
 * luohj - 下午5:20:22
 * 
 */
package org.loxf.registry.context;

import java.lang.reflect.InvocationTargetException;

import org.loxf.core.context.ApplicationContext;
import org.loxf.registry.utils.ExportUtil;

/**
 * @author luohj
 *
 */
public class ReferContext {
	
	@SuppressWarnings("unchecked")
	public <T> void loadLocalService(String path){
		T[] services = (T[])parseService(path);
		ApplicationContext.getInstance().setLocalBeans(services);
	}
	
	@SuppressWarnings("unchecked")
	<T>T[] parseService(String path) {
		try {
			return (T[]) ExportUtil.parse(path);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}
}
