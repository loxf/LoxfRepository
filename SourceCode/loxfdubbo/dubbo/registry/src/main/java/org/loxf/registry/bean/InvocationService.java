/**
 * InvocationService.java
 * luohj - 下午2:54:56
 * 
 */
package org.loxf.registry.bean;

import org.apache.commons.lang.StringUtils;

/**
 * @author luohj
 *
 */
public class InvocationService extends BaseBean {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 注册：REG	请求：REF
	 */
	private String oper;
	/**
	 * 接口
	 */
	private String interfaces;
	/**
	 * 实现
	 */
	private String impl;
	/**
	 * 服务名称/分组名称
	 */
	private String serviceName;
	/**
	 * 提供者端信息
	 */
	private Server server;
	/**
	 * 消费者端信息
	 */
	private Client client;
	/**
	 * 方法
	 */
	private Method method;
	/**
	 * 状态
	 */
	private String status;

	public String toString() {
		return new StringBuffer().append(this.interfaces)
				.append((StringUtils.isBlank(this.serviceName) ? "" : ":" + this.serviceName)).toString();
	}
	
	/**
	 * @return the oper
	 */
	public String getOper() {
		return oper;
	}
	/**
	 * @param oper the oper to set
	 */
	public void setOper(String oper) {
		this.oper = oper;
	}
	/**
	 * @return the interfaces
	 */
	public String getInterfaces() {
		return interfaces;
	}
	/**
	 * @param interfaces the interfaces to set
	 */
	public void setInterfaces(String interfaces) {
		this.interfaces = interfaces;
	}
	/**
	 * @return the impl
	 */
	public String getImpl() {
		return impl;
	}
	/**
	 * @param impl the impl to set
	 */
	public void setImpl(String impl) {
		this.impl = impl;
	}
	/**
	 * @return the serviceName
	 */
	public String getServiceName() {
		return serviceName;
	}
	/**
	 * @param serviceName the serviceName to set
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	/**
	 * @return the server
	 */
	public Server getServer() {
		return server;
	}

	/**
	 * @param server the server to set
	 */
	public void setServer(Server server) {
		this.server = server;
	}

	/**
	 * @return the client
	 */
	public Client getClient() {
		return client;
	}

	/**
	 * @param client the client to set
	 */
	public void setClient(Client client) {
		this.client = client;
	}

	/**
	 * @return the method
	 */
	public Method getMethod() {
		return method;
	}
	/**
	 * @param method the method to set
	 */
	public void setMethod(Method method) {
		this.method = method;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
}
