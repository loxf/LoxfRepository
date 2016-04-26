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

import org.apache.commons.lang.StringUtils;
import org.loxf.registry.bean.AliveClient;
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
 * 消费端管理中心 
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
	 * 服务引用工具
	 */
	private Refer refer;

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
		// TODO：这儿建议改造，init的时候先启动服务接受通信线程，如果线程端口被占用，端口自动加1重试，直到成功启动，然后返回当前的Port，不应该直接设置
		client.setPort(30001);
		client.setType("CUST"); 
		client.setTimeout(60000);// 默认1分钟
		
		pollingType = "RANDOM";

		this.refer = new Refer(this);
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
			return refer.refer(interfaces, group, client, asyn);
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
			ClientListener listener = new ClientListener(this, client);
			client.setPort(listener.getPort());
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
	 * TODO:软负载获取服务端信息
	 * 
	 * @param list
	 * @return
	 * @author:luohj
	 */
	public Server loadBalancingServer(String key, java.lang.reflect.Method method) {
		Service service = this.getService(key);
		if(service==null){
			throw new RuntimeException("在消费端未找到当前service的定义！"+ service.toString());
		}
		// TODO 负载均衡算法，方法级>服务级>客户端总定义
		String polling = StringUtils.isEmpty(service.getPollingType()) ? this.pollingType : service.getPollingType();
		HashMap<String,Method> m = service.getMethod();
		if(m!=null){
			String[] param = new String[method.getParameterTypes().length];
			int i = 0;
			for(Class<?> c : method.getParameterTypes()){
				param[i++] = c.getName();
			}
			Method mth = m.get(new StringBuffer().append(method.getName()).append("(").append(StringUtils.join(param, " ,")).append(")")
					.toString());
			if(mth!=null&&!StringUtils.isEmpty(mth.getPollingType())){
				polling = mth.getPollingType();
			}
		}
		HashMap<String, Server> servers = service.getServers();
		List<Server> list = (List<Server>) servers.values();
		return loadBalancingServer(list, polling);
	}

}
