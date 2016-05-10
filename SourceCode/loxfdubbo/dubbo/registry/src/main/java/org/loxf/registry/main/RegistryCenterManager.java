package org.loxf.registry.main;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.loxf.registry.bean.AliveClient;
import org.loxf.registry.bean.RegistryContainers;
import org.loxf.registry.bean.Server;
import org.loxf.registry.bean.Service;
import org.loxf.registry.listener.RpcListener;
import org.loxf.registry.queue.IssuedQueue;
import org.loxf.registry.thread.ClientLifeMgrThread;
import org.loxf.registry.thread.PushServicesThread;

/**
 * 注册中心管理中心 默认管理端口20880
 * 
 * @author luohj
 *
 */
public class RegistryCenterManager implements IRegistryCenterManager {
	private static IRegistryCenterManager mgr ;
	/**
	 * 监听（心跳/服务注册/全量服务获取）
	 */
	private RpcListener listener;
	/**
	 * 推送服务线程
	 */
	private PushServicesThread pushThread ;
	/**
	 * 客户端生命周期管理
	 */
	private ClientLifeMgrThread clientLifeThread;
	private boolean isRuning = true;
	private RegistryContainers center;
	private int port;
	/**
	 * 心跳存活队列
	 */
	private Map<String, AliveClient> aliveClients;
	
	public static IRegistryCenterManager getInstance(){
		if(mgr==null){
			synchronized(RegistryCenterManager.class){
				if(mgr==null){
					mgr = new RegistryCenterManager(20880);
				}
			}
		}
		return mgr;
	}	

	RegistryCenterManager(int port) {
		this.port = port;
		this.center = new RegistryContainers();
		this.aliveClients = new HashMap<String, AliveClient>();
	}


	/** 
	 * 停止注册中心
	 * @see org.loxf.registry.main.IRegistryCenterManager#stop()
	 */
	@Override
	public void stop() {
		listener.stop();
		pushThread.stop();
		clientLifeThread.stop();
	}
	
	/** 
	 * 启动注册中心
	 * @see org.loxf.registry.main.IRegistryCenterManager#start()
	 */
	public void start() {
		System.out.println("启动注册中心服务器");
		try {
			listener = new RpcListener(this);
			listener.start();
			this.isRuning = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("启动推送服务");
		try {
			pushThread = new PushServicesThread(getIssuedQueue(), aliveClients);
			pushThread.start();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		System.out.println("启动生产者/消费者心跳维护线程");
		try {
			clientLifeThread = new ClientLifeMgrThread(this, (HashMap<String, AliveClient>) aliveClients);
			clientLifeThread.start();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * @param isRuning
	 *            the isRuning to set
	 */
	public void setRuning(boolean isRuning) {
		this.isRuning = isRuning;
	}

	public boolean isRunning() {
		return isRuning;
	}

	public int getPort() {
		return port;
	}

	/**
	 * @see org.loxf.registry.main.IRegistryCenterManager#register(org.loxf.registry.bean.Service)
	 */
	@Override
	public void register(Service service) {
		center.registryService(service);
	}

	/**
	 * @see org.loxf.registry.main.IRegistryCenterManager#register(org.loxf.registry.bean.Service[])
	 */
	@Override
	public void register(Service[] service) {
		center.registryServices(service);
	}

	/**
	 * @see org.loxf.registry.main.IRegistryCenterManager#pauseServer(org.loxf.registry.bean.Server)
	 */
	@Override
	public void pauseServer(Server server) {
		 center.pauseServer(server);
	}

	/**
	 * @see org.loxf.registry.main.IRegistryCenterManager#resumeServer(org.loxf.registry.bean.Server)
	 */
	@Override
	public void resumeServer(Server server) {
		center.resumeServer(server);
	}

	/**
	 * @see org.loxf.registry.main.IRegistryCenterManager#pauseService(org.loxf.registry.bean.Service)
	 */
	@Override
	public void pauseService(Service service) {
		service.setStatus("EXP");
		service.setUpdate(true);
		service.setChanged(true);
		center.registryService(service);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.loxf.registry.main.IRegistryCenterManager#stopServer(org.loxf.registry.bean.Server)
	 */
	@Override
	public void stopServer(Server server, boolean must) {
		// 最后确认一次生产者是否真的断了，强制停机不需要校验
		boolean flag = true;
		if(!must){ 
			Socket socket = null;
			try {
				socket = new Socket(server.getServerAddr(), server.getServerPort());
				flag = false;
			} catch (NumberFormatException e0) {
				e0.printStackTrace();
			} catch (UnknownHostException | ConnectException e1) {
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (socket != null) {
					try {
						socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		if (must || (!must&&flag))
			center.stopServer(server);
	}

	/**
	 * @see org.loxf.registry.main.IRegistryCenterManager#resumeService(org.loxf.registry.bean.Service)
	 */
	@Override
	public void resumeService(Service service) {
		service.setStatus("EFF");
		service.setUpdate(true);
		service.setChanged(true);
		center.registryService(service);
	}

	/**
	 * 服务端/消费端存活队列（有心跳）
	 * 
	 * @param client
	 *            消费端或者服务端
	 * @author:luohj
	 */
	public void addAliveClient(AliveClient client) {
		if (aliveClients.containsKey(client.toString())) {
			aliveClients.get(client.toString()).setLastModifyDate(new Date());
		} else {
			client.setLastModifyDate(new Date());
			aliveClients.put(client.toString(), client);
		}
	}

	/**
	 * @return the aliveClients
	 */
	public Map<String, AliveClient> getAliveClients() {
		return aliveClients;
	}

	/**
	 * @param aliveClients
	 *            the aliveClients to set
	 */
	public void setAliveClients(Map<String, AliveClient> aliveClients) {
		this.aliveClients = aliveClients;
	}

	/**
	 * @return the issuedQueue
	 */
	public IssuedQueue getIssuedQueue() {
		return this.center.getIssuedQueue();
	}

	/**
	 * @return the issuedQueue
	 */
	public Map<String, Service> getServices() {
		return this.center.getServices();
	}
}
