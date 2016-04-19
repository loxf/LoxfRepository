/**
 * PushServicesThread.java
 * luohj - 下午8:12:19
 * 
 */
package org.loxf.registry.thread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

import org.loxf.registry.bean.AliveClient;
import org.loxf.registry.queue.IssuedQueue;

/**
 * 发布服务给客户端（每五秒钟）
 * @author luohj
 *
 */
public class PushServicesThread {
	private IssuedQueue queue;
	private Map<String, AliveClient> aliveClients;
	
	public PushServicesThread(IssuedQueue queue, Map<String, AliveClient> aliveClients){
		this.queue = queue;
		this.aliveClients = aliveClients;
	}
	public void start() {
		new Thread(new Runnable() {
			public void run() {
				while(true){
					if(!queue.isEmpty()){
						synchronized(queue){
							List<AliveClient> listClient = (List<AliveClient>)aliveClients.values();
							for(AliveClient client : listClient){
								if("CUST".equalsIgnoreCase(client.getType())){
									Socket socket = null;
									try {
										socket = new Socket(client.getIp(), Integer.valueOf(client.getPort()));
										ObjectOutputStream out = new ObjectOutputStream (socket.getOutputStream());
										// 推送queue到客户端
										out.writeObject(queue);
										ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
										try {
											Object result = in.readObject();
											if(result instanceof Throwable){
												throw (Throwable)result; 
											}
											System.out.println(result);
										} catch (ClassNotFoundException e) {
											e.printStackTrace();
										} catch (Throwable e) {
											e.printStackTrace();
										} finally {
											in.close();
											out.close();
										}
									} catch (NumberFormatException e) {
										e.printStackTrace();
									} catch (UnknownHostException e) {
										e.printStackTrace();
									} catch (IOException e) {
										e.printStackTrace();
									} finally {
										try {
											if(socket !=null)
												socket.close();
										} catch (IOException e) {
											e.printStackTrace();
										}
									}
								}
							}
							queue.clear();
						}
					}
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
}
