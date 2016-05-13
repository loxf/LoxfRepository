/**
 * BaseRefer.java
 * luohj - 下午5:27:29
 * 
 */
package org.loxf.registry.service;

import org.loxf.core.interfaces.IBaseService;
import org.loxf.core.service.BaseImpl;
import org.loxf.registry.utils.ReferUtil;

/**
 * @author luohj
 *
 */
public class BaseService extends BaseImpl implements IBaseService{
	
	public BaseService(){}
	/**
	 * 实例化bean，注入远程调用
	 * 
	 * @param clazz
	 * @author:luohj
	 */
	public void init() {
		ReferUtil.referInService(this, this.getTransaction());
	}
	
}
