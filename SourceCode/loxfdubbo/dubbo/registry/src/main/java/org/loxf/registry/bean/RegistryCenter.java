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
	
}
