/**
 * Method.java
 * {luohj} - 下午6:29:42
 * 
 */
package org.loxf.registry.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author luohj
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Method {
	/**
	 * 服务异步调用（方法>服务）
	 * @return
	 * @author:luohj
	 */
	public boolean asyn() default false;
	/**
	 * 方法调用超时设置
	 * @return
	 * @author:luohj
	 */
	public int timeout() default 0;
	/**
	 * 负载均衡（方法>服务）
	 * @return
	 * @author:luohj
	 */
	public String pollingType() default "";
}
