/**
 * DealTimeOutThread.java
 * luohj - 上午11:34:08
 * 
 */
package org.loxf.registry.listener;

import java.io.IOException;
import java.util.Date;

/**
 * @author luohj
 *
 */
public class DealTimeOutThread extends Thread {
	ExecThread t;
	Date start;
	long timeout ;
	public DealTimeOutThread(ExecThread t, Date start, long timeout){
		this.t = t;
		this.start = start;
		this.timeout = timeout;
	}
	
	public void run(){
		while(t.isAlive()){
			Date curr = new Date();
			if(curr.getTime()-start.getTime()>timeout){
				if(t!=null && !t.isInterrupted()){
					try{
						t.timeoutException();
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						t.interrupt();
					}
					
				}
			}
		}
	}
}
