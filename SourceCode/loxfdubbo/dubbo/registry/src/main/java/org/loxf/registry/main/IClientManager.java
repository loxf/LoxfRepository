/**
 * IClientManager.java
 * luohj - 下午8:46:58
 * 
 */
package org.loxf.registry.main;

import java.io.IOException;
import java.util.Properties;

import org.loxf.core.transcation.bean.Transaction;
import org.loxf.registry.annotation.Customer;
import org.loxf.registry.bean.AliveClient;
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
	 * @param referConfig
	 * @param tr
	 * @return
	 * @author:luohj
	 */
	<T> T refer(final Class<?> interfaces, final Customer referConfig, Transaction tr) throws Exception;
	/**
	 * 引用服务（单个服务）
	 * @param interfaceClass
	 * @param referConf
	 * @param tr
	 * @param newTr
	 * @return
	 * @throws Exception
	 * @author:luohj
	 */
	<T> T refer(final Class<T> interfaceClass, final Customer referConf, Transaction tr, boolean newTr)
			throws Exception;
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
	/**
	 * 根据key获取Service
	 * 
	 * @param key
	 * @return
	 * @author:luohj
	 */
	public Service getService(String key);
	/**
	 * 获取客户端负载均衡总配置
	 * @return
	 * @author:luohj
	 */
	public String getPollingType();
	/**
	 * 获取客户端信息
	 * @return
	 * @author:luohj
	 */
	public AliveClient getClient();
}
