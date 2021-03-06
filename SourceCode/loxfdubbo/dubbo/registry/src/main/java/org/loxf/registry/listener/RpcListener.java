package org.loxf.registry.listener;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.loxf.registry.bean.AliveClient;
import org.loxf.registry.bean.Service;
import org.loxf.registry.main.IRegistryCenterManager;


/**
 * RPC监听<br>
 * 用于服务注册和心跳管理
 * @author lenovo
 *
 */
public class RpcListener {
	private ServerSocket server;
	private IRegistryCenterManager serverManager;
	private int port;
	private int maxConnection = 1000 ;
	Thread t = null;

	public RpcListener(IRegistryCenterManager serverManager) throws IOException {
		this.serverManager = serverManager;
		this.port = serverManager.getPort();
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
			server = new ServerSocket(port, maxConnection);
		}
		return true;
	}

	public ServerSocket getRpcListen() {
		return server;
	}
	
	public void stop(){
		if(t!=null && t.isInterrupted()){
			t.interrupt();
		}
		try {
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 启动监听
	 */
	public void start() {
		t = new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						final Socket socket = server.accept();// 服务器端一旦收到消息，就创建一个线程进行处理
						try {
							ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
							try {
								ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
								try {
									int type = input.readInt();// 0：心跳服务 1：注册服务 2：客户端第一次请求，返回当前全量服务列表给客户端
									switch (type) {
									case 0:
										// 0：心跳服务
										try {
											AliveClient client = (AliveClient) input.readObject();// 获取调用
											serverManager.addAliveClient(client);
											output.writeObject(true);// 将结果发送
										} catch (Throwable t) {
											output.writeObject(t);
										}
										break;
									case 1:
										// 1：注册服务
										try {
											Service[] services = (Service[])input.readObject();//获取注册服务
											serverManager.register(services);
											output.writeObject(true);
										} catch (Throwable t) {
											output.writeObject(t);
										}
										break;
									case 2:
										// 2：客户端第一次请求，返回当前全量服务列表给客户端
										try {
											output.writeObject(serverManager.getServices());
										} catch (Throwable t) {
											output.writeObject(t);
										}
										break;
									default:
										throw new RuntimeException("参数设置错误!");
									}
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
		});
		t.start();
	}
	
}
