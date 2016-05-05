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
import org.loxf.service.impl.HelloWorldImpl;
import org.loxf.service.impl.WhoBoy;
import org.loxf.service.impl.WhoGirl;

/**
 * @author {luohj}
 *
 */
public class ExportTest {
	public static void main(String [] args) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		Service[] services= new Service[3];
		services[0] = ExportUtil.parse(HelloWorldImpl.class);
		services[1] = ExportUtil.parse(WhoBoy.class);
		services[2] = ExportUtil.parse(WhoGirl.class);
		IProviderManager provMgr = ProviderManager.getProviderManager();
		provMgr.export(services);
	}

}
