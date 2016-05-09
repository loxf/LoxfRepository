/**
 * IClientManager.java
 * luohj - 下午8:46:58
 * 
 */
package org.loxf.registry.main;

import org.loxf.registry.queue.IssuedQueue;

/**
 * @author luohj
 */
public interface IClientManager {
	/**
	 * TODO:初始化
	 * @author:luohj
	 */
	void init();
	/**
	 * TODO:引用服务（单个服务）
	 * @param interfaces
	 * @param group
	 * @param asyn
	 * @return
	 * @author: luohj
	 */
	<T> T refer(final Class<T> interfaces, final String group, final boolean asyn);
	/**
	 * TODO:更新services
	 * @author:luohj
	 */
	void updateServices(IssuedQueue queue);
	/**
	 * TODO:启动客户端获取服务列表监听端口
	 * @author:luohj
	 */
	void start();
	/**
	 * TODO:客户端是否准备好
	 * @return
	 * @author:luohj
	 */
	public boolean isReady();
}
