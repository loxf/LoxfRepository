/**
 * ClientManager.java
 * luohj - 下午8:56:32
 * 
 */
package org.loxf.registry.main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.loxf.core.transcation.bean.Transaction;
import org.loxf.core.utils.CommonUtil;
import org.loxf.core.utils.ComputerInfoUtil;
import org.loxf.core.utils.PropertiesUtil;
import org.loxf.registry.annotation.Customer;
import org.loxf.registry.bean.AliveClient;
import org.loxf.registry.bean.RegistryCenter;
import org.loxf.registry.bean.Server;
import org.loxf.registry.bean.Service;
import org.loxf.registry.listener.ClientListener;
import org.loxf.registry.proxy.ReferProxy;
import org.loxf.registry.thread.ClientHeartBeatThread;
import org.loxf.registry.utils.MonitorUtil;

/**
 * 消费端管理中心 
 * @author luohj
 *
 */
public class ClientManager implements IClientManager {
	private static IClientManager mgr ;
	/**
	 * 注册中心
	 */
	private RegistryCenter registryCenter;
	/**
	 * 客户端监听
	 */
	private ClientListener listener;
	/**
	 * 客户端心跳线程
	 */
	private ClientHeartBeatThread thread;
	/**
	 * 客户端信息
	 */
	private AliveClient client;
	/**
	 * 客户端存放的服务列表
	 */
	private Map<String, Service> services = new HashMap<String, Service>();
	/**
	 * 心跳间隔
	 */
	private int heartBeatTime;
	/**
	 * 负载配置
	 */
	private String pollingType ;
	/**
	 * 是否准备好，当第一次获取全部服务后isReady=true
	 */
	private boolean isReady = false;

	/**
	 * @return the isReady
	 */
	public boolean isReady() {
		return isReady;
	}
	ClientManager(){}
	
	public static IClientManager getClientManager() {
		if (mgr == null) {
			synchronized (ProviderManager.class) {
				if (mgr == null) {
					mgr = new ClientManager();
				}
			}
		}
		return mgr;
	}

	@Override
	public void init(String configPath) throws IOException {
		if(StringUtils.isEmpty(configPath)){
			throw new RuntimeException("消费者监听启动失败，未配置customerConfig。");
		}
		Properties pro = PropertiesUtil.init(configPath);
		init(pro);
	}
	
	/**
	 * 初始化方法
	 * 
	 * @author:luohj
	 */
	public void init(Properties properties) {
		System.out.println("ClientManager initing...");
		this.registryCenter = new RegistryCenter();
		registryCenter.setIp(properties.getProperty("registry.ip"));
		registryCenter.setPort(CommonUtil.valueofInt(properties.getProperty("registry.port"), 20880));			
		heartBeatTime = CommonUtil.valueofInt(properties.getProperty("client.heartTime"), 10000);
		client = new AliveClient();
		client.setAppName(properties.getProperty("client.appName"));
		try {
			client.setIp(ComputerInfoUtil.getIp());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		client.setPort(CommonUtil.valueofInt(properties.getProperty("client.port"), 20880));
		client.setType("CUST"); 
		client.setTimeout(CommonUtil.valueofInt(properties.getProperty("client.timeout"), 60000));// 默认1分钟
		
		pollingType = CommonUtil.valueofString(properties.getProperty("client.pollingType"), "RANDOM");
		System.out.println("ClientManager init succ");
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.loxf.registry.main.IClientManager#refer(java.lang.Class,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T refer(Class<?> interfaces, Customer referConf, Transaction tr) 
			throws Exception {
		return (T) refer(interfaces, referConf, tr, true);
	}
	
	/**
	 * 引用服务
	 * 
	 * @param <T>
	 *            接口泛型
	 * @param interfaceClass
	 *            接口类型
	 * @param referConf
	 *            消费端配置
	 * @param tr
	 *            事务
	 * @param newTr
	 *            是否新事务
	 *            
	 * @return 远程服务
	 * @throws Exception
	 */
	@Override
	public <T> T refer(final Class<T> interfaceClass, final Customer referConf, Transaction tr, boolean newTr)
			throws Exception {
		if (interfaceClass == null)
			throw new IllegalArgumentException("Interface class == null");
		if (!interfaceClass.isInterface())
			throw new IllegalArgumentException("The " + interfaceClass.getName() + " must be interface class!");
		System.out.println("Refer service " + interfaceClass.getName() + " success. ");
		return new ReferProxy().bind(interfaceClass, new MonitorUtil(interfaceClass, true), mgr, referConf, tr, newTr);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.loxf.registry.main.IClientManager#updateServices()
	 */
	@Override
	public void updateServices(Service[] servs) {
		updateServiceThread(servs);
	}
	
	@Override
	public void stop(){
		listener.stop();
		thread.stop();
	}

	/**
	 * TODO 消费者启动start
	 * 
	 * @see org.loxf.registry.main.IClientManager#start()
	 */
	@Override
	public void start() {
		System.out.println("ClientManager starting...");
		Date start = new Date();
		// 第一次启动，主动向服务端获取一次全量服务列表
		getAllServicesFirstConnect();
		// 再启动客户端获取服务的监听
		try {
			listener = new ClientListener(this, client);
			client.setPort(listener.getPort());
			listener.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 最后启动心跳线程，在注册中心激活客户端，之后注册中心会主动推送有改变的服务
		try {
			thread = new ClientHeartBeatThread(heartBeatTime, registryCenter, client);
			thread.start();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		while(!mgr.isReady()){
			Date end = new Date();
			if(end.getTime() - start.getTime()>60000l){
				throw new RuntimeException("启动消费端监听超时：60S... 请检查注册中心是否正常。");
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("ClientManager start succ");
	}

	/**
	 * 第一次启动，主动向服务端获取一次全量服务列表<br>
	 * 协议：<br>
	 * in {int:2}<br>
	 * out {Map<String, Service> services}<br>
	 * 
	 * @author:luohj
	 * @throws IOException 
	 */
	private void getAllServicesFirstConnect() {
		new Thread(new Runnable(){
			public void run(){
				while(true){
					try {
						Socket socket = null;
						try {
							socket = new Socket(registryCenter.getIp(), registryCenter.getPort());
							ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
							// 向注册中心请求全量服务列表
							out.writeInt(2);
							out.writeObject("QUERY");
							ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
							try {
								@SuppressWarnings("unchecked")
								Map<String, Service> result = (Map<String, Service>) in.readObject();
								services = result;
								System.out.println(result);
								isReady = true;
								break;// 获取成功才能结束
							} catch (ClassNotFoundException e) {
								e.printStackTrace();
							} finally {
								in.close();
								out.close();
							}
						} catch (NumberFormatException e) {
							e.printStackTrace();
						} catch (UnknownHostException |ConnectException e) {
							e.getMessage();
						} finally {
							if (socket != null)
								socket.close();
							Thread.sleep(heartBeatTime);
						}
					} catch (IOException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	/**
	 * 更新服务
	 * 
	 * @param services
	 * @author:luohj
	 */
	public void updateServiceThread(final Service[] servs) {
		new Thread(new Runnable() {
			public void run() {
				if(servs!=null && servs.length>0){
					synchronized (services) {
						for (Service service : servs) {
							if (service != null && service.isUpdate()) {
								service.setLastModifyDate(new Date());
								services.put(service.toString(), service);
								System.out.println("客户端服务["+service.toString() + "]更新成功!");
							}
						}
					}
				}
			}
		}).start();
	}

	/**
	 * @return the registryCenter
	 */
	public RegistryCenter getRegistryCenter() {
		return registryCenter;
	}

	/**
	 * @param registryCenter
	 *            the registryCenter to set
	 */
	public void setRegistryCenter(RegistryCenter registryCenter) {
		this.registryCenter = registryCenter;
	}

	/**
	 * @return the client
	 */
	public AliveClient getClient() {
		return client;
	}

	/**
	 * @param client
	 *            the client to set
	 */
	public void setClient(AliveClient client) {
		this.client = client;
	}

	/**
	 * @return the services
	 */
	public Map<String, Service> getServices() {
		return services;
	}

	/**
	 * @param services
	 *            the services to set
	 */
	public void setServices(Map<String, Service> services) {
		this.services = services;
	}

	/**
	 * 根据key获取Service
	 * 
	 * @param key
	 * @return
	 * @author:luohj
	 */
	public Service getService(String key) {
		if (this.getServices() == null) {
			return null;
		}
		return this.getServices().get(key);
	}

	/**
	 * 获取服务端信息
	 * 
	 * @param key
	 * @return
	 * @author:luohj
	 */
	public List<Server> getServerList(String key) {
		Service service = this.getService(key);
		HashMap<String, Server> services = service.getServers();
		return (List<Server>) services.values();
	}
	
	/**
	 * @return the pollingType
	 */
	public String getPollingType() {
		return pollingType;
	}
	/**
	 * @param pollingType the pollingType to set
	 */
	public void setPollingType(String pollingType) {
		this.pollingType = pollingType;
	}
	public static void main(String args[]){
		IClientManager mgr = ClientManager.getClientManager();
		mgr.start();
	}

}
