/**
 * TransactionResult.java
 * luohj - 下午10:50:26
 * 
 */
package org.loxf.core.transcation.bean;

import java.io.Serializable;

/**
 * @author luohj
 *
 */
public class TransactionResult implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Transaction tr;
	private Object value;
	
	/**
	 * @return the tr
	 */
	public Transaction getTr() {
		return tr;
	}
	/**
	 * @param tr the tr to set
	 */
	public void setTr(Transaction tr) {
		this.tr = tr;
	}
	/**
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(Object value) {
		this.value = value;
	}
}
