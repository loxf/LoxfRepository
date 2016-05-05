/**
 * ExportUtil.java
 * {luohj} - 下午6:09:19
 * 
 */
package org.loxf.registry.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import org.loxf.registry.annotation.Provider;
import org.loxf.registry.bean.Method;
import org.loxf.registry.bean.Service;
import org.springframework.util.StringUtils;

/**
 * @author luohj
 *
 */
public class ExportUtil {
	public static Service parse(Class<?> clazz) throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		Annotation[] classAnos = clazz.getAnnotations();
		boolean flag = false;
		if (classAnos != null) {
			for (Annotation ano : classAnos) {
				if (ano.annotationType().equals(Provider.class)) {
					flag = true;
					Provider provider = clazz.getAnnotation(Provider.class);
					if (provider != null) {
						Service service = new Service();
						service.setAsyn(provider.asyn());
						service.setInterfaces(provider.interfaces().getName());
						service.setImplClazz(provider.impl().getName());
						service.setServiceName(StringUtils.isEmpty(provider.group()) ? ""
								: provider.group());
						service.setTimeout(provider.timeout());
						service.setStatus(provider.status().getValue());

						java.lang.reflect.Method[] _methods = clazz.getDeclaredMethods();
						if (_methods != null && _methods.length > 0) {
							HashMap<String, Method> methods = new HashMap<String, Method>();
							for (java.lang.reflect.Method _method : _methods) {
								Annotation[] anos = _method.getAnnotations();
								for (Annotation a : anos) {
									if (a.annotationType().equals(org.loxf.registry.annotation.Method.class)) {
										org.loxf.registry.annotation.Method method = _method
												.getAnnotation(org.loxf.registry.annotation.Method.class);
										if (method != null) {
											Method m = new Method();
											m.setAsyn(method.asyn());
											m.setPollingType(method.pollingType());
											m.setTimeout(method.timeout());
											m.setMethodName(_method.getName());
											Class<?>[] paramTypes = _method.getParameterTypes();
											String[] paramTypeStrs = new String[paramTypes.length];
											int i = 0;
											for (Class<?> type : paramTypes) {
												paramTypeStrs[i++] = type.getName();
											}
											m.setParams(paramTypeStrs);
											methods.put(m.toString(), m);
										}
									}
								}
							}
							service.setMethod(methods);
						}
						return service;
					} else {
						throw new RuntimeException("服务[" + clazz.getName() + "]暴露失败，当前实现类未注解为服务提供者。");
					}
				}
			}
			if (!flag) {
				throw new RuntimeException("服务[" + clazz.getName() + "]暴露失败，当前实现类未注解为服务提供者。");
			}
			return null;
		} else {
			throw new RuntimeException("服务[" + clazz.getName() + "]暴露失败，当前实现类未注解为服务提供者。");
		}
	}

}
