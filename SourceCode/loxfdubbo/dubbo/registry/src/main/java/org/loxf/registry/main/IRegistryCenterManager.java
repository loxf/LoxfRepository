package org.loxf.registry.main;

import java.util.Map;

import org.loxf.registry.bean.AliveClient;
import org.loxf.registry.bean.Server;
import org.loxf.registry.bean.Service;

/**
 * @author luohj
 *
 */
public interface IRegistryCenterManager {
	
	/**
	 * TODO:启动心跳接受线程
	 * @author:luohj
	 */
	public void start();
	/**
	 * TODO:暂停服务器
	 * @param server
	 * @author:luohj
	 */
	public void pauseServer(Server server);  
	/**
	 * TODO:停止服务器
	 * @param server
	 * @param must 强制停机
	 * @author:luohj
	 */
	public void stopServer(Server server, boolean must);  
    /**
     * TODO:恢复服务器
     * @param server
     * @author:luohj
     */
    public void resumeServer(Server server);  
	/**
	 * TODO:暂停服务
	 * @param service
	 * @author:luohj
	 */
	public void pauseService(Service service);  
    /**
     * TODO:恢复服务
     * @param service
     * @author:luohj
     */
    public void resumeService(Service service);  
    /**
     * TODO:注册服务
     * @param service
     * @author:luohj
     */
    public void register(Service service);  
    /**
     * TODO:注册服务组
     * @param service
     * @author:luohj
     */
    public void register(Service[] service);  
    /**
     * TODO:线程是否运行
     * @return
     * @author:luohj
     */
    public boolean isRunning();  
    /**
     * TODO:获取端口
     * @return
     * @author:luohj
     */
    public int getPort(); 
	/**
	 * TODO:服务端/消费端存活队列（有心跳）
	 * @param client 消费端或者服务端
	 * @author:luohj
	 */
	public void addAliveClient(AliveClient client);
	/**
	 * TODO:获取所有服务
	 * @return
	 * @author:luohj
	 */
	public Map<String, Service> getServices();
}
