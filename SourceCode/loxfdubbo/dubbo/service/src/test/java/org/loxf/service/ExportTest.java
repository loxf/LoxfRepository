/**
 * ExportTest.java
 * {luohj} - 下午6:51:27
 * 
 */
package org.loxf.service;

import java.lang.reflect.InvocationTargetException;

import org.loxf.registry.main.IProviderManager;
import org.loxf.registry.main.ProviderManager;

/**
 * @author {luohj}
 *
 */
public class ExportTest {
	/**
	 * TODO 启动服务端MAIN方法
	 * @param args
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @author:luohj
	 */
	public static void main(String[] args) throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		IProviderManager mgr = ProviderManager.getProviderManager();
		mgr.export();
	}

}
