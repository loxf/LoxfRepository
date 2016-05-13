/**
 * 事务实体
 * Transaction.java
 * luohj - 下午7:55:26
 * 
 */
package org.loxf.core.transcation.bean;

import java.io.Serializable;
import java.sql.Connection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import javax.sql.DataSource;

/**
 * @author luohj
 *
 */
public class Transaction implements Serializable {
	private static final long serialVersionUID = 1L;
	private String serialNo;
	private String start;
	private String end;

	private boolean writeable;
	private LinkedList<Connection> conns;
	private HashMap<DataSource, Connection> map;

	public Transaction(String curr){
		init(curr);
	}

	public Transaction(){
		init(null);
	}
	
	protected void init(String curr){
		this.start = curr;
		this.end = curr;
		this.serialNo = randomString(15);
		this.conns = new LinkedList<Connection>();
		this.map = new HashMap<DataSource, Connection>();
	}

	public boolean equals(Transaction o) {
		if (this.serialNo.equals(o.serialNo))
			return true;
		return false;
	}

	public static final String randomString(int length) {
		Random randGen = new Random();
		if (length < 1) {
			return null;
		}
		char[] numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz" + "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ")
				.toCharArray();
		char[] randBuffer = new char[length];
		for (int i = 0; i < randBuffer.length; i++) {
			randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];
		}
		return new String(randBuffer);
	}

	/**
	 * @return the serial
	 */
	public String getSerial() {
		return serialNo;
	}

	/**
	 * @return the start
	 */
	public String getStart() {
		return start;
	}

	/**
	 * @param start
	 *            the start to set
	 */
	public void setStart(String start) {
		this.start = start;
	}

	/**
	 * @return the end
	 */
	public String getEnd() {
		return end;
	}

	/**
	 * @param end
	 *            the end to set
	 */
	public void setEnd(String end) {
		this.end = end;
	}

	/**
	 * @return the writeable
	 */
	public boolean isWriteable() {
		return writeable;
	}

	/**
	 * @param writeable
	 *            the writeable to set
	 */
	public void setWriteable(boolean writeable) {
		this.writeable = writeable;
	}

	/**
	 * @return the conns
	 */
	public LinkedList<Connection> getConns() {
		return conns;
	}

	/**
	 * @param conns
	 *            the conns to set
	 */
	public void setConns(LinkedList<Connection> conns) {
		this.conns = conns;
	}

	/**
	 * @return the map
	 */
	public HashMap<DataSource, Connection> getMap() {
		return map;
	}

	/**
	 * @param map
	 *            the map to set
	 */
	public void setMap(HashMap<DataSource, Connection> map) {
		this.map = map;
	}
}
