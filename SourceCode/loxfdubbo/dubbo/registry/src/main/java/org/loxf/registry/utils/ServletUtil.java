/**
 * ServletUtil.java
 * luohj - 下午7:04:03
 * 
 */
package org.loxf.registry.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.loxf.registry.annotation.Controller;
import org.loxf.registry.annotation.RequestMapping;
import org.loxf.registry.bean.ServletBean;

/**
 * @author luohj
 *
 */
public class ServletUtil {
	public static List<ServletBean> parse(String packagePath) {
		Set<Class<?>> set = ScanPackage.getClasses(packagePath);
		List<ServletBean> serviceList = new ArrayList<ServletBean>();
		for (Class<?> cl : set) {
			ServletBean srv = ServletUtil.parse(cl);
			if (srv != null) {
				serviceList.add(srv);
			}
		}
		return serviceList;
	}

	public static ServletBean parse(Class<?> clazz) {
		Annotation[] classAnos = clazz.getAnnotations();
		if (classAnos != null) {
			for (Annotation ano : classAnos) {
				if (ano.annotationType().equals(Controller.class)) {
					Controller ctr = clazz.getAnnotation(Controller.class);
					if (ctr != null) {
						ServletBean servlet = new ServletBean();
						servlet.setServlet(clazz);
						HashMap<String, Method> methods = new HashMap<String, Method>();
						servlet.setMethod(methods);
						java.lang.reflect.Method[] _methods = clazz.getDeclaredMethods();
						if (_methods != null && _methods.length > 0) {
							for (java.lang.reflect.Method _method : _methods) {
								Annotation[] anos = _method.getAnnotations();
								for (Annotation a : anos) {
									if (a.annotationType().equals(RequestMapping.class)) {
										RequestMapping req = _method.getAnnotation(RequestMapping.class);
										methods.put(req.value(), _method);
										break;
									}
								}
							}
						}
						return servlet;
					}
				}
			}
		}
		return null;
	}
}
