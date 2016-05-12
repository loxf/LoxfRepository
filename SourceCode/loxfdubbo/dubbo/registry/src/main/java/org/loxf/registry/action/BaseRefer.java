/**
 * BaseRefer.java
 * luohj - 下午5:27:29
 * 
 */
package org.loxf.registry.action;

import org.loxf.registry.utils.ReferUtil;

/**
 * @author luohj
 *
 */
public class BaseRefer {
	public BaseRefer(){
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
