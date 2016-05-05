/**
 * ProviderMananger.java
 * luohj - 下午5:06:38
 * 
 */
package org.loxf.registry.main;

import java.io.File;
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
import org.loxf.registry.bean.Method;
import org.loxf.registry.bean.RegistryCenter;
import org.loxf.registry.bean.Server;
import org.loxf.registry.bean.Service;
import org.loxf.registry.invocation.Invocation;
import org.loxf.registry.listener.ProviderListener;
import org.loxf.registry.queue.UploadServiceQueue;
import org.loxf.registry.thread.ServerHeartBeatThread;
import org.loxf.registry.thread.UploadServiceThread;
import org.loxf.registry.utils.ComputerInfoUtil;
import org.loxf.registry.utils.MapCastList;

/**
 * @author luohj
 *
 */
public class ProviderManager implements IProviderManager {
	static IProviderManager mgr;
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
	 * 上传服务间隔时间
	 */
	private int uploadTime;
	/**
	 * 监听是否运行
	 */
	private boolean isRuning = true;
	/**
	 * 上发服务队列
	 */
	private UploadServiceQueue queue = new UploadServiceQueue();
	/**
	 * 配置文件路径
	 */
	private String xmlPath;

	ProviderManager() {
		init();
		start();
		mgr = this;
	}

	public static IProviderManager getProviderManager() {
		if (mgr == null) {
			synchronized (ProviderManager.class) {
				if (mgr == null) {
					mgr = new ProviderManager();
				}
			}
		}
		return mgr;
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

		heartBeatTime = 20000;
		uploadTime = 30000;

		client = new AliveClient();
		client.setAppName("SERVER1");
		try {
			client.setIp(ComputerInfoUtil.getIp());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		client.setPort(30200);// 建议不要手工设置，从30200开始自动设置，端口被占，自动加1重设。
		client.setType("SERV");
		client.setTimeout(60000);

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
	 * 调用方法
	 * 
	 * @param invo
	 * 
	 * @throws ClassNotFoundException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @see org.loxf.registry.main.IProviderManager#call(org.loxf.registry.invocation.Invocation)
	 */
	@Override
	public Object call(Invocation invo)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException {
		// 异步执行服务调用的客户端信息
		String key = invo.getInterfaces().getName()
				+ (StringUtils.isBlank(invo.getGroup()) ? "" : ":" + invo.getGroup()).toString();
		Service service = services.get(key);
		if (service == null) {
			throw new RuntimeException("服务[" + key + "]不存在！");
		}
		new Thread(new Runnable() {
			public void run() {
				synchronized (services) {
					Date now = new Date();
					HashMap<String, Client> clients = service.getClients();
					Client c = new Client(invo.getIp(), invo.getAppName(), invo.isAsyn());
					if(clients==null){
						clients = new HashMap<String, Client> ();
					}
					if (!clients.containsKey(c.toString())) {
						// 更新客户端信息
						c.setUpdate(true);
						c.setChanged(true);
						c.setLastModifyDate(now);
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
						c.setUpdate(true);
						c.setChanged(true);
						m.setLastModifyDate(now);
						// 更新方法信息
						methods.put(methodKey, m);
					}
				}
				synchronized (queue) {
					queue.add(service);
				}
			}
		}).start();
		// 反射调用方法
		java.lang.reflect.Method m = null;
		Object result = null;
		try {
			m = Class.forName(service.getInterfaces()).getMethod(invo.getMethod().getName(),
					invo.getMethod().getParameterTypes());
			result = m.invoke(Class.forName(service.getImplClazz()).newInstance(), invo.getParams());
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		return result;

	}

	/**
	 * TODO:暴露服务(直接暴露)
	 * 
	 * @param interfaces
	 * @param impl
	 * @param group
	 * @author:luohj
	 */
	public void export(Class<?> interfaces, Class<?> impl, String group) {
		Service service = new Service();
		service.setInterfaces(interfaces.getName());
		service.setImplClazz(impl.getName());
		service.setServiceName(group == null ? impl.getClass().getSimpleName() : group);
		service.setUpdate(true);
		service.setChanged(true);
		service.setLastModifyDate(new Date());
		this.export(service);
	}

	/**
	 * 暴露服务
	 * 
	 * @param file
	 * @return
	 * @author:luohj
	 */
	public Service export(File file) {
		return null;
	}

	/**
	 * 暴露服务（单个） (non-Javadoc)
	 * 
	 * @see org.loxf.registry.main.IProviderManager#export(org.loxf.registry.bean.Service)
	 */
	@Override
	public void export(Service service) {

		if (service != null && service.isUpdate()) {
			Server server = new Server();
			server.setServerAddr(client.getIp());
			server.setServerPort(client.getPort());
			server.setServerName(client.getAppName());
			server.setStatus("EFF");
			server.setTimeout(client.getTimeout());
			HashMap<String, Server> map = new HashMap<String, Server>();
			map.put(server.toString(), server);
			service.setServers(map);
			synchronized (this.services) {
				if (!this.services.containsKey(service.toString())) {
					this.services.put(service.toString(), service);
				}
			}
			synchronized (queue) {
				queue.add(this.services.get(service.toString()));
			}
			// 更新了服务端信息时需要推送
			System.out.println(service.toString() + ":暴露成功!");
		}
	}

	/**
	 * 暴露服务（多个） (non-Javadoc)
	 * 
	 * @see org.loxf.registry.main.IProviderManager#export(org.loxf.registry.bean.Service[])
	 */
	@Override
	public void export(Service[] services) {
		for (Service srv : services) {
			this.export(srv);
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
			client.setPort(listener.getPort());
			listener.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 上发服务线程
		uploadService();

		// 启动心跳线程
		ServerHeartBeatThread serverHeartBeat = new ServerHeartBeatThread(heartBeatTime, registryCenter, client);
		serverHeartBeat.start();

		// 保持一个重发线程
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					if (registryCenter.getStatus().equals("EXP")) {
						reUploadService();
						// 重新注册成功后，改注册中心状态为保持。
						registryCenter.setStatus("HOLD");
					}
					try {
						Thread.sleep(uploadTime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

		}).start();
		return true;
	}

	/**
	 * @see org.loxf.registry.main.IProviderManager#reUploadService()
	 */
	@SuppressWarnings("unchecked")
	public void reUploadService() {
		List<Service> srvs = (List<Service>) MapCastList.convert(this.services);
		if (srvs != null) {
			synchronized (queue) {
				for (Service srv : srvs) {
					queue.add(srv);
				}
			}
		}
	}

	/**
	 * TODO:上发服务
	 * 
	 * @author:luohj
	 */
	void uploadService() {
		// 启动服务上发线程（与注册中心交互）
		UploadServiceThread upload = new UploadServiceThread(uploadTime, queue, registryCenter);
		upload.start();
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

	public static void main(String args[]) {
		IProviderManager mgr = ProviderManager.getProviderManager();
		mgr.start();
	}
}
