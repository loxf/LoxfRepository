/**
 * 生产者配置
 * Provider.java
 * {luohj} - 下午5:49:05
 * 
 */
package org.loxf.registry.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 提供者
 * @author luohj
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Provider {
	/**
	 * 接口
	 * @return
	 * @author:luohj
	 */
	public Class<?> interfaces();
	/**
	 * 服务分组（接口+服务分组必须唯一）
	 * @return
	 * @author:luohj
	 */
	public String group() default "";
	/**
	 * 服务状态
	 * @return
	 * @author:luohj
	 */
	public Status status() default Status.EFF;
	/**
	 * 超时设置（方法>服务>客户端总配置）
	 * @return
	 * @author:luohj
	 */
	public int timeout() default 0;
	/**
	 * 负载均衡（方法>服务>客户端总配置）
	 * @return
	 * @author:luohj
	 */
	public String pollingType() default "";
	/**
	 * 服务异步调用（方法>服务）
	 * @return
	 * @author:luohj
	 */
	public boolean asyn() default false;
	
	public enum Status {
		EFF("EFF"), EXP("EXP");
		private String value;
		Status(String value){
			this.value = value;
		}
		public String getValue() {
			return value;
		}
	}
}
