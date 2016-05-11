/**
 * ServletContext.java
 * luohj - 下午1:55:53
 * 
 */
package org.loxf.registry.context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.loxf.registry.bean.ServletBean;
import org.loxf.registry.utils.ServletUtil;

/**
 * @author luohj
 *
 */
public class ServletContext {
	private static ServletContext ctx ;
	private Map<String, Object > map ;
	private Map<String, String> methodPathMapServlet;
	ServletContext(){
		map = new HashMap<String, Object> ();
		methodPathMapServlet = new HashMap<String, String> ();
	}
	@SuppressWarnings("unchecked")
	public <T> T getBean(String key){
		String servletKey = methodPathMapServlet.get(key);
		if(StringUtils.isEmpty(servletKey)){
			return null;
		}
		return (T) map.get(servletKey);
	}
	
	public void setBean(String key, Object o){
		map.put(key, o);
	}
	
	public boolean isExistsBean(String key){
		return map.containsKey(key);
	}
	
	public static ServletContext getInstance(){
		if(ctx==null){
			synchronized(ServletContext.class){
				if(ctx==null){
					ctx = new ServletContext();
				}
			}
		}
		return ctx;
	}
	
	public void load(String path){
		List<ServletBean> servletList = ServletUtil.parse(path);
		for(ServletBean servlet : servletList){
			Class<?> c = servlet.getServlet();
			map.put(servlet.toString(), servlet);
			for (String key : servlet.getMethod().keySet()) {
				methodPathMapServlet.put(key, c.getName());
			}
		}
	}
}
