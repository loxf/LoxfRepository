package org.loxf.core.service;

import org.loxf.core.interfaces.IBase;
import org.loxf.core.transcation.bean.Transaction;

/**
 * BaseImpl.java
 * luohj - 下午4:53:26
 * 
 */

/**
 * @author luohj
 *
 */
public class BaseImpl implements IBase {
	private Transaction tr;

	/**
	 * @return the tr
	 */
	public Transaction getTransaction() {
		return tr;
	}

	/**
	 * @param tr the tr to set
	 */
	public void setTransaction(Transaction tr) {
		this.tr = tr;
	}
}
