package org.loxf.registry.main;

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
}
