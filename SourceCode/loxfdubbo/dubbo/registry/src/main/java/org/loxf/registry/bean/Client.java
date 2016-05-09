package org.loxf.registry.bean;

import org.springframework.util.StringUtils;

/**
 * 客户端
 * @author luohj
 *
 */
public class Client extends BaseBean {
	private static final long serialVersionUID = 1L;

	public Client(){
		super(true);
	}
	/**
	 * 客户端地址（IP/HOST）
	 */
	private String clientAddr;
	/**
	 * 客户端接受服务端推送的端口
	 */
	private String clientPort;
	/**
	 * 客户端名称
	 */
	private String clientName;
	/**
	 * 是否异步调用服务<br>
	 * 同步标志的优先级：默认（同步）<客户端总配置<服务配置<方法配置 <br>
	 * true:异步；false:同步
	 */
	private boolean asyn;
	/**
	 * 轮询方式<br>
	 * 轮询优先级：client < service <br>
	 * @see org.loxf.registry.constracts.PollingType
	 */
	private String pollingType;
	/**
	 * 失效时间：毫秒（注册中心timeout时长过后，删除client信息）<br>
	 * 默认10分钟
	 */
	private int timeout;
	
	public Client(String clientAddr){
		this.clientAddr = clientAddr;
		this.asyn = false;
		this.timeout = 600000;
	}
	
	public Client(String clientAddr, String appName){
		this.clientAddr = clientAddr;
		this.clientName = appName;
		this.asyn = false;
		this.timeout = 600000;
	}
	
	public Client(String clientAddr, String appName, boolean asyn){
		this.clientAddr = clientAddr;
		this.clientName = appName;
		this.asyn = asyn;
		this.timeout = 600000;
	}

	/**
	 * IP:PORT[APPNAME]
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return (this.clientAddr + (StringUtils.isEmpty(this.clientPort) ? "" : (":" + this.clientPort)) 
				+ "[" + this.clientName + "]");
	}
	
	/** 
	 * toString().hashCode()
	 * @see Client#toString()
	 */
	public int hashCode(){
		return this.toString().hashCode();
	}

	/**
	 * @return the clientAddr
	 */
	public String getClientAddr() {
		return clientAddr;
	}

	/**
	 * @param clientAddr the clientAddr to set
	 */
	public void setClientAddr(String clientAddr) {
		this.clientAddr = clientAddr;
	}

	/**
	 * @return the clientPort
	 */
	public String getClientPort() {
		return clientPort;
	}

	/**
	 * @param clientPort the clientPort to set
	 */
	public void setClientPort(String clientPort) {
		this.clientPort = clientPort;
	}

	/**
	 * @return the clientName
	 */
	public String getClientName() {
		return clientName;
	}

	/**
	 * @param clientName the clientName to set
	 */
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	/**
	 * @return the asyn
	 */
	public boolean isAsyn() {
		return asyn;
	}

	/**
	 * @param asyn the asyn to set
	 */
	public void setAsyn(boolean asyn) {
		this.asyn = asyn;
	}

	/**
	 * @return the pollingType
	 */
	public String getPollingType() {
		return pollingType;
	}
	
	/**
	 * @param pollingType the pollingType to set
	 */
	public void setPollingType(String pollingType) {
		this.pollingType = pollingType;
	}

	/**
	 * @return the timeout
	 */
	public int getTimeout() {
		return timeout;
	}

	/**
	 * @param timeout the timeout to set
	 */
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
}
