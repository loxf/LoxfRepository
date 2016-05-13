/**
 * Customer.java
 * {luohj} - 下午5:49:14
 * 
 */
package org.loxf.registry.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 消费者配置
 * @author {luohj}
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Customer {
	/**
	 * 服务分组（必须和服务端设置一样）
	 * @return
	 * @author:luohj
	 */
	public String group() default "";
	/**
	 * 异步调用：true：是，false：非
	 * @return
	 * @author:luohj
	 */
	public boolean asyn() default false;
	/**
	 * 方法负载均衡
	 * @return
	 * @author:luohj
	 */
	public String pollingType() default "";
	/**
	 * 方法调用超时设置
	 * @return
	 * @author:luohj
	 */
	public int timeout() default 0;
	/**
	 * 优先调用JVM方法
	 * @return
	 * @author:luohj
	 */
	public boolean jvm() default false;
}
