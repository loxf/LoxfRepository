/**
 * ClientManager.java
 * luohj - 下午8:56:32
 * 
 */
package org.loxf.registry.main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.loxf.registry.bean.AliveClient;
import org.loxf.registry.bean.Client;
import org.loxf.registry.bean.Method;
import org.loxf.registry.bean.RegistryCenter;
import org.loxf.registry.bean.Server;
import org.loxf.registry.bean.Service;
import org.loxf.registry.constracts.PollingType;
import org.loxf.registry.exportAndRefer.Refer;
import org.loxf.registry.listener.ClientListener;
import org.loxf.registry.queue.IssuedQueue;
import org.loxf.registry.thread.ClientHeartBeatThread;
import org.loxf.registry.utils.ComputerInfoUtil;
import org.loxf.registry.utils.LoadBalanceUtil;

/**
 * @author luohj
 *
 */
public class ClientManager implements IClientManager {
	/**
	 * 注册中心
	 */
	private RegistryCenter registryCenter;
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
	 * 客户端服务接受监听端口
	 */
	private int port ;

	/**
	 * TODO:初始化方法
	 * 
	 * @author:luohj
	 */
	public void init() {
		this.registryCenter = new RegistryCenter();
		registryCenter.setIp("127.0.0.1");
		registryCenter.setPort(20880);

		heartBeatTime = 30000;

		client = new AliveClient();
		client.setAppName("CLIENT1");
		try {
			client.setIp(ComputerInfoUtil.getIp());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		client.setPort(30001);
		client.setType("CUST");
		
		pollingType = "RANDOM";
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.loxf.registry.main.IClientManager#refer(java.lang.Class,
	 *      java.lang.String)
	 */
	@Override
	public <T> T refer(Class<T> interfaces, String group, boolean asyn) {
		// 负载均衡获取服务端
		Server server = loadBalancingServer(getServerList(interfaces.getName() + (group == null ? "" : ":" + group)),
				pollingType);
		try {
			return Refer.refer(interfaces, group, server, client, asyn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.loxf.registry.main.IClientManager#start()
	 */
	@Override
	public void start() {
		// 第一次启动，主动向服务端获取一次全量服务列表
		getAllServicesFirstConnect();
		// 再启动客户端获取服务的监听
		try {
			ClientListener listener = new ClientListener(this, port);
			listener.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 最后启动心跳线程，在注册中心激活客户端，之后注册中心会主动推送有改变的服务
		try {
			ClientHeartBeatThread thread = new ClientHeartBeatThread(heartBeatTime, registryCenter, client);
			thread.start();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * TODO:第一次启动，主动向服务端获取一次全量服务列表<br>
	 * 协议：<br>
	 * in {int:2}<br>
	 * out {Map<String, Service> services}<br>
	 * 
	 * @author:luohj
	 */
	private void getAllServicesFirstConnect() {
		Socket socket = null;
		try {
			socket = new Socket(registryCenter.getIp(), registryCenter.getPort());
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			// 向注册中心请求全量服务列表
			out.writeInt(2);
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			try {
				@SuppressWarnings("unchecked")
				Map<String, Service> result = (Map<String, Service>) in.readObject();
				this.services = result;
				System.out.println(result);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} finally {
				in.close();
				out.close();
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (socket != null)
					socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * TODO:更新服务
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
							String result = service.toString();
							Date now = new Date();
							if (services.containsKey(service.toString())) {
								Service srv = services.get(service.toString());
								if (srv != null) {
									// 先注册方法
									HashMap<String, Method> methods = srv.getMethod();
									if (methods != null) {
										List<Method> tmpMethods = (List<Method>) service.getMethod().values();
										for (Method tmpMethod : tmpMethods) {
											if (tmpMethod.isUpdate() && tmpMethod.isChanged()) {
												tmpMethod.setLastModifyDate(now);
												methods.put(tmpMethod.toString(), tmpMethod);
											}
										}
									} else {
										if (service.getMethod() != null) {
											srv.setMethod(service.getMethod());
										}
									}
									// 再注册客户端
									HashMap<String, Client> clients = srv.getClients();
									if (clients != null) {
										List<Client> tmpClients = (List<Client>) service.getClients().values();
										for (Client tmpClient : tmpClients) {
											if (tmpClient.isUpdate() && tmpClient.isChanged()) {
												tmpClient.setLastModifyDate(now);
												clients.put(tmpClient.toString(), tmpClient);
											}
										}

									} else {
										if (service.getClients() != null) {
											srv.setClients(service.getClients());
										}
									}
									// 再注册服务端
									HashMap<String, Server> servers = srv.getServers();
									if (servers != null) {
										List<Server> tmpServers = (List<Server>) service.getServers().values();
										for (Server tmpSv : tmpServers) {
											if (tmpSv.isUpdate() && tmpSv.isChanged()) {
												tmpSv.setLastModifyDate(now);
												servers.put(tmpSv.toString(), tmpSv);
												result += "[" + tmpSv.toString() + "]";
											}
										}
									} else {
										if (service.getServers() != null) {
											srv.setServers(service.getServers());
										}
									}
									// 最后注册服务本身
									if (service.isUpdate() && service.isChanged()) {
										srv.setInterfaces(service.getInterfaces());
										srv.setImplClazz(service.getImplClazz());
										srv.setPollingType(service.getPollingType());
										srv.setServiceName(service.getServiceName());
										srv.setStatus(service.getStatus());
										srv.setTimeout(service.getTimeout());
										srv.setAsyn(service.isAsyn());
									}
									srv.setUpdate(service.isUpdate());
									srv.setChanged(service.isChanged());
									srv.setLastModifyDate(now);
								}
							} else {
								services.put(service.toString(), service);
							}
							System.out.println(result + ":更新成功!");

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
	 * TODO:根据key获取Service
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
	 * TODO:获取服务端信息
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
	 * TODO:软负载获取服务端信息
	 * 
	 * @param list
	 * @param pollingType
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

}
