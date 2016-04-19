/**
 * WhoBoy.java
 * lenovo - 下午4:39:22
 * 
 */
package org.loxf.service.impl;

import org.loxf.api.demo.IWho;

/**
 * @author lenovo
 *
 */
public class WhoBoy implements IWho {

	/* (non-Javadoc)
	 * @see org.loxf.service.interfaces.IWho#who()
	 */
	@Override
	public String who() {
		return "Who are you, boy?";
	}

}
