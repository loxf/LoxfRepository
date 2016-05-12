package org.loxf.registry.bean;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

/**
 * 服务
 * 
 * @author luohj
 *
 */
public class Service extends BaseBean {

	private static final long serialVersionUID = 1L;
	public Service(){
		super(true);
	}
	/**
	 * 服务接口
	 */
	private String interfaces;
	/**
	 * 服务实现类
	 */
	private String implClazz;
	/**
	 * 方法信息
	 */
	private HashMap<String, Method> methods;
	/**
	 * 服务名称/分组名称
	 */
	private String serviceName;
	/**
	 * 服务状态：<br>
	 * 生效：EFF<br>
	 * 失效：EXP
	 */
	private String status;
	/**
	 * 服务调用timeout时间：毫秒<br>
	 * 描述：超过timeout时长服务未返回结果，直接报错timeout<br>
	 * 默认1分钟
	 */
	private int timeout = 60000;
	/**
	 * 轮询方式<br>
	 * 轮询优先级：client < service < method <br>
	 * 
	 * @see org.loxf.registry.constracts.PollingType
	 */
	private String pollingType;
	/**
	 * 客户端信息<br>
	 * key=client.hashcode(), value=client
	 */
	private HashMap<String, Client> clients;
	/**
	 * 服务端信息<br>
	 * key=server.hashcode(), value=server
	 */
	private HashMap<String, Server> servers;
	/**
	 * 是否异步调用服务 同步标志的优先级：默认（同步）<客户端总配置<服务配置<方法配置 true:异步；false:同步
	 */
	private boolean asyn;

	/** 
	 * interfaces:serviceName<br>
	 * eg:<br>
	 * org.loxf.test.service.ITestService:test1
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new StringBuffer().append(this.interfaces)
				.append((StringUtils.isBlank(this.serviceName) ? "" : ":" + this.serviceName)).toString();
	}

	public int hashCode() {
		return this.toString().hashCode();
	}

	/**
	 * @return the interfaces
	 */
	public String getInterfaces() {
		return interfaces;
	}

	/**
	 * @param interfaces
	 *            the interfaces to set
	 */
	public void setInterfaces(String interfaces) {
		this.interfaces = interfaces;
	}

	/**
	 * @return the implClazz
	 */
	public String getImplClazz() {
		return implClazz;
	}

	/**
	 * @param implClazz
	 *            the implClazz to set
	 */
	public void setImplClazz(String implClazz) {
		this.implClazz = implClazz;
	}

	/**
	 * @return the method
	 */
	public HashMap<String, Method> getMethod() {
		return methods;
	}

	/**
	 * @param method
	 *            the method to set
	 */
	public void setMethod(HashMap<String, Method> methods) {
		this.methods = methods;
	}

	/**
	 * @return the serviceName
	 */
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * @param serviceName
	 *            the serviceName to set
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the timeout
	 */
	public int getTimeout() {
		return timeout;
	}

	/**
	 * @param timeout
	 *            the timeout to set
	 */
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	/**
	 * @return the pollingType
	 */
	public String getPollingType() {
		return pollingType;
	}

	/**
	 * @param pollingType
	 *            the pollingType to set
	 */
	public void setPollingType(String pollingType) {
		this.pollingType = pollingType;
	}

	/**
	 * @return the clients
	 */
	public HashMap<String, Client> getClients() {
		return clients;
	}

	/**
	 * @param clients
	 *            the clients to set
	 */
	public void setClients(HashMap<String, Client> clients) {
		this.clients = clients;
	}

	/**
	 * @return the servers
	 */
	public HashMap<String, Server> getServers() {
		return servers;
	}

	/**
	 * @param servers
	 *            the servers to set
	 */
	public void setServers(HashMap<String, Server> servers) {
		this.servers = servers;
	}

	/**
	 * @return the asyn
	 */
	public boolean isAsyn() {
		return asyn;
	}

	/**
	 * @param asyn
	 *            the asyn to set
	 */
	public void setAsyn(boolean asyn) {
		this.asyn = asyn;
	}

	/**
	 * 获取指定的Server
	 * @param key
	 * @return
	 * @author:luohj
	 */
	public Server getServer(String key) {
		return (servers == null ? null : servers.get(key));
	}
}
