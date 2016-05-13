/**
 * IBase.java
 * luohj - 下午4:59:17
 * 
 */
package org.loxf.core.interfaces;

import org.loxf.core.transcation.bean.Transaction;

/**
 * @author luohj
 *
 */
public interface IBase extends I {
	public Transaction getTransaction();
	public void setTransaction(Transaction t);
}
