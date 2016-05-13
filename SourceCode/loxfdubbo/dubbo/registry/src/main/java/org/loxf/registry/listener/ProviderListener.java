/**
 * ProviderListener.java
 * luohj - 下午5:30:10
 * 
 */
package org.loxf.registry.listener;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import org.loxf.registry.invocation.Invocation;
import org.loxf.registry.main.ProviderManager;

/**
 * 提供者监听
 * 
 * @author luohj
 *
 */
public class ProviderListener {

	private ServerSocket server;
	private ProviderManager providerMgr;
	private int port;
	private int maxConnection = 1000;

	public ProviderListener(ProviderManager providerMgr, int port) throws IOException {
		this.providerMgr = providerMgr;
		this.port = port;
		openListen();
	}

	public int getPort() {
		return this.port;
	}

	/**
	 * 打开注册端口
	 * 
	 * @param port
	 * @throws IOException
	 */
	public boolean openListen() throws IOException {
		if (port <= 0 || port > 65535)
			throw new IllegalArgumentException("Invalid port " + port);
		if (server == null) {
			synchronized (RpcListener.class) {
				if (server == null) {
					while (true) {
						try {
							server = new ServerSocket(port, maxConnection);
							break;
						} catch (BindException e) {
							port++;
							if (port <= 0 || port > 65535)
								throw new IllegalArgumentException("Invalid port " + port);
						}
					}
				}
			}
		}
		return true;
	}

	public ServerSocket getRpcListen() {
		return server;
	}

	/**
	 * 监听
	 */
	public void start() {
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						final Socket socket = server.accept();// 服务器端一旦收到消息，就创建一个线程进行处理
						Date start = new Date();
						ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
						Invocation invo = (Invocation) input.readObject();
						ExecThread exec = new ExecThread(providerMgr, socket, invo, input);
						exec.start();
						new DealTimeOutThread(exec, start, invo.getTimeout()).start();
					} catch (IOException | ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
}
