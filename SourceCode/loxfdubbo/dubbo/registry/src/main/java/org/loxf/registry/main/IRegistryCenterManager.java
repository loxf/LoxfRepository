package org.loxf.registry.main;

import java.util.Map;
import java.util.Set;

import org.loxf.registry.bean.AliveClient;
import org.loxf.registry.bean.Client;
import org.loxf.registry.bean.Server;
import org.loxf.registry.bean.Service;

/**
 * @author luohj
 *
 */
public interface IRegistryCenterManager {
	
	/**
	 * 启动注册中心
	 * @author:luohj
	 */
	public void start();
	/**
	 * 关闭注册中心
	 * @author:luohj
	 */
	public void stop();
	/**
	 * 暂停服务器
	 * @param server
	 * @author:luohj
	 */
	public void pauseServer(Server server);  
	/**
	 * 停止服务器
	 * @param server
	 * @param must 强制停机
	 * @author:luohj
	 */
	public void stopServer(Server server, boolean must);  
    /**
     * 恢复服务器
     * @param server
     * @author:luohj
     */
    public void resumeServer(Server server);  
	/**
	 * 暂停服务
	 * @param service
	 * @author:luohj
	 */
	public void pauseService(Service service);  
    /**
     * 恢复服务
     * @param service
     * @author:luohj
     */
    public void resumeService(Service service);  
    /**
     * 注册服务
     * @param service
     * @author:luohj
     */
    public void register(Service service);  
    /**
     * 注册服务组
     * @param service
     * @author:luohj
     */
    public void register(Service[] service);  
    /**
     * 线程是否运行
     * @return
     * @author:luohj
     */
    public boolean isRunning();  
    /**
     * 获取端口
     * @return
     * @author:luohj
     */
    public int getPort(); 
	/**
	 * 服务端/消费端存活队列（有心跳）
	 * @param client 消费端或者服务端
	 * @author:luohj
	 */
	public void addAliveClient(AliveClient client);
	/**
	 * 获取所有客户端
	 * @return
	 * @author:luohj
	 */
	public Map<String, AliveClient> getAliveClients();
	/**
	 * 获取所有服务
	 * @return
	 * @author:luohj
	 */
	public Map<String, Service> getServices();
	/**
	 * 根据server获取服务
	 * @param server
	 * @return
	 * @author:luohj
	 */
	public Set<Service> getServiceByServer(Server server);
}
