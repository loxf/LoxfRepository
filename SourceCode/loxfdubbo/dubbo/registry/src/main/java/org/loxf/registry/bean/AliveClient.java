/**
 * AliveClient.java
 * lenovo - 下午7:22:51
 * 
 */
package org.loxf.registry.bean;

import org.springframework.util.StringUtils;

/**
 * @author lenovo
 *
 */
public class AliveClient extends BaseBean {

	private static final long serialVersionUID = 1L;
	private String ip;
	private int port;
	private String appName;
	/**
	 * 类型：客户端CUST，服务端SERV
	 */
	private String type;

	/**
	 * type-ip{:port}[appName]<br>
	 * eg:<br>
	 * CUST-127.0.0.1[CUST1]<br>
	 * SERV-127.0.0.1:2000[SERV1]
	 */
	public String toString() {
		return new StringBuffer().append(type+"-").append(ip).append(StringUtils.isEmpty(this.port) ? "" : ":" + this.port).append("[")
				.append(appName).append("]").toString();
	}

	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * @param ip
	 *            the ip to set
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
	 * @param port
	 *            the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return the appName
	 */
	public String getAppName() {
		return appName;
	}

	/**
	 * @param appName
	 *            the appName to set
	 */
	public void setAppName(String appName) {
		this.appName = appName;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

}
