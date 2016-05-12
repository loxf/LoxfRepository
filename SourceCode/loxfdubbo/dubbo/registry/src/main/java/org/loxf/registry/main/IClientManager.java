/**
 * IClientManager.java
 * luohj - 下午8:46:58
 * 
 */
package org.loxf.registry.main;

import java.io.IOException;
import java.util.Properties;

import org.loxf.registry.bean.Service;

/**
 * @author luohj
 */
public interface IClientManager {
	/**
	 * 初始化
	 * @author:luohj
	 */
	void init(String configPath) throws IOException;
	/**
	 * 初始化
	 * @author:luohj
	 */
	void init(Properties properties);
	/**
	 * 引用服务（单个服务）
	 * @param interfaces
	 * @param group
	 * @param asyn
	 * @return
	 * @author: luohj
	 */
	<T> T refer(final Class<T> interfaces, final String group, final boolean asyn, final boolean jvm);
	/**
	 * 更新services
	 * @author:luohj
	 */
	void updateServices(Service[] servs);
	/**
	 * 启动客户端获取服务列表监听端口
	 * @author:luohj
	 */
	void start();
	/**
	 * 停止客户端
	 * @author:luohj
	 */
	void stop();
	/**
	 * 客户端是否准备好
	 * @return
	 * @author:luohj
	 */
	public boolean isReady();
}
