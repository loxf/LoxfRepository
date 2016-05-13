/**
 * BaseImpl.java
 * luohj - 下午7:53:26
 * 
 */
package org.loxf.core.service;

import org.loxf.core.impl.Impl;
import org.loxf.core.interfaces.IBaseTransactionService;
import org.loxf.core.transcation.bean.Transaction;
import org.loxf.core.transcation.config.Propagation;

/**
 * @author luohj
 *
 */
public class BaseTransactionService extends Impl implements IBaseTransactionService {
	private Transaction tr;
	private Propagation propagation;

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.loxf.core.interfaces.IBaseTransactionService#getTransaction()
	 */
	@Override
	public Transaction getTransaction() {
		return tr;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.loxf.core.interfaces.IBaseTransactionService#setTransaction()
	 */
	@Override
	public void setTransaction(Transaction t) {
		tr = t;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.loxf.core.interfaces.IBaseTransactionService#getPropagation()
	 */
	@Override
	public Propagation getPropagation() {
		return propagation;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.loxf.core.interfaces.IBaseTransactionService#setPropagation(org.loxf.core.transcation.config.Propagation)
	 */
	@Override
	public void setPropagation(Propagation propagation) {
		this.propagation = propagation;
	}

}
