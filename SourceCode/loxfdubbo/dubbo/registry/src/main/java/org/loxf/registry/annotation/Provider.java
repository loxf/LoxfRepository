/**
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
	public Class<?> interfaces();
	public String group() default "";
	public Status status() default Status.EFF;
	public int timeout() default 0;
	public String pollingType() default "";
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
