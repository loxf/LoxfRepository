/**
 * BaseRefer.java
 * luohj - 下午5:27:29
 * 
 */
package org.loxf.registry.utils;

/**
 * @author luohj
 *
 */
public class BaseRefer {
	/**
	 * 实例化bean
	 * 
	 * @param clazz
	 * @author:luohj
	 */
	protected void instancesBean(Object o) {
		ReferUtil.refer(o);
	}

}
