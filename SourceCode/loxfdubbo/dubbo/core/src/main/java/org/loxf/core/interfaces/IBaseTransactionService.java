/**
 * IBase.java
 * luohj - 下午4:59:17
 * 
 */
package org.loxf.core.interfaces;

import org.loxf.core.transcation.bean.Transaction;
import org.loxf.core.transcation.config.Propagation;

/**
 * @author luohj
 *
 */
public interface IBaseTransactionService extends I {
	public Propagation getPropagation();
	public void setPropagation(Propagation propagation);
	public Transaction getTransaction();
	public void setTransaction(Transaction t);
}
