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
	 * 初始化
	 * @author:luohj
	 */
	void init();
	/**
	 * 引用服务（单个服务）
	 * @param interfaces
	 * @param group
	 * @param asyn
	 * @return
	 * @author: luohj
	 */
	<T> T refer(final Class<T> interfaces, final String group, final boolean asyn);
	/**
	 * 更新services
	 * @author:luohj
	 */
	void updateServices(IssuedQueue queue);
	/**
	 * 启动客户端获取服务列表监听端口
	 * @author:luohj
	 */
	void start();
	/**
	 * 客户端是否准备好
	 * @return
	 * @author:luohj
	 */
	public boolean isReady();
}
