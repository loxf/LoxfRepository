/**
 * Title: Menu.java
 * Copyright: Loxf
 * Company: loxf Team
 * @author: ouxin
 * @version: 1.0
 * @time:  2015-9-10 下午04:47:03
 */
package com.luohj.privileges.model;

import com.luohj.privileges.core.model.BaseBean;
import com.luohj.privileges.core.tags.Column;
import com.luohj.privileges.core.tags.Table;

/**
 * @author ouxin
 *
 */
@Table("TB_MENU")
public class Menu extends BaseBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5932085348868818020L;
	
	@Column(value="MENU_ID", id=true)
	private Long menuId;
	@Column("MENU_NAME")
	private String menuName;
	@Column("URL")
	private String url;
	
	/**
	 * @return the menuId
	 */
	public Long getMenuId() {
		return menuId;
	}
	/**
	 * @param menuId the menuId to set
	 */
	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}
	/**
	 * @return the menuName
	 */
	public String getMenuName() {
		return menuName;
	}
	/**
	 * @param menuName the menuName to set
	 */
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
}
