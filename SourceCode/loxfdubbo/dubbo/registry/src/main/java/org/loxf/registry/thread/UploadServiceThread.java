/**
 * UploadServiceThread.java
 * luohj - 下午3:35:44
 * 
 */
package org.loxf.registry.thread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.loxf.registry.bean.RegistryCenter;
import org.loxf.registry.bean.Service;
import org.loxf.registry.queue.UploadServiceQueue;

/**
 * 上发服务的线程
 * @author luohj
 *
 */
public class UploadServiceThread {
	/**
	 * 心跳时间，单位毫秒，默认30s
	 */
	private int sleepTime = 30000;
	/**
	 * 上发队列
	 */
	private UploadServiceQueue queue ;
	/**
	 * 注册中心
	 */
	private RegistryCenter registryCenter ;
	
	/**
	 * @param sleepTime
	 * @param queue
	 * @param registryCenter
	 */
	public UploadServiceThread(int sleepTime, UploadServiceQueue queue,RegistryCenter registryCenter){
		this.sleepTime = sleepTime;
		this.queue = queue;
		this.registryCenter = registryCenter;
	}
	
	public void start(){
		new Thread(new Runnable(){
			public void run(){
				while(true){
					if(!queue.isEmpty()){
						synchronized(queue){
							try {
								Service[] services = (Service[])queue.toArray();
								Socket socket = null;
								try {
									socket = new Socket(registryCenter.getIp(), registryCenter.getPort());
									ObjectOutputStream out = new ObjectOutputStream (socket.getOutputStream());
									// 向注册中心请求全量服务列表
									/**
									 * 协议：<br>
									 * in {object:LIST/SINGLE, Object Service[] services/Service service}<br>
									 * out {(boolean)Object }<br>
									 */
									out.writeObject("LIST");
									out.writeObject(services);
									ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
									try {
										Object result = in.readObject();
										if(result instanceof Throwable){
											throw (Throwable)result; 
										}
									} catch (ClassNotFoundException e) {
										e.printStackTrace();
									} catch (Throwable e) {
										e.printStackTrace();
									} finally {
										in.close();
										out.close();
									}
								} catch (UnknownHostException e) {
									e.printStackTrace();
								} catch (IOException e) {
									e.printStackTrace();
								} finally{
									try {
										if(socket !=null)
											socket.close();
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
								queue.clear();
								Thread.sleep(sleepTime);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					} else {
						try {
							Thread.sleep(sleepTime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}).start();
	}
	
	/**
	 * @return the sleepTime
	 */
	public int getSleepTime() {
		return sleepTime;
	}
	/**
	 * @param sleepTime the sleepTime to set
	 */
	public void setSleepTime(int sleepTime) {
		this.sleepTime = sleepTime;
	}
}
