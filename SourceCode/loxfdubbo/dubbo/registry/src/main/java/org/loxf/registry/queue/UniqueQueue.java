/**
 * UniqueQueue.java
 * luohj - 下午10:26:24
 * 
 */
package org.loxf.registry.queue;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author luohj
 *
 */
public class UniqueQueue <E> extends ConcurrentLinkedQueue<E>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Map<String, String> serviceNames = new HashMap<String, String>();

	public boolean add(E paramE) {
		if (this.contains(paramE)) {
			super.remove(paramE);
		} else {
			serviceNames.put(paramE.toString(), paramE.toString());
		}
		return super.offer(paramE);
	}

	public boolean remove(Object paramObject) {
		if (this.contains(paramObject)) {
			serviceNames.remove(paramObject.toString());
			return super.remove(paramObject);
		}
		return true;
	}

	public boolean offer(E paramE) {
		if (this.contains(paramE)) {
			super.remove(paramE);
		} else {
			serviceNames.put(paramE.toString(), paramE.toString());
		}
		return super.offer(paramE);
	}

	public E poll() {
		E s = super.poll();
		if(s==null){
			return null;
		}
		serviceNames.remove(s.toString());
		return s;
	}

	public void clear() {
		while (this.poll() != null) {}
	}

	public boolean contains(Object paramObject) {
		if (serviceNames.containsKey(paramObject.toString())) {
			return true;
		}
		return false;
	}
}
