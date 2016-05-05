/**
 * ServerHeartBeatThread.java
 * lenovo - 下午3:35:44
 * 
 */
package org.loxf.registry.thread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.loxf.registry.bean.AliveClient;
import org.loxf.registry.bean.RegistryCenter;

/**
 * @author lenovo
 *
 */
public class ServerHeartBeatThread {
	/**
	 * 心跳时间，单位毫秒，默认30s
	 */
	private int sleepTime = 30000;
	/**
	 * 注册中心
	 */
	private RegistryCenter registryCenter ;
	/**
	 * 客户端信息
	 */
	private AliveClient client ;
	
	/**
	 * @param sleepTime
	 * @param registryCenter
	 * @param client
	 */
	public ServerHeartBeatThread(int sleepTime, RegistryCenter registryCenter, AliveClient client){
		this.sleepTime = sleepTime;
		this.registryCenter = registryCenter;
		this.client = client;
	}
	
	public void start(){
		new Thread(new Runnable(){
			public void run(){
				while(true){
					try {
						Socket socket = null;
						try {
							socket = new Socket(registryCenter.getIp(), registryCenter.getPort());
							if(registryCenter.getStatus().equals("HOLD")){
								registryCenter.setStatus("EFF");
							}
							ObjectOutputStream out = new ObjectOutputStream (socket.getOutputStream());
							// 向注册中心请求全量服务列表
							/**
							 * 协议：<br>
							 * in {int:0, AliveClient client}<br>
							 * out {(boolean)Object }<br>
							 */
							out.writeInt(0);
							out.writeObject(client);
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
							registryCenter.setStatus("EXP");
						} catch (IOException e) {
							e.printStackTrace();
						} finally{
							if(socket !=null)
								socket.close();
						}
						Thread.sleep(sleepTime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
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
