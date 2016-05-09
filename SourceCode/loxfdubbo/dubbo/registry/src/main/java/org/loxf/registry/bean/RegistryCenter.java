/**
 * RegistryCenter.java
 * lenovo - 下午9:28:27
 * 
 */
package org.loxf.registry.bean;

/**
 * 注册中心实体
 * @author lenovo
 *
 */
public class RegistryCenter extends BaseBean {
	private static final long serialVersionUID = 1L;
	private String ip;
	private int port;
	/**
	 * 生效：EFF, 失效：EXP, 保持：HOLD（失效后，客户端做出处理后，改状态为保持）<br>
	 * 对于服务端：<br>
	 * 默认注册中心生效（EFF），由心跳线程维护此状态，当心跳线程检测不到注册中心的存在，改状态为失效（EXP），
	 * 失效状态由重发线程维护，重发线程检测到状态失效后，将所有的services数据重发到上载队列，并改状态为保持（HOLD），
	 * 保持状态由心跳线程维护，当心跳检测到注册中心重新连接成功，改保持状态为生效（EFF）。<br>
	 * 注意：服务上载线程只在注册中心生效时处理上载请求。<br>
	 * 对于客户端：<br>
	 * 
	 */
	private String status = "EFF";
	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}
	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}
	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}
	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
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
