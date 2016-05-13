/**
 * ExportTest.java
 * {luohj} - 下午6:51:27
 * 
 */
package org.loxf.service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.loxf.registry.main.ClientManager;
import org.loxf.registry.main.IClientManager;
import org.loxf.registry.main.IProviderManager;
import org.loxf.registry.main.ProviderManager;

/**
 * @author {luohj}
 *
 */
public class ExportTest {
	/**
	 * TODO 启动服务端MAIN方法(非WEB启动时使用)
	 * @param args
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IOException 
	 * @author:luohj
	 */
	public static void main(String[] args) throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException {
		// 暴露服务 启用生产者
		IProviderManager serverMgr = ProviderManager.getProviderManager();
		serverMgr.init("org/loxf/service/product.properties");
		serverMgr.start();
		
		// 获取服务 启用消费者
		IClientManager clientMgr = ClientManager.getClientManager();
		clientMgr.init("org/loxf/service/customer.properties");
		clientMgr.start();
	}

}
