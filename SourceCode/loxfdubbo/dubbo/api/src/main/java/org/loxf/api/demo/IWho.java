/**
 * IWho.java
 * lenovo - 下午4:36:53
 * 
 */
package org.loxf.api.demo;

import org.loxf.core.interfaces.IBaseService;

/**
 * @author lenovo
 *
 */
public interface IWho extends IBaseService {
	String who();
	String iam(String name);
}
