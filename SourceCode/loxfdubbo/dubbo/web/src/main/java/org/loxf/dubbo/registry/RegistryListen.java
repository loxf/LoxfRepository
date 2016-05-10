/**
 * RegistryListen.java
 * luohj - 下午8:25:41
 * 
 */
package org.loxf.dubbo.registry;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.loxf.registry.main.RegistryCenterManager;

/**
 * 注册中心监听（WEB工程专用）
 * @author luohj
 *
 */
public class RegistryListen implements ServletContextListener {

	/** (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		RegistryCenterManager.getInstance().stop();
	}

	/** (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		RegistryCenterManager.getInstance().start();
	}
	
}
