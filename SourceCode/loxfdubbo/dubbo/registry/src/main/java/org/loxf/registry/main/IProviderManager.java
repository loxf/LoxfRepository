/**
 * IProviderManager.java
 * luohj - 下午5:06:20
 * 
 */
package org.loxf.registry.main;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import org.loxf.registry.bean.Service;
import org.loxf.registry.invocation.Invocation;

/**
 * @author luohj
 *
 */
public interface IProviderManager {
	/**
	 * TODO:调用方法
	 * @param invo
	 * @return
	 * @author:luohj
	 * @throws ClassNotFoundException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
	Object call(Invocation invo) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException;
    /**
     * TODO:暴露服务
     * @param service
     * @author:luohj
     */
    public void export(Service service);  
    /**
     * TODO:暴露服务组
     * @param service
     * @author:luohj
     */
    public void export(Service[] services);  
    /**
     * TODO:暴露服务(配置文件形式)
     * @param file
     * @return
     * @author:luohj
     */
    public Service export(File file);
    /**
     * TODO:暴露服务(直接暴露)
     * @param interfaces
     * @param impl
     * @param group
     * @author:luohj
     */
    public void export(Class<?> interfaces, Class<?> impl, String group);
    /**
     * TODO:监听是否运行
     * @return
     * @author:luohj
     */
    public boolean isRunning();  
    /**
     * TODO:启动中心
     * @return
     * @author:luohj
     */
    public boolean start();  
    /**
     * TODO:获取监听端口
     * @return
     * @author:luohj
     */
    public int getPort(); 
    /**
     * TODO:重发服务（注册中心拓机重启后使用）
     * @author:luohj
     */
    public void reUploadService();
}
