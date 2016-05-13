/**
 * TransactionOper.java
 * luohj - 下午9:05:00
 * 
 */
package org.loxf.core.transcation.oper;

import java.lang.annotation.Annotation;

import org.loxf.core.interfaces.IBaseService;
import org.loxf.core.transcation.annotation.Transactional;
import org.loxf.core.transcation.bean.Transaction;
import org.loxf.core.transcation.config.Propagation;
import org.loxf.core.transcation.exception.TransactionException;

/**
 * @author luohj
 *
 */
public class TransactionOper {
	public Transaction getTransaction() {
		return null;

	}

	public void setTranscation(Transaction t) {

	}

	public boolean openTransaction(Transaction t) {
		t.setWriteable(true);
		return true;

	}

	public boolean preCommit(Transaction t) {
		return false;

	}

	public boolean commit(Transaction t) {
		return false;

	}

	public boolean rollBack(Transaction t) {
		return false;
	}

	public void setStart(Transaction t, String start) {
		t.setStart(start);
	}

	public void setEnd(Transaction t, String end) {
		t.setEnd(end);
	}

	public String getStart(Transaction t) {
		return t.getStart();
	}

	public String getEnd(Transaction t) {
		return t.getEnd();
	}

	public void set(IBaseService o, Transaction tr) {
		o.setTransaction(tr);
	}

	public void deal(IBaseService o, Transaction tr, String curr) {
		Annotation[] classAnos = o.getClass().getAnnotations();
		if (classAnos != null) {
			for (Annotation ano : classAnos) {
				if (ano.annotationType().equals(Transactional.class)) {
					Transactional transInfo = o.getClass().getAnnotation(Transactional.class);
					if (transInfo != null) {
						if (transInfo.type().equals(Propagation.PROPAGATION_SUPPORTS)) {
							o.setTransaction(tr);
							if(tr!=null){
								openTransaction(o.getTransaction());
							}
						} else if (transInfo.type().equals(Propagation.PROPAGATION_MANDATORY)) {
							if (tr == null) {
								throw new TransactionException(
										"当前没有事务，事务管理器异常！事务传播属性：[" + transInfo.type().value + "].");
							}
							openTransaction(o.getTransaction());
						} else if (transInfo.type().equals(Propagation.PROPAGATION_REQUIRES_NEW)) {
							o.setTransaction(new Transaction(curr));
							openTransaction(o.getTransaction());
						} else if (transInfo.type().equals(Propagation.PROPAGATION_NOT_SUPPORTED)) {
							o.setTransaction(null);
						} else if (transInfo.type().equals(Propagation.PROPAGATION_NEVER)) {
							if(tr!=null){
								throw new TransactionException(
										"以非事务方式执行，当前事务必须为空！事务传播属性：[" + transInfo.type().value + "].");
							}
						} else {
							// 默认 PROPAGATION_REQUIRED：支持当前事务，如果当前没有事务，就新建一个事务
							if (tr == null) {
								tr = new Transaction(curr);
							}
							o.setTransaction(tr);
							openTransaction(o.getTransaction());
						}
						o.setPropagation(transInfo.type());
					}
				}
			}
		}
	}
}
