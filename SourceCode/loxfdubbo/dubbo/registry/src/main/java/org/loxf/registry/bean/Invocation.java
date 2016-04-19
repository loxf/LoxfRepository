package org.loxf.registry.bean;

import java.util.Arrays;
import java.lang.reflect.Method;

public class Invocation extends BaseBean{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Class<?> interfaces;
	private String group;
	private Method method;
	private Object[] params;
	private Object result;
	private String appName;
	private String ip;
	private boolean asyn;
	
	
	/**
	 * @return the result
	 */
	public Object getResult() {
		return result;
	}
	/**
	 * @param result the result to set
	 */
	public void setResult(Object result) {
		this.result = result;
	}
	/**
	 * @return the interfaces
	 */
	public Class<?> getInterfaces() {
		return interfaces;
	}
	/**
	 * @param interfaces the interfaces to set
	 */
	public void setInterfaces(Class<?> interfaces) {
		this.interfaces = interfaces;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
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
	 * @return the params
	 */
	public Object[] getParams() {
		return params;
	}
	/**
	 * @param params the params to set
	 */
	public void setParams(Object[] params) {
		this.params = params;
	}
	@Override
	public String toString() {
		return interfaces.getName()+"."+method.getName()+"("+Arrays.toString(params)+")";
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
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
	
}
