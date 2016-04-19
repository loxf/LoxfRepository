/**
 * IssuedQueue.java
 * luohj - 下午4:58:05
 * 
 */
package org.loxf.registry.queue;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.loxf.registry.bean.Service;

/**
 * @author luohj
 *
 */
public class IssuedQueue extends ConcurrentLinkedQueue<Service> {
	private static final long serialVersionUID = 1L;
	
}
