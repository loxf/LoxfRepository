/**
 * ReferProxy.java
 * luohj - 下午8:04:51
 * 
 */
package org.loxf.core.transcation.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.loxf.core.interfaces.IBase;
import org.loxf.core.transcation.bean.Transaction;
import org.loxf.core.transcation.oper.TransactionOper;

/**
 * @author luohj
 *
 */
public class TransactionProxy implements InvocationHandler {
	/**
	 * 代理工具
	 */
	private TransactionOper proxy;
	/**
	 * 代理对象
	 */
	private Class<?> delegate;

	@SuppressWarnings("unchecked")
	public <T> T bind(Class<T> delegate, TransactionOper proxy) {
		this.delegate = delegate;
		this.proxy = proxy;
		return (T) Proxy.newProxyInstance(this.delegate.getClassLoader(), new Class[] { delegate }, this);

	}

	/**
	 * 执行方法
	 * 
	 * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object,
	 *      java.lang.reflect.Method, java.lang.Object[])
	 */
	@Override
	public Object invoke(Object object, Method method, Object[] arguments) throws Throwable {
		Transaction tr = ((IBase)object).getTransaction();
		if(tr==null){
			tr = new Transaction();
		}
		if(proxy.openTransaction(tr)){
			
		}
		return arguments;
	}

	
}
