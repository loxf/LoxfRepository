/**
 * TransactionException.java
 * luohj - 下午3:49:26
 * 
 */
package org.loxf.core.transcation.exception;

/**
 * 事务异常
 * 
 * @author luohj
 *
 */
public class TransactionException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TransactionException() {
		super();
	}

	public TransactionException(String message) {
		super(message);
	}

	public TransactionException(String message, Throwable paramThrowable) {
		super(message, paramThrowable);
	}

	public TransactionException(Throwable paramThrowable) {
		super(paramThrowable);
	}

}
