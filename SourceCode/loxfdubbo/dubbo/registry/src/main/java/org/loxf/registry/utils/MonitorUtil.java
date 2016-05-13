/**
 * MonitorUtil.java
 * luohj - 下午8:43:48
 * 
 */
package org.loxf.registry.utils;

/**
 * @author luohj
 *
 */
public class MonitorUtil {
	boolean isMonitor ; 
	Class<?> clazz ;
	public MonitorUtil(Class<?> clazz, boolean isMonitor){
		this.isMonitor = !isMonitor;
		this.clazz = clazz;
		if(isMonitor)
			System.out.println(clazz.getName() + "：监视器打开");
	}
	public void beforeRefer(){
		if(isMonitor)
			System.out.println(clazz.getName() + ":调用前监控...");
	}
	public void afterRefer(){
		if(isMonitor)
			System.out.println(clazz.getName() + ":调用后监控...");		
	}
}
