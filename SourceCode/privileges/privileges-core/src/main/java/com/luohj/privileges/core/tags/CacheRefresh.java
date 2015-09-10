/**
 * Title: CacheRefresh.java
 * Copyright: Loxf
 * Company: Loxf Team
 * @author: luohj
 * @version: 1.0
 * @time:  2015-9-10 下午2:26:47
 */
package com.luohj.privileges.core.tags;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author luohj
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheRefresh {
	String key() ;
}
