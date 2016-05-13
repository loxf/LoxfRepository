/**
 * ExecThread.java
 * luohj - 上午11:52:04
 * 
 */
package org.loxf.registry.listener;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

import org.loxf.registry.invocation.Invocation;
import org.loxf.registry.main.ProviderManager;

/**
 * @author luohj
 *
 */
public class ExecThread extends Thread {
	private ProviderManager providerMgr;
	private Socket socket;
	private Invocation invo;
	private ObjectInputStream input;
	private ObjectOutputStream output;

	public ExecThread(ProviderManager providerMgr, final Socket socket, Invocation invo, ObjectInputStream input) {
		this.providerMgr = providerMgr;
		this.socket = socket;
		this.invo = invo;
		this.input = input;
		try {
			this.output = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void timeoutException() throws IOException {
		output.writeObject(new RuntimeException("执行超时[" + invo.getTimeout() + " ms]:" + invo.toString()));
		output.close();
		input.close();
		socket.close();
	}

	public void run() {
		try {
			try {
				// 获取服务列表并更新
				Object result = providerMgr.call(invo);
				output.writeObject(result);
			} catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | InstantiationException e) {
				e.printStackTrace();
			} finally {
				output.close();
				input.close();
				socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
