/**
 * ProviderMananger.java
 * luohj - 下午5:06:38
 * 
 */
package org.loxf.registry.main;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.loxf.registry.bean.AliveClient;
import org.loxf.registry.bean.Client;
import org.loxf.registry.bean.Invocation;
import org.loxf.registry.bean.Method;
import org.loxf.registry.bean.RegistryCenter;
import org.loxf.registry.bean.Server;
import org.loxf.registry.bean.Service;
import org.loxf.registry.listener.ProviderListener;
import org.loxf.registry.queue.UploadServiceQueue;
import org.loxf.registry.thread.ServerHeartBeatThread;
import org.loxf.registry.thread.UploadServiceThread;
import org.loxf.registry.utils.ComputerInfoUtil;

/**
 * @author luohj
 *
 */
public class ProviderManager implements IProviderManager {
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
	 * 监听是否运行
	 */
	private boolean isRuning = true;
	/**
	 * 上发服务队列
	 */
	private UploadServiceQueue queue;
	/**
	 * 配置文件路径
	 */
	private String xmlPath;
	
	public ProviderManager(){
		init();
	}

	/**
	 * TODO:初始化
	 * 
	 * @author:luohj
	 */
	void init() {
		parseXml(xmlPath);
		
		this.registryCenter = new RegistryCenter();
		registryCenter.setIp("127.0.0.1");
		registryCenter.setPort(20880);

		heartBeatTime = 30000;

		client = new AliveClient();
		client.setAppName("SERVER1");
		try {
			client.setIp(ComputerInfoUtil.getIp());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		client.setPort(30201);
		client.setType("SERV");

		isRuning = false;
	}

	/**
	 * TODO:解析XML配置文件
	 * 
	 * @param path
	 * @author:luohj
	 */
	void parseXml(String path) {
		
	}

	/**
	 * (non-Javadoc)
	 * @param invo
	 * 
	 * @throws ClassNotFoundException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @see org.loxf.registry.main.IProviderManager#call(org.loxf.registry.bean.Invocation)
	 */
	@Override
	public Object call(Invocation invo)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException {
		String key = invo.getInterfaces()
				+ (StringUtils.isBlank(invo.getGroup()) ? "" : ":" + invo.getGroup()).toString();
		if (services.containsKey(key)) {
			Service service = services.get(key);
			synchronized (services) {
				service.isUpdate();

				HashMap<String, Client> clients = service.getClients();
				Client c = new Client(invo.getIp(), invo.getAppName(), invo.isAsyn());
				if (!clients.containsKey(c.toString())) {
					// 更新客户端信息
					c.setUpdate(true);
					c.setChanged(true);
					clients.put(c.toString(), c);
				}
				HashMap<String, Method> methods = service.getMethod();

				Class<?>[] paramTypes = invo.getMethod().getParameterTypes();
				String[] params = new String[paramTypes.length];
				int i = 0;
				for (Class<?> t : paramTypes) {
					params[i++] = t.getName();
				}
				String methodKey = new StringBuffer().append(invo.getMethod().getName()).append("(")
						.append(StringUtils.join(params, " ,")).append(")").toString();
				if (!methods.containsKey(methodKey)) {
					Method m = new Method(invo.getMethod().getName(), params);
					m.isUpdate();
					m.isChanged();
					// 更新方法信息
					methods.put(methodKey, m);
				}
			}
			synchronized (queue) {
				queue.add(service);
			}
			// 反射调用方法
			Object result = invo.getMethod().invoke(Class.forName(service.getImplClazz()), invo.getParams());
			return result;
		} else {
			throw new RuntimeException("服务[" + key + "]不存在！");
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.loxf.registry.main.IProviderManager#export(org.loxf.registry.bean.Service)
	 */
	@Override
	public void export(Service service) {
		if (service != null && service.isUpdate()) {
			synchronized (this.services) {
				String result = service.toString();
				Date now = new Date();
				if (this.services.containsKey(service.toString())) {
					Service srv = this.services.get(service.toString());
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
					this.services.put(service.toString(), service);
				}
				// 更新了服务端信息时需要推送

				synchronized (queue) {
					queue.add(this.services.get(service.toString()));
				}
				System.out.println(result + ":暴露成功!");
			}
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.loxf.registry.main.IProviderManager#export(org.loxf.registry.bean.Service[])
	 */
	@Override
	public void export(Service[] services) {
		for (Service srv : services) {
			export(srv);
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.loxf.registry.main.IProviderManager#isRunning()
	 */
	@Override
	public boolean isRunning() {
		return isRuning;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.loxf.registry.main.IProviderManager#start()
	 */
	@Override
	public boolean start() {
		// 启动服务接受监听
		try {
			ProviderListener listener = new ProviderListener(this, client.getPort());
			listener.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 启动服务上发线程（与注册中心交互）
		UploadServiceThread upload = new UploadServiceThread(heartBeatTime, queue, registryCenter);
		upload.start();

		// 启动心跳线程
		ServerHeartBeatThread serverHeartBeat = new ServerHeartBeatThread(heartBeatTime, registryCenter, client);
		serverHeartBeat.start();
		return false;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.loxf.registry.main.IProviderManager#getPort()
	 */
	@Override
	public int getPort() {
		return client.getPort();
	}

}
