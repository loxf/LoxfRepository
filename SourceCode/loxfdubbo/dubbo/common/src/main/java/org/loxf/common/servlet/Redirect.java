/**
 * Redirect.java
 * luohj - 下午9:39:41
 * 
 */
package org.loxf.common.servlet;

/**
 * @author luohj
 *
 */
public class Redirect {
	private String location;

	public Redirect(String addr){
		location = addr;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}
}
