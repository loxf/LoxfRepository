/**
 * IProviderManager.java
 * luohj - 下午5:06:20
 * 
 */
package org.loxf.registry.main;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import org.loxf.registry.bean.Service;
import org.loxf.registry.invocation.Invocation;

/**
 * @author luohj
 *
 */
public interface IProviderManager {
	/**
	 * 调用方法
	 * 
	 * @param invo
	 * @return
	 * @author:luohj
	 * @throws ClassNotFoundException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	Object call(Invocation invo) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			ClassNotFoundException, NoSuchMethodException, InstantiationException;

	/**
	 * 暴露服务
	 * 
	 * @param service
	 * @author:luohj
	 */
	public void export(Service service);

	/**
	 * 暴露服务组
	 * 
	 * @param service
	 * @author:luohj
	 */
	public void export(Service[] services);

	/**
	 * 暴露服务
	 * 
	 * @param file
	 * @return
	 * @author:luohj
	 */
	public void export();

	/**
	 * 暴露服务(直接暴露)
	 * 
	 * @param interfaces
	 * @param impl
	 * @param group
	 * @author:luohj
	 */
	public void export(Class<?> interfaces, Class<?> impl, String group);

	/**
	 * 监听是否运行
	 * 
	 * @return
	 * @author:luohj
	 */
	public boolean isRunning();

	/**
	 * 启动中心
	 * 
	 * @return
	 * @author:luohj
	 */
	public boolean start();

	/**
	 * 获取监听端口
	 * 
	 * @return
	 * @author:luohj
	 */
	public int getPort();

	/**
	 * 重发服务（注册中心拓机重启后使用）
	 * 
	 * @author:luohj
	 */
	public void reUploadService();

	/**
	 * 初始化
	 * 
	 * @param path
	 *            配置文件路径
	 * @author:luohj
	 */
	public void init(String path) throws IOException;

	/**
	 * 初始化
	 * 
	 * @param path
	 *            配置文件
	 * @author:luohj
	 */
	public void init(Properties properties);
}
