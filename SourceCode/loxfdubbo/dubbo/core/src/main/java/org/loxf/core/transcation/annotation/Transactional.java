/**
 * Transactional.java
 * luohj - 下午2:52:48
 * 
 */
package org.loxf.core.transcation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.loxf.core.transcation.config.Propagation;

/**
 * 事务注解
 * @author luohj
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Transactional {
	/**
	 * 事务传播属性
	 * @return
	 * @author:luohj
	 * @see org.loxf.core.transcation.config.Propagation
	 */
	public Propagation type() default Propagation.PROPAGATION_REQUIRED;
}
