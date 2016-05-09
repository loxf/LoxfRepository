/**
 * IssuedQueue.java
 * luohj - 下午4:58:05
 * 
 */
package org.loxf.registry.queue;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.loxf.registry.bean.Service;

/**
 * @author luohj
 *
 */
public class IssuedQueue extends ConcurrentLinkedQueue<Service> {
	private static final long serialVersionUID = 1L;
	private Map<String, String> serviceNames = new HashMap<String, String>();

	public boolean add(Service paramE) {
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

	public boolean offer(Service paramE) {
		if (this.contains(paramE)) {
			super.remove(paramE);
		} else {
			serviceNames.put(paramE.toString(), paramE.toString());
		}
		return super.offer(paramE);
	}

	public Service poll() {
		Service s = super.poll();
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
