/**
 * HelloWorldImpl.java
 * lenovo - 下午4:38:28
 * 
 */
package org.loxf.service.impl;

import org.loxf.api.demo.IHello;
import org.loxf.api.demo.IWho;
import org.loxf.core.transcation.annotation.Transactional;
import org.loxf.registry.annotation.Customer;
import org.loxf.registry.annotation.Method;
import org.loxf.registry.annotation.Provider;
import org.loxf.registry.service.BaseService;

/**
 * @author lenovo
 *
 */
@Provider(interfaces = IHello.class)
@Transactional
public class HelloWorldImpl extends BaseService implements IHello {
	@Customer(group = "WhoBoy")
	IWho who;
	/** (non-Javadoc)
	 * @see org.loxf.service.interfaces.IHello#sayHi()
	 */
	@Method
	public String sayHi() {
		return "Hi, world! " + who.who();
	}

}
