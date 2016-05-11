/**
 * ClientListen.java
 * luohj - 下午2:05:53
 * 
 */
package org.loxf.dubbo.registry;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.loxf.registry.main.ClientManager;
import org.loxf.registry.main.IClientManager;
import org.loxf.registry.utils.PropertiesUtil;

/**
 * @author luohj
 *
 */
public class CustomerListen implements ServletContextListener {
	IClientManager mgr;

	/**
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		String configPath = sce.getServletContext().getInitParameter("customerConfig");
		try {
			InputStream is = sce.getServletContext().getResourceAsStream(configPath);
			Properties p = PropertiesUtil.init(is);
			mgr = ClientManager.getClientManager();
			if(!mgr.isReady()){
				mgr.init(p);
				mgr.start();
			}
			System.out.println("CustomerListen init succ!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		mgr.stop();
	}
}
