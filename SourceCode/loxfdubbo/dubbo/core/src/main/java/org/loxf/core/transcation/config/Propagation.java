/**
 * 事务传播属性
 * Propagation.java
 * luohj - 下午2:38:18
 * 
 */
package org.loxf.core.transcation.config;

/**
 * 事务传播属性：<br>
 * PROPAGATION_REQUIRED：支持当前事务，如果当前没有事务，就新建一个事务。默认选择。<br>
 * PROPAGATION_SUPPORTS：支持当前事务，如果当前没有事务，就以非事务方式执行。 <br>
 * PROPAGATION_MANDATORY：支持当前事务，如果当前没有事务，就抛出异常。 <br>
 * PROPAGATION_REQUIRES_NEW：新建事务，如果当前存在事务，把当前事务挂起。 <br>
 * PROPAGATION_NOT_SUPPORTED：以非事务方式执行操作，如果当前存在事务，就把当前事务挂起。 <br>
 * PROPAGATION_NEVER：以非事务方式执行，如果当前存在事务，则抛出异常。 <br>
 * @author luohj
 *
 */
public enum Propagation {
	
	/**
	 * 支持当前事务，如果当前没有事务，就新建一个事务。默认选择。
	 */
	PROPAGATION_REQUIRED("PROPAGATION_REQUIRED"), 
	/**
	 * 支持当前事务，如果当前没有事务，就以非事务方式执行。
	 */
	PROPAGATION_SUPPORTS("PROPAGATION_SUPPORTS"), 
	/**
	 * 支持当前事务，如果当前没有事务，就抛出异常。
	 */
	PROPAGATION_MANDATORY("PROPAGATION_MANDATORY"), 
	/**
	 * 新建事务，如果当前存在事务，把当前事务挂起。
	 */
	PROPAGATION_REQUIRES_NEW("PROPAGATION_REQUIRES_NEW"), 
	/**
	 * 以非事务方式执行操作，如果当前存在事务，就把当前事务挂起。
	 */
	PROPAGATION_NOT_SUPPORTED("PROPAGATION_NOT_SUPPORTED"), 
	/**
	 * 以非事务方式执行，如果当前存在事务，则抛出异常。
	 */
	PROPAGATION_NEVER("PROPAGATION_NEVER"); 
	
	Propagation(String value) {
		this.value = value;
	}

	public String value;
	
	public boolean equals(Propagation p){
		if(this.value.equals(p.value)){
			return true;
		} 
		return false;
	}
}
