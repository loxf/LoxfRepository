/**
 * ExportUtil.java
 * {luohj} - 下午6:09:19
 * 
 */
package org.loxf.registry.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

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
						if (!provider.interfaces().isInterface()) {
							throw new IllegalArgumentException(
									"The " + provider.interfaces().getName() + " must be interface class!");
						}
						service.setInterfaces(provider.interfaces().getName());
						service.setImplClazz(clazz.getName());
						service.setAsyn(provider.asyn());
						service.setServiceName(StringUtils.isEmpty(provider.group()) ? "" : provider.group());
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
						// throw new RuntimeException("服务[" + clazz.getName() +
						// "]暴露失败，当前实现类未注解为服务提供者。");
					}
				}
			}
			if (!flag) {
				// throw new RuntimeException("服务[" + clazz.getName() +
				// "]暴露失败，当前实现类未注解为服务提供者。");
			}
		} else {
			// throw new RuntimeException("服务[" + clazz.getName() +
			// "]暴露失败，当前实现类未注解为服务提供者。");
		}
		return null;
	}

	/**
	 * TODO:方法描述
	 * @param packagePath 包路径，不支持通配
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @author:luohj
	 */
	public static Service[] parse(String packagePath) throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		Set<Class<?>> set = ScanPackage.getClasses(packagePath);
		List<Service> serviceList = new ArrayList<Service>();
		for (Class<?> cl : set) {
			Service srv = ExportUtil.parse(cl);
			if (srv != null) {
				serviceList.add(srv);
			}
		}
		if(serviceList.isEmpty()){
			return null;
		}
		Service[] srvs = new Service[serviceList.size()];
		return serviceList.toArray(srvs);
	}
}
