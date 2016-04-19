/**
 * Refer.java
 * luohj - 下午3:20:56
 * 
 */
package org.loxf.registry.exportAndRefer;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;

import org.loxf.registry.bean.AliveClient;
import org.loxf.registry.bean.Invocation;
import org.loxf.registry.bean.Server;

/**
 * @author luohj
 *
 */
public class Refer {
	/**
	 * TODO:引用服务
	 * 
	 * @param <T>
	 *            接口泛型
	 * @param interfaceClass
	 *            接口类型
	 * @param group
	 *            分组，用于区分一个接口多个实现
	 * @param server
	 *            服务端信息
	 * @param AliveClient
	 *            客户端信息
	 * @param asyn
	 *            是否同步
	 * @return 远程服务
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static <T> T refer(final Class<T> interfaceClass, final String group, final Server server,
			final AliveClient client, final boolean asyn) throws Exception {
		if (interfaceClass == null)
			throw new IllegalArgumentException("Interface class == null");
		if (!interfaceClass.isInterface())
			throw new IllegalArgumentException("The " + interfaceClass.getName() + " must be interface class!");
		System.out.println("Refer service " + interfaceClass.getName() + " success. ");
		return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[] { interfaceClass },
				new InvocationHandler() {
					public Object invoke(Object proxy, Method method, Object[] arguments) throws Throwable {

						if (server.getServerAddr() == null || server.getServerAddr().length() == 0)
							throw new IllegalArgumentException("Host == null!");
						if (server.getServerPort() <= 0 || server.getServerPort() > 65535)
							throw new IllegalArgumentException("Invalid port " + server.getServerPort());
						System.out.println("Get remote service " + interfaceClass.getName() + " from server "
								+ server.getServerAddr() + ":" + server.getServerPort());

						Socket socket = new Socket(server.getServerAddr(), server.getServerPort());
						try {
							ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
							try {
								/**
								 * 协议 in{Invocation invo} out{Object result}
								 */
								Invocation invo = new Invocation();
								invo.setInterfaces(interfaceClass);
								invo.setGroup(group);
								invo.setMethod(method);
								invo.setParams(arguments);
								invo.setAppName(client.getAppName());
								invo.setAsyn(asyn);
								invo.setIp(client.getIp());
								output.writeObject(invo);
								ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
								try {
									Object result = input.readObject();
									if (result instanceof Throwable) {
										throw (Throwable) result;
									}
									return result;
								} finally {
									input.close();
								}
							} finally {
								output.close();
							}
						} finally {
							socket.close();
						}
					}
				});
	}

}
