/**
 * ReferUtil.java
 * {luohj} - 下午6:09:28
 * 
 */
package org.loxf.registry.utils;

import java.lang.reflect.Field;

import org.loxf.core.transcation.bean.Transaction;
import org.loxf.registry.annotation.Customer;
import org.loxf.registry.main.ClientManager;
import org.loxf.registry.main.IClientManager;

/**
 * @author luohj
 *
 */
public class ReferUtil {
	public static void referInAction(Object o){
		IClientManager mgr = ClientManager.getClientManager();
		// 解析
		// List<Map<String, ?>> list = ReferUtil.parse(o.getClass());
		try {
			// 获取其全部的字段描述
			Field[] fields = o.getClass().getDeclaredFields();
			for (Field f : fields) {
				if (f != null && f.isAnnotationPresent(Customer.class)) {
					Customer refer = f.getAnnotation(Customer.class);
					// 允许访问private字段
					f.setAccessible(true);
					// 把引用对象注入属性
					f.set(o, mgr.refer(f.getType(), refer, null));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void referInService(Object o, Transaction tr){
		IClientManager mgr = ClientManager.getClientManager();
		// 解析
		// List<Map<String, ?>> list = ReferUtil.parse(o.getClass());
		try {
			// 获取其全部的字段描述
			Field[] fields = o.getClass().getDeclaredFields();
			for (Field f : fields) {
				if (f != null && f.isAnnotationPresent(Customer.class)) {
					Customer refer = f.getAnnotation(Customer.class);
					// 允许访问private字段
					f.setAccessible(true);
					// 把引用对象注入属性
					f.set(o, mgr.refer(f.getType(), refer, tr, false));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
