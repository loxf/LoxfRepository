/**
 * ClientManager.java
 * luohj - 下午8:56:32
 * 
 */
package org.loxf.registry.main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.loxf.registry.bean.AliveClient;
import org.loxf.registry.bean.Method;
import org.loxf.registry.bean.RegistryCenter;
import org.loxf.registry.bean.Server;
import org.loxf.registry.bean.Service;
import org.loxf.registry.constracts.PollingType;
import org.loxf.registry.invocation.Invocation;
import org.loxf.registry.listener.ClientListener;
import org.loxf.registry.queue.IssuedQueue;
import org.loxf.registry.thread.ClientHeartBeatThread;
import org.loxf.registry.utils.CommonUtil;
import org.loxf.registry.utils.ComputerInfoUtil;
import org.loxf.registry.utils.LoadBalanceUtil;
import org.loxf.registry.utils.MapCastList;
import org.loxf.registry.utils.PropertiesUtil;

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
	@Override
	public <T> T refer(Class<T> interfaces, String group, boolean asyn) {
		try {
			return refer(interfaces, group, client, asyn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 引用服务
	 * 
	 * @param <T>
	 *            接口泛型
	 * @param interfaceClass
	 *            接口类型
	 * @param group
	 *            分组，用于区分一个接口多个实现
	 * @param server
	 *            服务端信息
	 * @param AliveClient
	 *            客户端信息
	 * @param asyn
	 *            是否同步
	 * @return 远程服务
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	<T> T refer(final Class<T> interfaceClass, final String group, final AliveClient client, final boolean asyn)
			throws Exception {
		if (interfaceClass == null)
			throw new IllegalArgumentException("Interface class == null");
		if (!interfaceClass.isInterface())
			throw new IllegalArgumentException("The " + interfaceClass.getName() + " must be interface class!");
		System.out.println("Refer service " + interfaceClass.getName() + " success. ");
		return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[] { interfaceClass },
				new InvocationHandler() {
					public Object invoke(Object proxy, java.lang.reflect.Method method, Object[] arguments) throws Throwable {
						String key = interfaceClass.getName() + (StringUtils.isEmpty(group) ? "" : ":" + group);
						Service service = getService(key);
						if(service==null){
							throw new RuntimeException("未找到当前service的定义！"+ key);
						}
						// 负载均衡算法，方法级>服务级>客户端总定义
						String polling = StringUtils.isEmpty(service.getPollingType()) ? pollingType : service.getPollingType();
						HashMap<String,Method> ms = service.getMethod();
						if(ms!=null){
							String[] param = new String[method.getParameterTypes().length];
							int i = 0;
							for(Class<?> c : method.getParameterTypes()){
								param[i++] = c.getName();
							}
							Method mth = ms.get(new StringBuffer().append(method.getName()).append("(").append(StringUtils.join(param, " ,")).append(")")
									.toString());
							if(mth!=null&&!StringUtils.isEmpty(mth.getPollingType())){
								polling = mth.getPollingType();
							}
						}
						// 获取所有服务端
						List<Server> servers = getServerListByServiceKey(key, service);
						while(!servers.isEmpty()){
							Server server = loadBalancingServer(polling, servers);
							if (server.getServerAddr() == null || server.getServerAddr().length() == 0)
								throw new IllegalArgumentException("Host == null!");
							if (server.getServerPort() <= 0 || server.getServerPort() > 65535)
								throw new IllegalArgumentException("Invalid port " + server.getServerPort());
							System.out.println("Get remote service " + interfaceClass.getName() + " from server "
									+ server.getServerAddr() + ":" + server.getServerPort());
							Socket socket = null;
							try {
								socket = new Socket(server.getServerAddr(), server.getServerPort());
								ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
								try {
									/**
									 * 协议 in{Invocation invo} out{Object result}
									 */
									Invocation invo = new Invocation();
									invo.setInterfaces(interfaceClass);
									invo.setGroup(group);
									org.loxf.registry.invocation.Method m = new org.loxf.registry.invocation.Method();
									m.setName(method.getName());
									m.setParameterTypes(method.getParameterTypes());
									invo.setMethod(m);
									invo.setParams(arguments);
									invo.setAppName(client.getAppName());
									invo.setAsyn(asyn);
									invo.setIp(client.getIp());
									output.writeObject(invo);
									ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
									try {
										Object result = input.readObject();
										if (result instanceof Throwable) {
											throw (Throwable) result;
										}
										return result;
									} finally{
										input.close();
									}
								} finally {
									output.close();
								}
							} catch (UnknownHostException e) {
								servers.remove(server);
								e.printStackTrace();
							} catch (ConnectException e) {
								servers.remove(server);
								e.printStackTrace();
							} finally {
								if(socket!=null)
									socket.close();
							}
						}
						return null;
					}
				});
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.loxf.registry.main.IClientManager#updateServices()
	 */
	@Override
	public void updateServices(IssuedQueue queue) {
		updateServiceThread(queue);
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
	public void updateServiceThread(IssuedQueue queue) {
		new Thread(new Runnable() {
			public void run() {
				while (!queue.isEmpty()) {
					Service service = queue.poll();
					if (service != null && service.isUpdate()) {
						synchronized (services) {
							service.setLastModifyDate(new Date());
							services.put(service.toString(), service);
							System.out.println("客户端服务["+service.toString() + "]更新成功!");
						}
					}
				}
				queue.clear();
			}
		}).start();
		;
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
	 * 软负载获取服务端信息
	 * 
	 * @param list
	 * @return
	 * @author:luohj
	 */
	public Server loadBalancingServer(List<Server> list, String pollingType) {
		if (pollingType.equals(PollingType.RANDOM)) {
			return (Server) LoadBalanceUtil.getTByRandom(list, pollingType);
		} else if (pollingType.equals(PollingType.DYNC_POLLING)) {
			return (Server) LoadBalanceUtil.getTByRandom(list, pollingType);
		} else if (pollingType.equals(PollingType.MIN_CONNECTION)) {
			return (Server) LoadBalanceUtil.getTByRandom(list, pollingType);
		} else if (pollingType.equals(PollingType.OBSERVATION)) {
			return (Server) LoadBalanceUtil.getTByRandom(list, pollingType);
		} else if (pollingType.equals(PollingType.POLLING)) {
			return (Server) LoadBalanceUtil.getTByRandom(list, pollingType);
		} else if (pollingType.equals(PollingType.PREDICTION)) {
			return (Server) LoadBalanceUtil.getTByRandom(list, pollingType);
		} else if (pollingType.equals(PollingType.WRR)) {
			return (Server) LoadBalanceUtil.getTByRandom(list, pollingType);
		}
		return null;
	}
	/**
	 * 软负载获取服务端信息
	 * 
	 * @param list
	 * @return
	 * @author:luohj
	 */
	public Server loadBalancingServer(String polling, List<Server> servers) {
		return loadBalancingServer(servers, polling);
	}
	
	@SuppressWarnings("unchecked")
	public List<Server> getServerListByServiceKey(String key, Service service){
		HashMap<String, Server> servers = service.getServers();
		return (List<Server>) MapCastList.convert(servers);
	}
	
	public static void main(String args[]){
		IClientManager mgr = ClientManager.getClientManager();
		mgr.start();
	}

}
