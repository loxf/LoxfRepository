package com.luohj.privileges.model;

import com.luohj.privileges.core.model.BaseBean;
import com.luohj.privileges.core.tags.Column;
import com.luohj.privileges.core.tags.Table;

/**
 * 功能说明:
 * 
 * @author luohj
 * 
 * @Date 2015-9-5 上午11:34:54
 * 
 * 
 * 版本号 | 作者 | 修改时间 | 修改内容
 * 
 */
@Table("TB_USER")
public class User extends BaseBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3166125142462227656L;

	@Column(value = "USER_ID", id = true)
	private Long userId;
	@Column("USER_NAME")
	private String userName;
	@Column("USER_PASSWORD")
	private String userPassword;
	@Column("CREATE_DATE")
	private String createDate;
	
	private String identifyCode ;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getIdentifyCode() {
		return identifyCode;
	}

	public void setIdentifyCode(String identifyCode) {
		this.identifyCode = identifyCode;
	}
}
