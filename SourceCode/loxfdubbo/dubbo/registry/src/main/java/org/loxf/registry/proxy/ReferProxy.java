/**
 * ReferProxy.java
 * luohj - 下午8:04:51
 * 
 */
package org.loxf.registry.proxy;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.loxf.core.context.ApplicationContext;
import org.loxf.core.interfaces.IBaseService;
import org.loxf.core.transcation.bean.Transaction;
import org.loxf.core.transcation.bean.TransactionResult;
import org.loxf.core.transcation.oper.TransactionOper;
import org.loxf.core.utils.MapCastList;
import org.loxf.registry.annotation.Customer;
import org.loxf.registry.bean.Server;
import org.loxf.registry.bean.Service;
import org.loxf.registry.invocation.Invocation;
import org.loxf.registry.main.IClientManager;
import org.loxf.registry.utils.LoadBalanceUtil;
import org.loxf.registry.utils.MonitorUtil;

/**
 * @author luohj
 *
 */
public class ReferProxy implements InvocationHandler {
	/**
	 * 监控管理
	 */
	private MonitorUtil proxyMonitor;
	/**
	 * 代理对象
	 */
	private Class<?> delegate;
	/**
	 * 事务
	 */
	private Transaction tr;
	/**
	 * 客户端管理
	 */
	private IClientManager mgr;
	/**
	 * 消费者配置信息
	 */
	private Customer conf;
	/**
	 * 是否新的事务
	 */
	private boolean isNewTr;

	@SuppressWarnings("unchecked")
	public <T> T bind(Class<T> delegate, MonitorUtil proxyMonitor, IClientManager mgr, Customer conf, Transaction tr, boolean isNewTr) {
		this.delegate = delegate;
		this.proxyMonitor = proxyMonitor;
		this.mgr = mgr;
		this.conf = conf;
		this.tr = tr;
		this.isNewTr = isNewTr;
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
		String key = delegate.getName() + (StringUtils.isEmpty(conf.group()) ? "" : ":" + conf.group());
		Service service = mgr.getService(key);
		if (service == null) {
			throw new RuntimeException("未找到当前service的定义！" + key);
		}
		TransactionOper proxyTrans = new TransactionOper();
		String curr = key + "-" + method.getName() + "(";
		int i = 0;
		for(Class<?> cl : method.getParameterTypes()){
			if(i++==0){
				curr += cl.getName();
			} else {
				curr += ", " + cl.getName() ;
			}
		}
		curr += ")";
		if(isNewTr){
			proxyTrans.setStart(tr, curr); 
			proxyTrans.setEnd(tr, curr); 
		}
		proxyMonitor.beforeRefer();
		if (conf.jvm()) {
			if (ApplicationContext.getInstance().isExistsBean(key + ":jvm")) {
				IBaseService o = (IBaseService) Class.forName(service.getImplClazz()).newInstance();
				o.setTransaction(tr);
				proxyTrans.openTransaction(tr);
				o.init();
				Object result = method.invoke(o, arguments);
				if (result instanceof Throwable) {
					throw (Throwable) result;
				}
				proxyMonitor.afterRefer();
				tr = o.getTransaction();
				if(curr.equals(proxyTrans.getEnd(tr))){
					proxyTrans.commit(tr);
				}
				return result;
			}
		}
		// 负载均衡算法，方法级>服务级>客户端总定义
		String polling = StringUtils.isEmpty(conf.pollingType()) ? service.getPollingType() : conf.pollingType();
		if (StringUtils.isEmpty(polling)) {
			polling = mgr.getPollingType();
		}
		// 获取所有服务端
		List<Server> servers = getServerListByServiceKey(key, service);
		while (!servers.isEmpty()) {
			Server server = (Server) LoadBalanceUtil.getInfoByLoadBalancing(servers, polling);
			if (server.getServerAddr() == null || server.getServerAddr().length() == 0)
				throw new IllegalArgumentException("Host == null!");
			if (server.getServerPort() <= 0 || server.getServerPort() > 65535)
				throw new IllegalArgumentException("Invalid port " + server.getServerPort());
			System.out.println("Get remote service " + delegate.getName() + " from server " + server.getServerAddr()
					+ ":" + server.getServerPort());
			Socket socket = null;
			try {
				socket = new Socket(server.getServerAddr(), server.getServerPort());
				ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
				try {
					/**
					 * 协议 in{Invocation invo} out{Object result}
					 */
					Invocation invo = new Invocation();
					invo.setInterfaces(delegate);
					invo.setGroup(conf.group());
					org.loxf.registry.invocation.Method m = new org.loxf.registry.invocation.Method();
					m.setName(method.getName());
					m.setParameterTypes(method.getParameterTypes());
					invo.setMethod(m);
					invo.setParams(arguments);
					invo.setAppName(mgr.getClient().getAppName());
					invo.setAsyn(conf.asyn());
					invo.setIp(mgr.getClient().getIp());
					invo.setPort(mgr.getClient().getPort());
					invo.setTransaction(tr);
					int timeout = conf.timeout();
					if (timeout <= 0) {
						timeout = service.getTimeout() > 0 ? service.getTimeout() : mgr.getClient().getTimeout();
					}
					invo.setTimeout(timeout);
					
					output.writeObject(invo);
					ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
					try {
						Object result = input.readObject();
						Object value = null;
						// TODO 处理异步的情况 else if instanceof Feature
						if (result instanceof Throwable) {
							throw (Throwable) result;
						} else if(result instanceof TransactionResult){
							value = ((TransactionResult) result).getValue();
							Transaction transaction = ((TransactionResult) result).getTr();
							if(transaction.getEnd().equals(curr)){
								proxyTrans.commit(tr);
							}
						} else {
							value = result;
						}
						proxyMonitor.beforeRefer();
						return value;
					} finally {
						input.close();
					}
				} finally {
					output.close();
				}
			} catch (UnknownHostException e) {
				servers.remove(server);
				e.printStackTrace();
			} catch (ConnectException e) {
				servers.remove(server);
				e.printStackTrace();
			} finally {
				if (socket != null)
					socket.close();
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Server> getServerListByServiceKey(String key, Service service) {
		HashMap<String, Server> servers = service.getServers();
		return (List<Server>) MapCastList.convert(servers);
	}

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
	
}
