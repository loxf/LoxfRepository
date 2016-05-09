/**
 * ExportTest.java
 * {luohj} - 下午6:51:27
 * 
 */
package org.loxf.service;

import java.lang.reflect.InvocationTargetException;

import org.loxf.registry.bean.Service;
import org.loxf.registry.main.IProviderManager;
import org.loxf.registry.main.ProviderManager;
import org.loxf.registry.utils.ExportUtil;

/**
 * @author {luohj}
 *
 */
public class ExportTest {
	public static void main(String[] args) throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		Service[] services = ExportUtil.parse("org.loxf.service");
		IProviderManager provMgr = ProviderManager.getProviderManager();
		provMgr.export(services);
	}

}
