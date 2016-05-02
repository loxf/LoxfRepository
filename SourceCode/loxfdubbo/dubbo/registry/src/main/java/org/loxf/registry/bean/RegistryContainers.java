/**
 * ServerContainers.java
 * luohj - 上午9:35:35
 * 
 */
package org.loxf.registry.bean;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.loxf.registry.queue.IssuedQueue;
import org.loxf.registry.utils.MapCastList;

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
	 * 推送服务队列
	 */
	private IssuedQueue issuedQueue;
	
	public RegistryContainers(){
		services = new HashMap<String, Service>();
		issuedQueue = new IssuedQueue();
	}
	
	/**
	 * TODO:注册服务组
	 * @param services
	 * @author:luohj
	 */
	public void registryServices(Service[] services) {
		if(services!=null&&services.length>0){
			for(Service tmp : services){
				registryService(tmp);
			}
		}
	}
	
	/**
	 * TODO:注册服务
	 * @param services
	 * @author:luohj
	 */
	public void registryService(Service service) {
		// 是否推送，当服务只是更改了方法与客户端信息时，不需要推送。
		boolean isPush = false;
		if(service!=null && service.isUpdate()){
			synchronized (this.services) {
				String result = service.toString();
				Date now = new Date();
				if(this.services.containsKey(service.toString())){
					Service srv = this.services.get(service.toString());
					if(srv!=null){
						//先注册方法
						HashMap<String, Method> methods = srv.getMethod();
						if(methods!=null){
							@SuppressWarnings("unchecked")
							List<Method> tmpMethods = (List<Method>) MapCastList.convert(service.getMethod());
							for(Method tmpMethod: tmpMethods){
								if(tmpMethod.isUpdate()&&tmpMethod.isChanged()){
									tmpMethod.setLastModifyDate(now);
									methods.put(tmpMethod.toString(), tmpMethod);
								}
							}
						} else {
							if(service.getMethod()!=null){
								srv.setMethod(service.getMethod());
							}
						}
						//再注册客户端
						HashMap<String, Client> clients = srv.getClients(); 
						if(clients!=null){
							List<Client> tmpClients = (List<Client>) service.getClients().values();
							for(Client tmpClient: tmpClients){
								if(tmpClient.isUpdate()&&tmpClient.isChanged()){
									tmpClient.setLastModifyDate(now);
									clients.put(tmpClient.toString(), tmpClient);
								}
							}
							
						} else {
							if(service.getClients()!=null){
								srv.setClients(service.getClients());
							}
						}
						//再注册服务端
						HashMap<String, Server> servers = srv.getServers(); 
						if(servers!=null){
							List<Server> tmpServers = (List<Server>) service.getServers().values();
							for(Server tmpSv: tmpServers){
								if(tmpSv.isUpdate()&&tmpSv.isChanged()){
									tmpSv.setLastModifyDate(now);
									servers.put(tmpSv.toString(), tmpSv);
									result += "[" + tmpSv.toString() + "]";
									isPush = true;
								}
							}
						} else {
							if(service.getServers()!=null){
								srv.setServers(service.getServers());
							}
						}
						//最后注册服务本身
						if(service.isChanged()){
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
				//更新了服务端信息时需要推送
				if(isPush){
					addIssueQueue(this.services.get(service.toString()));
				}
				System.out.println(result + ":注册成功!");

			}
		}
	}
	
	protected void addIssueQueue(Service service){
		if(issuedQueue==null){
			synchronized(RegistryContainers.class){
				if(issuedQueue==null){
					issuedQueue = new IssuedQueue();
					issuedQueue.add(service);
				} else {
					issuedQueue.add(service);
				}
			}
		} else {
			synchronized(this.issuedQueue){
				this.issuedQueue.add(service);
			}
		}
	}
	
	/**
	 * @return the services
	 */
	public HashMap<String, Service> getServices() {
		return services;
	}
	/**
	 * @param services the services to set
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
	 * @param issuedQueue the issuedQueue to set
	 */
	public void setIssuedQueue(IssuedQueue issuedQueue) {
		this.issuedQueue = issuedQueue;
	}

}
