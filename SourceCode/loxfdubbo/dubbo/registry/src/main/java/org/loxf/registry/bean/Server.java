/**
 * Server.java
 * luohj - 下午5:10:46
 * 
 */
package org.loxf.registry.bean;

/**
 * @author luohj
 *
 */
public class Server extends BaseBean {
	private static final long serialVersionUID = 1L;
	public Server(){
		super(true);
	}
	
	/**
	 * 提供者地址
	 */
	private String serverAddr;
	/**
	 * 提供者端口
	 */
	private int serverPort;
	/**
	 * 提供者名称
	 */
	private String serverName;
	/**
	 * 提供者状态：有效 EFF，暂停服务 PAUSE，失效 EXP
	 */
	private String status;
	/**
	 * 失效时间毫秒（注册中心timeout时长过后，删除server信息）
	 */
	private long timeout;
	
	public Server(String serverAddr, int serverPort){
		this.serverAddr = serverAddr;
		this.serverPort = serverPort;
	}
	/**
	 * return serverAddr:serverPort
	 */
	public String toString() {
		return new StringBuffer().append(this.serverAddr).append(":").append(this.serverPort).append("[")
				.append(serverName).append("]").toString();
	}
	
	public boolean equals(Object o){
		if(this.serverAddr.equals(((Server)o).getServerAddr())&&this.serverPort==((Server)o).getServerPort()){
			return true;
		}
		return false;
	}

	/**
	 * @return the serverAddr
	 */
	public String getServerAddr() {
		return serverAddr;
	}

	/**
	 * @param serverAddr
	 *            the serverAddr to set
	 */
	public void setServerAddr(String serverAddr) {
		this.serverAddr = serverAddr;
	}

	/**
	 * @return the serverPort
	 */
	public int getServerPort() {
		return serverPort;
	}

	/**
	 * @param serverPort
	 *            the serverPort to set
	 */
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	/**
	 * @return the serverName
	 */
	public String getServerName() {
		return serverName;
	}

	/**
	 * @param serverName
	 *            the serverName to set
	 */
	public void setServerName(String serverName) {
		this.serverName = serverName;
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
	public long getTimeout() {
		return timeout;
	}

	/**
	 * @param timeout
	 *            the timeout to set
	 */
	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}
}
