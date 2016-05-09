/**
 * BaseBean.java
 * lenovo - 下午4:51:08
 * 
 */
package org.loxf.registry.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * @author luohj
 *
 */
public class BaseBean implements Serializable{
	private static final long serialVersionUID = 1L;
	/**
	 * 当前信息是否已经更新
	 * true:需要更新，false:不需要
	 */
	private boolean isUpdate;
	/**
	 * 当前信息是否已经改变
	 * 配合isUpdate使用，当前实体满足 isUpdate&&isChanged == true时更新
	 * true:改变，false:未改变
	 */
	private boolean isChanged;
	/**
	 * 最后一次更新时间
	 */
	private Date lastModifyDate;
	
	public BaseBean(){
		
	}

	public BaseBean(boolean flag){
		isUpdate = flag;
		isChanged = flag;
		lastModifyDate = new Date();
	}
	/**
	 * @return the isUpdate
	 */
	public boolean isUpdate() {
		return isUpdate;
	}

	/**
	 * @param isUpdate the isUpdate to set
	 */
	public void setUpdate(boolean isUpdate) {
		this.isUpdate = isUpdate;
	}

	/**
	 * @return the isChanged
	 */
	public boolean isChanged() {
		return isChanged;
	}

	/**
	 * @param isChanged the isChanged to set
	 */
	public void setChanged(boolean isChanged) {
		this.isChanged = isChanged;
	}

	/**
	 * @return the lastModifyDate
	 */
	public Date getLastModifyDate() {
		return lastModifyDate;
	}

	/**
	 * @param lastModifyDate the lastModifyDate to set
	 */
	public void setLastModifyDate(Date lastModifyDate) {
		this.lastModifyDate = lastModifyDate;
	}
}
