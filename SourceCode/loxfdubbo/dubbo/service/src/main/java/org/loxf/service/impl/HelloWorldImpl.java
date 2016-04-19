/**
 * HelloWorldImpl.java
 * lenovo - 下午4:38:28
 * 
 */
package org.loxf.service.impl;

import org.loxf.api.demo.IHello;

/**
 * @author lenovo
 *
 */
public class HelloWorldImpl implements IHello {

	/* (non-Javadoc)
	 * @see org.loxf.service.interfaces.IHello#sayHi()
	 */
	@Override
	public String sayHi() {
		return "Hi, world!";
	}

}
