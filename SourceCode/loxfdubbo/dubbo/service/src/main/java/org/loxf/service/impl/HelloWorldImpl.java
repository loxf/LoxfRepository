/**
 * HelloWorldImpl.java
 * lenovo - 下午4:38:28
 * 
 */
package org.loxf.service.impl;

import org.loxf.api.demo.IHello;
import org.loxf.registry.annotation.Method;
import org.loxf.registry.annotation.Provider;
import org.loxf.registry.service.BaseService;

/**
 * @author lenovo
 *
 */
@Provider(interfaces = IHello.class)
public class HelloWorldImpl extends BaseService implements IHello {
	/** (non-Javadoc)
	 * @see org.loxf.service.interfaces.IHello#sayHi()
	 */
	@Method
	public String sayHi() {
		return "Hi, world!";
	}

}
