/**
 * ClientListener.java
 * luohj - 下午8:43:36
 * 
 */
package org.loxf.registry.listener;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.loxf.registry.main.ClientManager;
import org.loxf.registry.queue.IssuedQueue;

/**
 * 客户端监听
 * 用于获取注册中心推送的服务列表
 * @author luohj
 *
 */
public class ClientListener {
	private ServerSocket server;
	private ClientManager clientMgr ;
	private int maxConnection = 1000 ;
	private int port;
	
	public ClientListener(ClientManager clientMgr, int port) throws IOException{
		this.clientMgr = clientMgr;
		this.port = port;
		openListen();
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
					server = new ServerSocket(port, maxConnection);
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
						try {
							ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
							try {
								ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
								try {
									 IssuedQueue queue = (IssuedQueue)input.readObject();
									 //获取服务列表并更新
									 clientMgr.updateServices(queue);
									 output.writeObject(true);
								} finally {
									output.close();
								}
							} finally {
								input.close();
							}
						} finally {
							socket.close();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
}
