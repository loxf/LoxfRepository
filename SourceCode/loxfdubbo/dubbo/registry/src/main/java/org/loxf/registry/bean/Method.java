package org.loxf.registry.bean;

import org.apache.commons.lang.StringUtils;

public class Method extends BaseBean{

	private static final long serialVersionUID = 1L;
	
	/**
	 * 方法名
	 */
	private String methodName;
	/**
	 * 参数列表
	 */
	private String[] params;
	/**
	 * 是否异步调用服务
	 * 同步标志的优先级：默认（同步）<客户端总配置<服务配置<方法配置
	 * true:异步；false:同步
	 */
	private boolean asyn;
	/**
	 * 服务调用timeout时间：毫秒<br>
	 * 描述：超过timeout时长服务未返回结果，直接报错timeout<br>
	 * 优先级：method > service <br>
	 * 默认1分钟
	 */
	private int timeout = 60000;
	/**
	 * 轮询方式<br>
	 * 轮询优先级：client < service < method <br>
	 * @see org.loxf.registry.constracts.PollingType
	 */
	private String pollingType;
	
	public Method(String name, String[] parameterTypes) {
		this.methodName = name;
		this.params = parameterTypes;
	}
	
	/**
	 * methodName(param0, param1, ... , paramN)
	 */
	public String toString() {
		return new StringBuffer().append(methodName).append("(").append(StringUtils.join(params, " ,")).append(")")
				.toString();
	}
	
	/**
	 * @return the methodName
	 */
	public String getMethodName() {
		return methodName;
	}
	/**
	 * @param methodName the methodName to set
	 */
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	/**
	 * @return the params
	 */
	public String[] getParams() {
		return params;
	}
	/**
	 * @param params the params to set
	 */
	public void setParams(String[] params) {
		this.params = params;
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

}
