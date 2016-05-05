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
 * @author {luohj}
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Customer {
	public Class<?> interfaces();
	public String group() default "";
	public boolean asyn() default false;
	public String pollingType() default "";
	public int timeout() default 0;
}
