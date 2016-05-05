/**
 * Method.java
 * luohj - 下午10:02:11
 * 
 */
package org.loxf.registry.invocation;

import org.loxf.registry.bean.BaseBean;

/**
 * @author luohj
 *
 */

public class Method extends BaseBean{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 方法名
	 */
	private String name;
	/**
	 * 参数列表
	 */
	private Class<?>[] parameterTypes;
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the parameterTypes
	 */
	public Class<?>[] getParameterTypes() {
		return parameterTypes;
	}
	/**
	 * @param parameterTypes the parameterTypes to set
	 */
	public void setParameterTypes(Class<?>[] parameterTypes) {
		this.parameterTypes = parameterTypes;
	}
}