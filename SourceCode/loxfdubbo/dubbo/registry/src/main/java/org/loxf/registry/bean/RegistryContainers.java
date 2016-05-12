/**
 * ServerContainers.java
 * luohj - 上午9:35:35
 * 
 */
package org.loxf.registry.bean;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.loxf.core.utils.MapCastList;
import org.loxf.registry.queue.IssuedQueue;

/**
 * @author luohj
 *
 */
public class RegistryContainers extends BaseBean {
	private static final long serialVersionUID = 1L;
	/**
	 * 服务容器<br>
	 * key=service.toString(),value=service;
	 */
	private HashMap<String, Service> services;
	/**
	 * 服务器与服务映射关系<br>
	 * key=service.toString(),value=service;
	 */
	private HashMap<String, Set<Service>> serverMapServices;

	/**
	 * 推送服务队列
	 */
	private IssuedQueue issuedQueue;

	public RegistryContainers() {
		services = new HashMap<String, Service>();
		issuedQueue = new IssuedQueue();
		serverMapServices = new HashMap<String, Set<Service>>();
	}

	/**
	 * 注册服务组
	 * 
	 * @param services
	 * @author:luohj
	 */
	public void registryServices(Service[] services) {
		if (services != null && services.length > 0) {
			for (Service tmp : services) {
				registryService(tmp);
			}
		}
	}

	/**
	 * 注册服务
	 * 
	 * @param services
	 * @author:luohj
	 */
	public void registryService(Service service) {
		// 是否推送，当服务只是更改了方法与客户端信息时，不需要推送。
		boolean isPush = false;
		String serverStr = "";
		@SuppressWarnings("unchecked")
		List<Server> tmpServers = (List<Server>) MapCastList.convert(service.getServers());
		Server tmp = tmpServers.get(0);
		if (tmp.isUpdate() && tmp.isChanged()) {
			serverStr = tmp.toString();
		}
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
							@SuppressWarnings("unchecked")
							List<Method> tmpMethods = (List<Method>) MapCastList.convert(service.getMethod());
							if(tmpMethods!=null){
								for (Method tmpMethod : tmpMethods) {
									if (tmpMethod.isUpdate() && tmpMethod.isChanged()) {
										tmpMethod.setLastModifyDate(now);
										methods.put(tmpMethod.toString(), tmpMethod);
									}
								}
							}
						} else {
							if (service.getMethod() != null) {
								srv.setMethod(service.getMethod());
							} else {
								srv.setMethod(new HashMap<String, Method> ());
							}
						}
						// 再注册客户端
						HashMap<String, Client> clients = srv.getClients();
						if (clients != null) {
							@SuppressWarnings("unchecked")
							List<Client> tmpClients = (List<Client>) MapCastList.convert(service.getClients());
							if(tmpClients!=null){
								for (Client tmpClient : tmpClients) {
									if (tmpClient.isUpdate() && tmpClient.isChanged()) {
										tmpClient.setLastModifyDate(now);
										clients.put(tmpClient.toString(), tmpClient);
									}
								}
							}

						} else {
							if (service.getClients() != null) {
								srv.setClients(service.getClients());
							} else {
								srv.setClients(new HashMap<String, Client>());
							}
						}
						// 再注册服务端
						HashMap<String, Server> servers = srv.getServers();
						if (servers != null) {
							if (tmp.isUpdate() && tmp.isChanged()) {
								if(!servers.containsKey(tmp.toString())){
									tmp.setLastModifyDate(now);
									servers.put(tmp.toString(), tmp);
									isPush = true;
								}
							}
						} else {
							if (service.getServers() != null) {
								srv.setServers(service.getServers());
								isPush = true;
							}
						}
						// 最后注册服务本身
						if (service.isChanged()) {
							srv.setInterfaces(service.getInterfaces());
							srv.setImplClazz(service.getImplClazz());
							srv.setPollingType(service.getPollingType());
							srv.setServiceName(service.getServiceName());
							srv.setStatus(service.getStatus());
							srv.setTimeout(service.getTimeout());
							srv.setAsyn(service.isAsyn());
							isPush = true;
						}
						srv.setUpdate(service.isUpdate());
						srv.setChanged(service.isChanged());
						srv.setLastModifyDate(now);
					}
				} else {
					service.setLastModifyDate(now);
					this.services.put(service.toString(), service);
					isPush = true;
				}
				// 更新了服务端信息时需要推送
				if (isPush) {
					addIssueQueue(this.services.get(service.toString()));
				}
				System.out.println(result + "[" + serverStr + "]" + ":注册成功!");
			}
			Set<Service> set = null;
			if (!serverMapServices.containsKey(serverStr)) {
				set = new HashSet<Service>();
				serverMapServices.put(serverStr, set);
			} else {
				set = serverMapServices.get(serverStr);
			}
			set.add(this.services.get(service.toString()));
		}
	}
	
	public Set<Service> getServiceByServer(Server server){
		return serverMapServices.get(server.toString());
	}
	
	/**
	 * TODO 等待实现 暂停服务端
	 * @param server
	 * @author:luohj
	 */
	public void pauseServer(Server server) {
		
	}

	/**
	 * TODO 等待实现 恢复服务端 
	 * @param server
	 * @author:luohj
	 */
	public void resumeServer(Server server) {
		
	}
	
	/**
	 * 停止服务（删除内存数据）
	 * @param server
	 * @author:luohj
	 */
	public void stopServer(Server server){
		if(serverMapServices.containsKey(server.toString())){
			Set<Service> set =serverMapServices.get(server.toString());
			//先更新服务
			Iterator<Service> it = set.iterator();
			while(it.hasNext()){
				Service svTmp = (Service) it.next();
				Service sv = services.get(svTmp.toString());
				synchronized(services){
					HashMap<String, Server> servers = sv.getServers();
					Iterator<String> serverKeyIt = servers.keySet().iterator();
					while(serverKeyIt.hasNext()){
						String key = serverKeyIt.next();
						Server serverTmp = servers.get(key);
						if(serverTmp.equals(server)){
							servers.remove(server.toString());
							break;
						}
					}
				}
				addIssueQueue(sv);
			}
			// 最后移除映射
			serverMapServices.remove(server.toString());
		}
	}

	protected void addIssueQueue(Service service) {
		if (issuedQueue == null) {
			synchronized (RegistryContainers.class) {
				if (issuedQueue == null) {
					issuedQueue = new IssuedQueue();
				}
			}
		}
		synchronized (this.issuedQueue) {
			this.issuedQueue.add(service);
		}
	}

	/**
	 * @return the services
	 */
	public HashMap<String, Service> getServices() {
		return services;
	}

	/**
	 * @param services
	 *            the services to set
	 */
	public void setServices(HashMap<String, Service> services) {
		this.services = services;
	}

	/**
	 * @return the issuedQueue
	 */
	public IssuedQueue getIssuedQueue() {
		return issuedQueue;
	}

	/**
	 * @param issuedQueue
	 *            the issuedQueue to set
	 */
	public void setIssuedQueue(IssuedQueue issuedQueue) {
		this.issuedQueue = issuedQueue;
	}

}
