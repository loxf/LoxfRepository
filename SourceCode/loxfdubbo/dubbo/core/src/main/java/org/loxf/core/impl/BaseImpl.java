/**
 * BaseImpl.java
 * luohj - 下午7:53:26
 * 
 */
package org.loxf.core.impl;

import org.loxf.core.interfaces.IBase;
import org.loxf.core.transcation.bean.Transaction;

/**
 * @author luohj
 *
 */
public class BaseImpl extends Impl implements IBase {
	private Transaction tr;
	/** (non-Javadoc)
	 * @see org.loxf.core.interfaces.IBase#getTransaction()
	 */
	@Override
	public Transaction getTransaction() {
		return tr;
	}

	/** (non-Javadoc)
	 * @see org.loxf.core.interfaces.IBase#setTransaction()
	 */
	@Override
	public void setTransaction(Transaction t) {
		tr = t;
	}

}
