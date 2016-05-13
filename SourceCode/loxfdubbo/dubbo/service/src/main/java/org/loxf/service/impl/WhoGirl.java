/**
 * WhoGirl.java
 * lenovo - 下午4:39:13
 * 
 */
package org.loxf.service.impl;

import org.loxf.api.demo.IHello;
import org.loxf.api.demo.IWho;
import org.loxf.registry.annotation.Customer;
import org.loxf.registry.annotation.Method;
import org.loxf.registry.annotation.Provider;
import org.loxf.registry.service.BaseService;

/**
 * @author lenovo
 *
 */
@Provider(interfaces = IWho.class, group = "WhoGirl")
public class WhoGirl extends BaseService implements IWho {

	@Customer
	private IHello hello;
	
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
		return hello.sayHi() + "I am " + name;
	}

}
