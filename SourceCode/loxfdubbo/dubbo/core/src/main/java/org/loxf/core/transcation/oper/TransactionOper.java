/**
 * TransactionOper.java
 * luohj - 下午9:05:00
 * 
 */
package org.loxf.core.transcation.oper;

import org.loxf.core.transcation.bean.Transaction;

/**
 * @author luohj
 *
 */
public class TransactionOper {
	public Transaction getTransaction(){
		return null;
		
	}
	public void setTranscation(Transaction t){
		
	}
	public boolean openTransaction(Transaction t){
		t.setWriteable(true);
		return true;
		
	}
	public boolean preCommit(Transaction t){
		return false;
		
	}
	public boolean commit(Transaction t){
		return false;
		
	}
	public boolean rollBack(Transaction t){
		return false;
	}
	
	public void setStart(Transaction t, String start){
		t.setStart(start);
	}
	
	public void setEnd(Transaction t, String end){
		t.setEnd(end);
	}
	
	public String getStart(Transaction t){
		return t.getStart();
	}

	public String getEnd(Transaction t){
		return t.getEnd();
	}
}
