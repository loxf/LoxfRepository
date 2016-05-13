/**
 * ApplicationContext.java
 * luohj - 上午10:34:18
 * 
 */
package org.loxf.core.context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.loxf.core.servlet.bean.ServletBean;
import org.loxf.core.utils.ServletUtil;

/**
 * 统一容器管理工具
 * @author luohj
 *
 */
public class ApplicationContext extends BaseContext {
	private static ApplicationContext ctx ;
	private Map<String, String> methodPathMapServlet;
	
	public ApplicationContext(){
		super();
		methodPathMapServlet = new HashMap<String, String> ();
	}
	public static ApplicationContext getInstance(){
		if(ctx==null){
			synchronized(ApplicationContext.class){
				if(ctx==null){
					ctx = new ApplicationContext();
				}
			}
		}
		return ctx;
	}
	
	public void setLocalBeans(Object[] t){
		for(Object o : t){
			setLocalBean(o);
		}
	}
	
	public void setLocalBean(Object o){
		setBean(o.toString() + ":jvm", o);
	}
	
	public void setReferBeans(Object[] t){
		for(Object o : t){
			setReferBean(o);
		}
	}
	
	public void setReferBean(Object o){
		setBean(o.toString() + ":refer", o);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getServlet(String key){
		String servletKey = methodPathMapServlet.get(key);
		if(StringUtils.isEmpty(servletKey)){
			return null;
		}
		return (T) getMap().get(servletKey);
	}
	
	public void loadServlet(String path){
		List<ServletBean> servletList = ServletUtil.parse(path);
		for(ServletBean servlet : servletList){
			Class<?> c = servlet.getServlet();
			getMap().put(servlet.toString(), servlet);
			for (String key : servlet.getMethod().keySet()) {
				methodPathMapServlet.put(key, c.getName());
			}
		}
	}
}
