/**
 * BaseAction.java
 * luohj - 上午11:11:07
 * 
 */
package org.loxf.dubbo.action;

import org.loxf.registry.utils.ReferUtil;

/**
 * @author luohj
 *
 */
public class BaseAction implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	BaseAction(){
		instancesBean(this);
	}
	/**
	 * 实例化bean，注入远程调用
	 * 
	 * @param clazz
	 * @author:luohj
	 */
	protected void instancesBean(Object o) {
		ReferUtil.refer(o);
	}
}
