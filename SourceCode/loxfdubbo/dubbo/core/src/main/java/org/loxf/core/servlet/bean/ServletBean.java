/**
 * ServletBean.java
 * luohj - 下午5:35:27
 * 
 */
package org.loxf.core.servlet.bean;

import java.util.HashMap;

/**
 * @author luohj
 *
 */
public class ServletBean {
	private Class<?> servlet;
	private HashMap<String, java.lang.reflect.Method> method;
	
	public String toString(){
		return servlet.getName();
	}
	/**
	 * @return the servlet
	 */
	public Class<?> getServlet() {
		return servlet;
	}
	/**
	 * @param servlet the servlet to set
	 */
	public void setServlet(Class<?> servlet) {
		this.servlet = servlet;
	}
	/**
	 * @return the method
	 */
	public HashMap<String, java.lang.reflect.Method> getMethod() {
		return method;
	}
	/**
	 * @param method the method to set
	 */
	public void setMethod(HashMap<String, java.lang.reflect.Method> method) {
		this.method = method;
	}
}
