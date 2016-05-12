/**
 * IWho.java
 * lenovo - 下午4:36:53
 * 
 */
package org.loxf.api.demo;

import org.loxf.core.interfaces.IBase;

/**
 * @author lenovo
 *
 */
public interface IWho extends IBase {
	String who();
	String iam(String name);
}
