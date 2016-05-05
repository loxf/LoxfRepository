/**
 * WhoGirl.java
 * lenovo - 下午4:39:13
 * 
 */
package org.loxf.service.impl;

import org.loxf.api.demo.IWho;
import org.loxf.registry.annotation.Method;
import org.loxf.registry.annotation.Provider;

/**
 * @author lenovo
 *
 */
@Provider(impl = WhoGirl.class, interfaces = IWho.class, group = "WhoGirl")
public class WhoGirl implements IWho {

	/** (non-Javadoc)
	 * @see org.loxf.service.interfaces.IWho#who()
	 */
	@Method
	public String who() {
		return "Who are you, girl?";
	}

	/** (non-Javadoc)
	 * @see org.loxf.api.demo.IWho#iam(java.lang.String)
	 */
	@Override
	@Method
	public String iam(String name) {
		return "I am " + name;
	}

}
