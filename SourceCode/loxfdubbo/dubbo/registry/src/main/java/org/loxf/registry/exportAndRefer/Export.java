/**
 * Export.java
 * luohj - 下午5:02:49
 * 
 */
package org.loxf.registry.exportAndRefer;

import java.io.File;

import org.loxf.registry.bean.Service;

/**
 * @author luohj
 *
 */
public class Export {
	/**
	 * TODO:最基本的服务发布
	 * @param interfaces
	 * @param impl
	 * @param group
	 * @return
	 * @author:luohj
	 */
	public static Service export(Class<?> interfaces, Class<?> impl, String group){
		Service service = new Service();
		service.setInterfaces(interfaces.getName());
		service.setImplClazz(impl.getName());
		service.setServiceName(group==null? impl.getClass().getSimpleName():group);
		return null;
	}
	
	/**
	 * TODO:根据文件发布
	 * @param file
	 * @return
	 * @author:luohj
	 */
	public static Service export(File file){
		return null;
	}
}
