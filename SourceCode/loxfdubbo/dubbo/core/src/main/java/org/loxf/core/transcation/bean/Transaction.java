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

import javax.sql.DataSource;

/**
 * @author luohj
 *
 */
public class Transaction implements Serializable {
	private static final long serialVersionUID = 1L;
	private String start;
	private String end;
	
	private boolean writeable;
	private LinkedList<Connection> conns ;
	private HashMap<DataSource, Connection> map;

	/**
	 * @return the start
	 */
	public String getStart() {
		return start;
	}
	/**
	 * @param start the start to set
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
	 * @param end the end to set
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
	 * @param writeable the writeable to set
	 */
	public void setWriteable(boolean writeable) {
		this.writeable = writeable;
	}
	public Transaction(){
		conns = new LinkedList<Connection>();
		map = new HashMap<DataSource, Connection>();
	}
	/**
	 * @return the conns
	 */
	public LinkedList<Connection> getConns() {
		return conns;
	}
	/**
	 * @param conns the conns to set
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
	 * @param map the map to set
	 */
	public void setMap(HashMap<DataSource, Connection> map) {
		this.map = map;
	}
}
