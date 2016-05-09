/**
 * ReferUtil.java
 * {luohj} - 下午6:09:28
 * 
 */
package org.loxf.registry.utils;

import java.lang.reflect.Field;

import org.loxf.registry.annotation.Customer;
import org.loxf.registry.context.ApplicationContext;
import org.loxf.registry.main.ClientManager;
import org.loxf.registry.main.IClientManager;
import org.springframework.util.StringUtils;

/**
 * @author luohj
 *
 */
public class ReferUtil {
	public static void refer(Object o){
		ApplicationContext ctx = ApplicationContext.getInstance();
		IClientManager mgr = ClientManager.getClientManager();
		while(!mgr.isReady()){
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// 解析
		// List<Map<String, ?>> list = ReferUtil.parse(o.getClass());
		try {
			// 获取其全部的字段描述
			Field[] fields = o.getClass().getDeclaredFields();
			for (Field f : fields) {
				if (f != null && f.isAnnotationPresent(Customer.class)) {
					Customer refer = f.getAnnotation(Customer.class);
					String group = refer.group();
					Class<?> interfaces = f.getType();
					boolean asyn = refer.asyn();
					String key = interfaces.toString()
							+ (StringUtils.isEmpty(group) ? "" : ":" + group);
					if (!ctx.isExistsBean(key))
						ctx.setBean(key, mgr.refer((Class<?>) interfaces, group, asyn));
					
					// 允许访问private字段
					f.setAccessible(true);
					// 把引用对象注入属性
					f.set(o, ctx.getBean(key));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
