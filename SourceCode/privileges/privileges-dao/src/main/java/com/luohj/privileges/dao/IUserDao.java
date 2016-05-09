package com.luohj.privileges.dao;

import com.luohj.privileges.core.dao.IBaseDao;
import com.luohj.privileges.model.User;

/**
 * @author luohj
 */
public interface IUserDao extends IBaseDao {
	User getUser(User user);

	Long insertUser(User user);
	
	User login(User ur);
}
