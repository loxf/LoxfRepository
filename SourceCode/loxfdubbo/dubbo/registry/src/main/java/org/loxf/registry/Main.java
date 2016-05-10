/**
 * 启动注册中心(JAR形式启动)
 * Start.java
 * luohj - 下午8:22:46
 * 
 */
package org.loxf.registry;

import org.loxf.registry.main.RegistryCenterManager;

/**
 * @author luohj
 *
 */
public class Main {
	
	/**
	 * TODO:MAIN
	 * @author:luohj
	 */
	public void run() {
		RegistryCenterManager.getInstance().start();
	}
}
