/**
 * ClientLiftMgrThread.java
 * luohj - 下午5:26:02
 * 
 */
package org.loxf.registry.thread;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import org.loxf.registry.bean.AliveClient;
import org.loxf.registry.bean.Server;
import org.loxf.registry.main.IRegistryCenterManager;
import org.loxf.registry.utils.MapUtils;

/**
 * @author luohj
 *
 */
public class ClientLifeMgrThread {
	IRegistryCenterManager serverManager;
	HashMap<String, AliveClient> aliveClients;
	Thread t = null;

	public ClientLifeMgrThread(IRegistryCenterManager serverManager, HashMap<String, AliveClient> aliveClients) {
		this.serverManager = serverManager;
		this.aliveClients = aliveClients;
	}
	
	public void stop(){
		if(t!=null && t.isInterrupted()){
			t.interrupt();
		}
	}

	public void start() {
		t = new Thread(new Runnable() {
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				while(true){
					HashMap<String, AliveClient> tmpClients = (HashMap<String, AliveClient>) MapUtils.clone(aliveClients);
					Iterator<String> it = tmpClients.keySet().iterator();
					HashMap<String, String> needDel = new HashMap<String, String>();
					while (it.hasNext()) {
						String key = it.next();
						AliveClient client = aliveClients.get(key);
						if (new Date().getTime() - client.getLastModifyDate().getTime() > client.getTimeout()) {
							if(client.getType().equals("SERV")){
								Server server = new Server();
								server.setServerAddr(client.getIp());
								server.setServerPort(client.getPort());
								serverManager.stopServer(server, false);
							}
							needDel.put(key, key);
						}
					}
					synchronized(aliveClients){
						if(needDel.size()>0){
							for(String key : needDel.keySet()){
								aliveClients.remove(key); 
							}
						}
					}
						
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

		});
		t.start();
	}
}
