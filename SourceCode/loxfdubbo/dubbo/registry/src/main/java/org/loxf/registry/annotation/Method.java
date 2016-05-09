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
	public boolean asyn() default false;
	public int timeout() default 0;
	public String pollingType() default "";
}
